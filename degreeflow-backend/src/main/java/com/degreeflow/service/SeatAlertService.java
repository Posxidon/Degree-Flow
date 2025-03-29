package com.degreeflow.service;

import com.degreeflow.model.SeatAlertSubscription;
import com.degreeflow.repository.SeatAlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SeatAlertService {

    private final SeatAlertRepository seatAlertRepository;
    private final TimeTableScraperService timeTableScraperService;
    private final JavaMailSender mailSender;

    @Autowired
    public SeatAlertService(
            SeatAlertRepository seatAlertRepository,
            TimeTableScraperService timeTableScraperService,
            JavaMailSender mailSender
    ) {
        this.seatAlertRepository = seatAlertRepository;
        this.timeTableScraperService = timeTableScraperService;
        this.mailSender = mailSender;
    }

    // New method: Returns true if the course is recognized for that term
    // i.e., the scraper returned a key for courseCode
    public boolean courseExistsInTerm(String courseCode, String term) {
        List<String> courses = List.of(courseCode.toUpperCase());
        try {
            Map<String, Object> openSeatsData = timeTableScraperService.getOpenSeats(term, courses);
            System.out.println(" [courseExistsInTerm] Seat data: " + openSeatsData);

            String normalizedCourseCode = courseCode.toUpperCase();
            // If openSeatsData has a key for this course code, it's recognized/offered
            return openSeatsData.containsKey(normalizedCourseCode);

        } catch (IOException e) {
            System.out.println(" Error fetching seat data: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // If an exception or no data => not recognized
    }

    // Student subscribes for seat alerts
    public SeatAlertSubscription subscribeToSeatAlert(SeatAlertSubscription subscription) {
        SeatAlertSubscription savedSubscription = seatAlertRepository.save(subscription);
        sendSubscriptionEmail(
                savedSubscription.getEmail(),
                savedSubscription.getCourseCode(),
                savedSubscription.getTerm()
        );
        return savedSubscription;
    }

    // Basic confirmation email
    private void sendSubscriptionEmail(String to, String courseCode, String term) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(" Seat Alert Subscription Confirmation");
        message.setText("You have successfully subscribed to seat alerts for "
                + courseCode.toUpperCase()
                + " in the " + term
                + " term. We will notify you when a seat becomes available!");

        mailSender.send(message);
    }

    // Check if a course has > 0 open seats (returns true if so)
    public boolean checkSeatAvailability(String courseCode, String term) {
        List<String> courses = List.of(courseCode.toUpperCase());
        System.out.println(" Checking seat availability for: " + courseCode.toUpperCase() + " in term " + term);

        try {
            Map<String, Object> openSeatsData = timeTableScraperService.getOpenSeats(term, courses);
            System.out.println(" Fetched seat data: " + openSeatsData);

            String normalizedCourseCode = courseCode.toUpperCase();
            if (openSeatsData.containsKey(normalizedCourseCode)) {
                Map<String, List<Map<String, Object>>> seatInfo =
                        (Map<String, List<Map<String, Object>>>) openSeatsData.get(normalizedCourseCode);

                for (String sectionKey : seatInfo.keySet()) {
                    for (Map<String, Object> block : seatInfo.get(sectionKey)) {
                        int openSeats = (int) block.get("open_seats");
                        System.out.println(" Section: " + sectionKey + " | Open Seats: " + openSeats);

                        if (openSeats > 0) {
                            System.out.println(" Seats are available for " + normalizedCourseCode);
                            return true; // Found an open seat
                        }
                    }
                }
            } else {
                System.out.println(" No seat info found for " + normalizedCourseCode);
            }
        } catch (IOException e) {
            System.out.println(" Error fetching seat availability: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println(" No available seats for " + courseCode.toUpperCase());
        return false;
    }

    // Notify & remove from DB if seats are available
    private void sendEmailNotification(String email, String courseCode, Long subscriptionId) {
        System.out.println(" Preparing to send email to: " + email);

        if (email == null || email.isEmpty()) {
            System.out.println(" Error: Email address is null or empty!");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Seat Available for " + courseCode.toUpperCase());
            message.setText("A seat has opened up for " + courseCode.toUpperCase()
                    + ". Hurry up and register! \n\nYou will be unsubscribed now.");

            mailSender.send(message);
            System.out.println(" Email successfully sent to: " + email);

            // Remove from subscription after sending
            seatAlertRepository.deleteById(subscriptionId);
            System.out.println(" Unsubscribed " + email + " from alerts.");
        } catch (Exception e) {
            System.out.println(" Error while sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Check every 1 minute for seat openings and notify subscribers
    @Scheduled(fixedRate = 60000)
    public void checkAndNotifySubscribers() {
        System.out.println(" Running scheduled task: Checking for open seats...");
        List<SeatAlertSubscription> subscriptions = seatAlertRepository.findAll();

        for (SeatAlertSubscription subscription : subscriptions) {
            if (checkSeatAvailability(subscription.getCourseCode(), subscription.getTerm())) {
                sendEmailNotification(subscription.getEmail(),
                        subscription.getCourseCode(),
                        subscription.getId());
            }
        }
    }

    // Manual test function for sending an email
    public void sendTestEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(" Test Email from Seat Alert System");
        message.setText("This is a test email to confirm email notifications are working.");

        mailSender.send(message);
        System.out.println(" Test email sent to: " + email);
    }

    // Get all subscriptions
    public List<SeatAlertSubscription> getSubscriptions() {
        return seatAlertRepository.findAll();
    }

    // Get subscription by ID
    public Optional<SeatAlertSubscription> getSubscriptionById(Long id) {
        return seatAlertRepository.findById(id);
    }

    // Unsubscribe
    public void deleteSubscription(Long id) {
        seatAlertRepository.deleteById(id);
    }
}
