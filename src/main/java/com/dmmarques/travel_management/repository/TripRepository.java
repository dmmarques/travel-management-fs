package com.dmmarques.travel_management.repository;

import com.dmmarques.travel_management.model.Trip;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends MongoRepository<Trip, String> {

    List<Trip> findAllByCreatorUsername(String username);

    Optional<Trip> findByName(String name);

    Optional<Trip> findById(String id);

    Optional<Trip> findByCreatorUsernameAndId(String username, String id);

    boolean existsByCreatorUsernameAndName(String creatorUsername, String name);

    Optional<Trip> findByCreatorUsernameAndName(String creatorUsername, String name);

}
