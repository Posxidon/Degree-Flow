package com.degreeflow.controller;

import com.degreeflow.model.SeatAlertSubscription;
import com.degreeflow.service.SeatAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/seat-alerts")
public class SeatAlertController {

    @Autowired
    private SeatAlertService seatAlertService;

    //  Subscribe to seat alert
    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestBody SeatAlertSubscription subscription) {
        String courseCode = subscription.getCourseCode();
        String term = subscription.getTerm();

        // 1) Check if the course is recognized in that term
        boolean courseOffered = seatAlertService.courseExistsInTerm(courseCode, term);
        if (!courseOffered) {
            // Return 404 => "Failed to subscribe" if course not offered at all
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "Course " + courseCode.toUpperCase()
                                    + " is not offered in " + term
                                    + " (no seat data)."
                    ));
        }

        // 2) If the course is recognized (even if seats=0), proceed with subscription
        // Because the user wants to be alerted if a seat opens up later
        SeatAlertSubscription saved = seatAlertService.subscribeToSeatAlert(subscription);

        // Return 200 => "Ok subscribed"
        return ResponseEntity.ok(Map.of(
                "message", "Subscription created successfully!",
                "subscription", saved
        ));
    }

    //  Get all subscriptions
    @GetMapping
    public List<SeatAlertSubscription> getAllSubscriptions() {
        return seatAlertService.getSubscriptions();
    }

    //  Check availability manually
    @GetMapping("/check")
    public boolean checkAvailability(@RequestParam String courseCode, @RequestParam String term) {
        return seatAlertService.checkSeatAvailability(courseCode, term);
    }

    //  Unsubscribe
    @DeleteMapping("/{id}")
    public void unsubscribe(@PathVariable Long id) {
        seatAlertService.deleteSubscription(id);
    }

    //  Send a test email
    @GetMapping("/send-test-email")
    public String sendTestEmail(@RequestParam String email) {
        seatAlertService.sendTestEmail(email);
        return "Test email sent to: " + email;
    }

    //  Manually trigger seat checks
    @GetMapping("/trigger-check")
    public String triggerCheckManually() {
        seatAlertService.checkAndNotifySubscribers();
        return " Manual check triggered!";
    }
}
