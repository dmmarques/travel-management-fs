package com.dmmarques.travel_management.controller;

import com.dmmarques.travel_management.model.Activity;
import com.dmmarques.travel_management.service.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(
    origins = {"http://localhost:3000", "http://127.0.0.1:3000"},
    allowCredentials = "true"
)
@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    public ResponseEntity<List<Activity>> listAllTripsByUsername(@RequestParam String username, @RequestParam String tripName) {
        return ResponseEntity.ok(activityService.listAllActivitiesByTripName(username, tripName));
    }

    @PostMapping
    public ResponseEntity<String> createActivity(@Valid @RequestBody Activity activity) {
        return new ResponseEntity<>(activityService.createActivity(activity), HttpStatus.CREATED);
    }

}
