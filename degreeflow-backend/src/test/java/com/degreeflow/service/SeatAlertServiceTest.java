package com.degreeflow.service;

import com.degreeflow.model.SeatAlertSubscription;
import com.degreeflow.repository.SeatAlertRepository;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class SeatAlertServiceTest {

    private SeatAlertService seatAlertService;
    private SeatAlertRepository seatAlertRepository;
    private TimeTableScraperService timeTableScraperService;
    private JavaMailSender mailSender;

    @BeforeMethod
    public void setUp() {
        seatAlertRepository = mock(SeatAlertRepository.class);
        timeTableScraperService = mock(TimeTableScraperService.class);
        mailSender = mock(JavaMailSender.class);
        seatAlertService = new SeatAlertService(seatAlertRepository, timeTableScraperService, mailSender);
    }

    @Test
    public void testSubscribeToSeatAlert_SavesAndSendsEmail() {
        SeatAlertSubscription subscription = new SeatAlertSubscription();
        subscription.setEmail("test@example.com");
        subscription.setCourseCode("CS101");
        subscription.setTerm("Fall-2024");

        when(seatAlertRepository.save(any())).thenReturn(subscription);

        SeatAlertSubscription saved = seatAlertService.subscribeToSeatAlert(subscription);

        assertEquals(saved.getEmail(), "test@example.com");

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());

        SimpleMailMessage sentMsg = messageCaptor.getValue();
        assertEquals(sentMsg.getTo()[0], "test@example.com");
        assertTrue(sentMsg.getText().contains("CS101"));
    }

    @Test
    public void testGetSubscriptions_ReturnsList() {
        SeatAlertSubscription s1 = new SeatAlertSubscription();
        s1.setEmail("user1@example.com");
        s1.setCourseCode("CS101");

        SeatAlertSubscription s2 = new SeatAlertSubscription();
        s2.setEmail("user2@example.com");
        s2.setCourseCode("MATH1B03");

        when(seatAlertRepository.findAll()).thenReturn(List.of(s1, s2));

        List<SeatAlertSubscription> result = seatAlertService.getSubscriptions();
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getEmail(), "user1@example.com");
    }

    @Test
    public void testCheckSeatAvailability_ReturnsTrueIfAvailable() throws Exception {
        Map<String, Object> mockSeatData = Map.of(
                "CS101", Map.of("LEC", List.of(Map.of("open_seats", 5)))
        );

        when(timeTableScraperService.getOpenSeats(any(), any())).thenReturn(mockSeatData);

        boolean result = seatAlertService.checkSeatAvailability("CS101", "Fall-2024");
        assertTrue(result);
    }

    @Test
    public void testCheckSeatAvailability_ReturnsFalseIfNoneAvailable() throws Exception {
        Map<String, Object> mockSeatData = Map.of(
                "CS101", Map.of("LEC", List.of(Map.of("open_seats", 0)))
        );

        when(timeTableScraperService.getOpenSeats(any(), any())).thenReturn(mockSeatData);

        boolean result = seatAlertService.checkSeatAvailability("CS101", "Fall-2024");
        assertFalse(result);
    }

    @Test
    public void testDeleteSubscription_CallsRepositoryDelete() {
        seatAlertService.deleteSubscription(123L);
        verify(seatAlertRepository).deleteById(123L);
    }

    @Test
    public void testGetSubscriptionById_ReturnsOptional() {
        SeatAlertSubscription sub = new SeatAlertSubscription();
        sub.setEmail("a@b.com");

        when(seatAlertRepository.findById(1L)).thenReturn(Optional.of(sub));

        Optional<SeatAlertSubscription> result = seatAlertService.getSubscriptionById(1L);
        assertTrue(result.isPresent());
        assertEquals(result.get().getEmail(), "a@b.com");
    }
}
