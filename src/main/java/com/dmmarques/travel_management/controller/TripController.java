package com.dmmarques.travel_management.controller;

import com.dmmarques.travel_management.dto.PartialTripDto;
import com.dmmarques.travel_management.model.Accommodation;
import com.dmmarques.travel_management.model.Activity;
import com.dmmarques.travel_management.model.Travel;
import com.dmmarques.travel_management.model.Trip;
import com.dmmarques.travel_management.service.TripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;


@CrossOrigin(
    origins = {"http://localhost:3000", "http://127.0.0.1:3000"},
    allowCredentials = "true"
)
@RestController
@RequestMapping("/trips")
@RequiredArgsConstructor
@Slf4j
public class TripController {

    private final TripService tripService;

    @GetMapping("/{username}")
    public ResponseEntity<List<Trip>> listAllTripsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(tripService.listAllTripsByUsername(username));
    }

    @GetMapping("/trip/{tripname}")
    public ResponseEntity<Trip> listAllTripsByName(@PathVariable String tripname) {
        log.info("listAllTripsByName: {}", tripname);
        return ResponseEntity.ok(tripService.listAllTripsByTripName(tripname));
    }

    @GetMapping()
    public ResponseEntity<Trip> listAllTripsByName(@RequestParam String username, @RequestParam String tripId) {
        log.info("Fetching trip {} of {}", tripId, username);
        return ResponseEntity.ok(tripService.listTripByIdAndUsername(username, tripId));
    }

    @PostMapping("/trip")
    public ResponseEntity<String> createTrip(@Valid @RequestBody Trip trip) {
        log.info("Creating new trip");
        Trip savedTrip = tripService.createTrip(trip);
        URI location = URI.create("/trip/" + savedTrip.id());
        log.info("Trip created with id: {}", savedTrip.id());
        return ResponseEntity
            .created(location)
            .body(savedTrip.id()
            );
    }

    @PutMapping("/trip")
    public ResponseEntity<String> updateTrip(@Valid @RequestBody Trip trip) {
        log.info("Updating trip {}", trip.id());
        tripService.updateTrip(trip);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/trip/{tripId}")
    public ResponseEntity<Activity> addActivityToTrip(@PathVariable String tripId, @Valid @RequestBody Activity activity) {
        log.info("Adding activity to trip {}", tripId);
        tripService.addActivityToTrip(tripId, activity);
        return new ResponseEntity<>(activity, HttpStatus.OK);
    }

    @PutMapping("/trip/partial/{tripId}")
    public ResponseEntity<String> updateTrip(@PathVariable String tripId, @Valid @RequestBody PartialTripDto partialTripDto) {
        log.info("Updating trip {}", tripId);
        return new ResponseEntity<>(tripService.updateTripWithPartialInfo(tripId, partialTripDto), HttpStatus.OK);
    }

    @PutMapping("/trip/{tripId}/accommodation")
    public ResponseEntity<String> updateTripWithAccommodation(@PathVariable String tripId, @Valid @RequestBody Accommodation accommodation) {
        log.info("Adding accommodation to trip {}", tripId);
        return new ResponseEntity<>(tripService.updateTripWithAccommodation(tripId, accommodation), HttpStatus.OK);
    }

    @PutMapping("/trip/{tripId}/accommodation/update")
    public ResponseEntity<HttpStatus> updateAccommodationFromTrip(@PathVariable String tripId, @Valid @RequestBody Accommodation accommodation) {
        log.info("Updating accommodation from trip {}", tripId);
        tripService.updateAccommodationFromTrip(tripId, accommodation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/trip/{tripId}/accommodation")
    public ResponseEntity<HttpStatus> deleteAccommodationFromTrip(@PathVariable String tripId, @RequestParam String accommodationId) {
        log.info("Deleting accommodation from trip {}", tripId);
        tripService.deleteAccommodation(tripId, accommodationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/trip/{tripId}/accommodation")
    public ResponseEntity<List<Accommodation>> getTripAccommodations(@PathVariable String tripId) {
        log.info("Fectching trip {} accommodations ", tripId);
        return new ResponseEntity<>(tripService.listAllTripAccomodations(tripId), HttpStatus.OK);
    }

    @PutMapping("/activity/{tripId}")
    public ResponseEntity<Activity> updateActivityFromTrip(@PathVariable String tripId, @Valid @RequestBody Activity activity) {
        log.info("Updating activity from trip {}", tripId);
        tripService.updateActivityFromTrip(tripId, activity);
        return new ResponseEntity<>(activity, HttpStatus.OK);
    }

    @DeleteMapping("/trip/{tripId}")
    public ResponseEntity<String> deleteActivityFromTrip(@PathVariable String tripId, @RequestParam String activityId) {
        log.info("Updating trip {}", tripId);
        tripService.deleteActivity(tripId, activityId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/trip/{tripId}/travel")
    public ResponseEntity<String> addTravelToTrip(@PathVariable String tripId, @Valid @RequestBody Travel activity) {
        tripService.addTravelToTrip(tripId, activity);
        log.info("Adding travel details to trip {}", tripId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/trip/{tripId}/travel/update")
    public ResponseEntity<String> updateTravelFromTrip(@PathVariable String tripId, @Valid @RequestBody Travel activity) {
        tripService.updateTravelFromTrip(tripId, activity);
        log.info("Updating travel details from trip {}", tripId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/trip/{tripId}/travel")
    public ResponseEntity<String> deleteTravelFromTrip(@PathVariable String tripId, String travelName) {
        tripService.deleteTravelFromTrip(tripId, travelName);
        log.info("Updating travel details from trip {}", tripId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
