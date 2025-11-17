package com.dmmarques.travel_management.service;

import com.dmmarques.travel_management.dto.PartialTripDto;
import com.dmmarques.travel_management.model.Accommodation;
import com.dmmarques.travel_management.model.Activity;
import com.dmmarques.travel_management.model.Travel;
import com.dmmarques.travel_management.model.Trip;
import com.dmmarques.travel_management.repository.TripRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    public List<Trip> listAllTripsByUsername(String username) {
        return tripRepository.findAllByCreatorUsername(username);
    }

    public Trip listAllTripsByTripName(String tripName) {
        return tripRepository.findByName(tripName).get();
    }

    public Trip listTripByIdAndUsername(String username, String tripId) {
        return tripRepository.findByCreatorUsernameAndId(username, tripId).get();
    }

    public Trip createTrip(Trip trip) {
        if(existsTripWithName(trip.creatorUsername(), trip.name())) {
            throw new IllegalArgumentException("Trip with name " + trip.name() + " already exists.");
        }
        
        return tripRepository.save(trip);
    }

    private boolean existsTripWithName(String username, String tripName) {
        return tripRepository.existsByCreatorUsernameAndName(username, tripName);
    }

    public void updateTrip(@Valid Trip trip) {
        tripRepository.save(trip);
    }

    public Activity addActivityToTrip(String tripId, @Valid Activity activity) {
        tripRepository.findById(tripId).ifPresent(trip -> {
            List<Activity> activityList = trip.activityList();

            Activity activityWithId = activity.id() == null ?
                                      new Activity(
                                          new ObjectId().toString(),
                                          activity.name(),
                                          activity.address(),
                                          activity.category(),
                                          activity.creatorUsername(),
                                          activity.creationDate(),
                                          activity.activityDate(),
                                          activity.cost(),
                                          activity.description(),
                                          activity.latitude(),
                                          activity.longitude()
                                      ) : activity;
            activityList.add(activityWithId);
            tripRepository.save(trip);
        });
        return activity;
    }

    public void deleteActivity(String tripId, String activityId) {
        tripRepository.findById(tripId).ifPresent(trip -> {
            trip.activityList().removeIf(activity -> activity.id().equals(activityId));
            tripRepository.save(trip);
        });
    }

    public Activity updateActivityFromTrip(String tripId, @Valid Activity activity) {
        final Activity[] updatedActivity = {null};
        tripRepository.findById(tripId).ifPresent(trip -> {
            List<Activity> activityList = trip.activityList();
            activityList.removeIf(a -> a.id().equals(activity.id()));
            activityList.add(activity);
            tripRepository.save(trip);
            updatedActivity[0] = activity;
        });
        return updatedActivity[0];
    }

    public String updateTripWithPartialInfo(String tripId, @Valid PartialTripDto partialTripDto) {
        tripRepository.findById(tripId).ifPresent(trip -> {
            Trip updatedTrip = new Trip(
                trip.id(),
                partialTripDto.getName() != null ? partialTripDto.getName() : trip.name(),
                trip.description(),
                trip.creatorUsername(),
                trip.creationDate(),
                partialTripDto.getStartDate() != null ? partialTripDto.getStartDate() : trip.startDate(),
                partialTripDto.getEndDate() != null ? partialTripDto.getEndDate() : trip.endDate(),
                trip.participantUsernames(),
                trip.accommodations(),
                trip.activityList(),
                trip.travelList(),
                partialTripDto.getEndDate() != null ? partialTripDto.getBudget() : trip.budget()
            );
            tripRepository.save(updatedTrip);
        });
        return tripId;
    }

    public String updateTripWithAccommodation(String tripId, @Valid Accommodation accommodation) {
        tripRepository.findById(tripId).ifPresent(trip -> {
            List<Accommodation> accommodations = trip.accommodations();
            if (accommodation.getId() == null || accommodation.getId().isBlank()) {
                accommodation.setId(new ObjectId().toString());
            }
            if (accommodations == null) {
                accommodations = new ArrayList<>();
                Trip updatedTrip = new Trip(
                    trip.id(),
                    trip.name(),
                    trip.description(),
                    trip.creatorUsername(),
                    trip.creationDate(),
                    trip.startDate(),
                    trip.endDate(),
                    trip.participantUsernames(),
                    accommodations,
                    trip.activityList(),
                    trip.travelList(),
                    trip.budget()
                );
                accommodations.add(accommodation);
                tripRepository.save(updatedTrip);
            } else {
                accommodations.add(accommodation);
                tripRepository.save(trip);
            }
        });
        return tripId;
    }

    public List<Accommodation> listAllTripAccomodations(String tripId) {
        return tripRepository.findById(tripId).map(Trip::accommodations).orElse(List.of());
    }

    public void addTravelToTrip(String tripId, @Valid Travel activity) {
        tripRepository.findById(tripId).ifPresent(trip -> {
            List<Travel> travelList = trip.travelList() == null ? new ArrayList<>() : new ArrayList<>(trip.travelList());
            travelList.add(activity);

            Trip updatedTrip = new Trip(
                trip.id(),
                trip.name(),
                trip.description(),
                trip.creatorUsername(),
                trip.creationDate(),
                trip.startDate(),
                trip.endDate(),
                trip.participantUsernames(),
                trip.accommodations(),
                trip.activityList(),
                travelList,
                trip.budget()
            );
            tripRepository.save(updatedTrip);
        });
    }

    public void updateTravelFromTrip(String tripId, @Valid Travel travel) {
        tripRepository.findById(tripId).ifPresent(trip -> {
            List<Travel> travelList = trip.travelList() == null ? new ArrayList<>() : new ArrayList<>(trip.travelList());

            // Check if travel with the same name exists and update it
            boolean found = false;
            for (int i = 0; i < travelList.size(); i++) {
                if (travelList.get(i).getName().equals(travel.getName())) {
                    travelList.set(i, travel);
                    found = true;
                    break;
                }
            }

            // If not found, you might want to add it (optional)
            if (!found) {
                travelList.add(travel);
            }

            Trip updatedTrip = new Trip(
                trip.id(),
                trip.name(),
                trip.description(),
                trip.creatorUsername(),
                trip.creationDate(),
                trip.startDate(),
                trip.endDate(),
                trip.participantUsernames(),
                trip.accommodations(),
                trip.activityList(),
                travelList,
                trip.budget()
            );
            tripRepository.save(updatedTrip);
        });
    }

    public void updateAccommodationFromTrip(String tripId, @Valid Accommodation accommodation) {
        tripRepository.findById(tripId).ifPresent(trip -> {
            List<Accommodation> accommodations = trip.accommodations() == null ? new ArrayList<>() : new ArrayList<>(trip.accommodations());
            
            // Check if accommodation with the same name exists and update it
            boolean found = false;
            for (int i = 0; i < accommodations.size(); i++) {
                if (accommodations.get(i).getName().equals(accommodation.getName())) {
                    accommodations.set(i, accommodation);
                    found = true;
                    break;
                }
            }
            
            // If not found, you might want to add it (optional)
            if (!found) {
                accommodations.add(accommodation);
            }
            
            Trip updatedTrip = new Trip(
                trip.id(),
                trip.name(),
                trip.description(),
                trip.creatorUsername(),
                trip.creationDate(),
                trip.startDate(),
                trip.endDate(),
                trip.participantUsernames(),
                accommodations,
                trip.activityList(),
                trip.travelList(),
                trip.budget()
            );
            tripRepository.save(updatedTrip);
        });
    }

    public void deleteAccommodation(String tripId, String accommodationId) {
        tripRepository.findById(tripId).ifPresent(trip -> {
            List<Accommodation> accommodations = trip.accommodations();
            accommodations.removeIf(accommodation -> accommodation.getId().equals(accommodationId));
            tripRepository.save(trip);
        });
    }

    public void deleteTravelFromTrip(String tripId, String travelName) {
        tripRepository.findById(tripId).ifPresent(trip -> {
            List<Travel> travelList = trip.travelList();
            travelList.removeIf(travel -> travel.getName().equals(travelName));
            tripRepository.save(trip);
        });
    }
}
