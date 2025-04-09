package com.degreeflow.controller;

import com.degreeflow.model.SeatAlertSubscription;
import com.degreeflow.service.SeatAlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Seat Alerts",
        description = "Subscribe, check, and manage seat alerts"
)
@CrossOrigin(
        origins = {"*"}
)
@RestController
@RequestMapping({"/api/seat-alerts"})
public class SeatAlertController {
    @Autowired
    private SeatAlertService seatAlertService;

    public SeatAlertController() {
    }

    @Operation(
            summary = "Subscribe to seat alert"
    )
    @PostMapping({"/subscribe"})
    public ResponseEntity<?> subscribe(@RequestBody SeatAlertSubscription subscription) {
        String courseCode = subscription.getCourseCode();
        String term = subscription.getTerm();
        boolean courseOffered = this.seatAlertService.courseExistsInTerm(courseCode, term);
        if (!courseOffered) {
            ResponseEntity.BodyBuilder var10000 = ResponseEntity.status(HttpStatus.NOT_FOUND);
            String var10002 = courseCode.toUpperCase();
            return var10000.body(Map.of("error", "Course " + var10002 + " is not offered in " + term + " (no seat data)."));
        } else {
            SeatAlertSubscription saved = this.seatAlertService.subscribeToSeatAlert(subscription);
            return ResponseEntity.ok(Map.of("message", "Subscription created successfully!", "subscription", saved));
        }
    }

    @Operation(
            summary = "Get all subscriptions"
    )
    @GetMapping
    public List<SeatAlertSubscription> getAllSubscriptions() {
        return this.seatAlertService.getSubscriptions();
    }

    @Operation(
            summary = "Check seat availability manually"
    )
    @GetMapping({"/check"})
    public boolean checkAvailability(@RequestParam String courseCode, @RequestParam String term) {
        return this.seatAlertService.checkSeatAvailability(courseCode, term);
    }

    @Operation(
            summary = "Unsubscribe from alert"
    )
    @DeleteMapping({"/{id}"})
    public void unsubscribe(@PathVariable Long id) {
        this.seatAlertService.deleteSubscription(id);
    }

    @Operation(
            summary = "Send a test alert email"
    )
    @GetMapping({"/send-test-email"})
    public String sendTestEmail(@RequestParam String email) {
        this.seatAlertService.sendTestEmail(email);
        return "Test email sent to: " + email;
    }

    @Operation(
            summary = "Manually trigger seat check + notifications"
    )
    @GetMapping({"/trigger-check"})
    public String triggerCheckManually() {
        this.seatAlertService.checkAndNotifySubscribers();
        return " Manual check triggered!";
    }
}