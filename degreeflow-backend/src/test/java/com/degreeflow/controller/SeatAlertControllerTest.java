package com.degreeflow.controller;

import com.degreeflow.model.SeatAlertSubscription;
import com.degreeflow.service.SeatAlertService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class SeatAlertControllerTest {

    @Mock
    private SeatAlertService seatAlertService;

    @InjectMocks
    private SeatAlertController seatAlertController;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSubscribe_Success() {
        SeatAlertSubscription input = new SeatAlertSubscription();
        input.setEmail("test@example.com");
        input.setCourseCode("CS101");
        input.setTerm("Fall-2024");

        SeatAlertSubscription saved = new SeatAlertSubscription();
        saved.setId(1L);
        saved.setEmail("test@example.com");
        saved.setCourseCode("CS101");
        saved.setTerm("Fall-2024");

        when(seatAlertService.courseExistsInTerm("CS101", "Fall-2024")).thenReturn(true);
        when(seatAlertService.subscribeToSeatAlert(input)).thenReturn(saved);

        var response = seatAlertController.subscribe(input);
        assertEquals(response.getStatusCodeValue(), 200);

        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertTrue(body.containsKey("message"));
        assertEquals(body.get("subscription"), saved);
    }

    @Test
    public void testSubscribe_CourseNotOffered() {
        SeatAlertSubscription input = new SeatAlertSubscription();
        input.setEmail("test@example.com");
        input.setCourseCode("CS999");
        input.setTerm("Winter-2025");

        when(seatAlertService.courseExistsInTerm("CS999", "Winter-2025")).thenReturn(false);

        var response = seatAlertController.subscribe(input);
        assertEquals(response.getStatusCodeValue(), 404);
    }

    @Test
    public void testGetAllSubscriptions() {
        SeatAlertSubscription sub = new SeatAlertSubscription();
        sub.setId(1L);
        sub.setEmail("test@example.com");
        sub.setCourseCode("CS101");
        sub.setTerm("Fall-2024");

        when(seatAlertService.getSubscriptions()).thenReturn(List.of(sub));

        List<SeatAlertSubscription> result = seatAlertController.getAllSubscriptions();
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getEmail(), "test@example.com");
    }

    @Test
    public void testCheckAvailability() {
        when(seatAlertService.checkSeatAvailability("CS101", "Fall-2024")).thenReturn(true);
        boolean result = seatAlertController.checkAvailability("CS101", "Fall-2024");
        assertTrue(result);
    }

    @Test
    public void testSendTestEmail() {
        doNothing().when(seatAlertService).sendTestEmail("test@example.com");
        String result = seatAlertController.sendTestEmail("test@example.com");
        assertEquals(result, "Test email sent to: test@example.com");
    }

    @Test
    public void testTriggerCheckManually() {
        doNothing().when(seatAlertService).checkAndNotifySubscribers();
        String result = seatAlertController.triggerCheckManually();
        assertEquals(result, " Manual check triggered!");
    }

    @Test
    public void testUnsubscribe() {
        doNothing().when(seatAlertService).deleteSubscription(1L);
        seatAlertController.unsubscribe(1L);
        verify(seatAlertService, times(1)).deleteSubscription(1L);
    }
}
