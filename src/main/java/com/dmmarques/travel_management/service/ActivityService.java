package com.dmmarques.travel_management.service;

import com.dmmarques.travel_management.model.Activity;
import com.dmmarques.travel_management.model.Trip;
import com.dmmarques.travel_management.repository.TripRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final TripRepository tripRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Activity> listAllActivitiesByTripName(String username, String tripName) {
        Trip trip = tripRepository.findByCreatorUsernameAndName(username, tripName)
                                  .orElseThrow(() -> new IllegalArgumentException("Trip with name " + tripName + " does not exist for user " + username));

        List<String> activityIds = new ArrayList<>();
        if (activityIds.isEmpty()) {
            return List.of();
        }

        //return activityRepository.findAllById(activityIds);
        return null;
    }

    public String createActivity(@Valid Activity activity) {
        //String savedActivityId = activityRepository.save(activity).id();
        String savedActivityId = null;

        Update update = new Update().push("acitivityIds", savedActivityId);
        mongoTemplate.updateFirst(
            Query.query(Criteria.where("_id").is("")),
            update,
            Trip.class
        );

        return savedActivityId;
    }

}
