package com.dmmarques.travel_management.controller;

import com.dmmarques.travel_management.model.Activity;
import com.dmmarques.travel_management.model.Trip;
import com.dmmarques.travel_management.service.TripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TripControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TripService tripService;

    @InjectMocks
    private TripController tripController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(tripController).build();
    }

    @Test
    @DisplayName("GET /trips/{username} returns list of trips")
    void listAllTripsByUsername_returnsTrips() throws Exception {
        //GIVEN
        Trip trip = sampleTrip("t1");

        //WHEN
        when(tripService.listAllTripsByUsername("john"))
            .thenReturn(List.of(trip));

        //THEN
        mockMvc.perform(get("/trips/john"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value("t1"))
            .andExpect(jsonPath("$[0].name").value(trip.name()));
    }

    @Test
    @DisplayName("POST /trips/trip creates a trip and returns id in body")
    void createTrip_returnsCreatedWithId() throws Exception {
        Trip saved = sampleTrip("generated-id");
        when(tripService.createTrip(any())).thenReturn(saved);

        String body = "{" +
            "\"name\":\"Trip\"," +
            "\"description\":\"Desc\"," +
            "\"creatorUsername\":\"john\"," +
            "\"creationDate\":\"" + LocalDateTime.now() + "\"," +
            "\"startDate\":\"" + LocalDate.now() + "\"," +
            "\"endDate\":\"" + LocalDate.now().plusDays(3) + "\"," +
            "\"participantUsernames\":[\"john\"]," +
            "\"accommodations\":[]," +
            "\"activityList\":[]," +
            "\"travelList\":[]," +
            "\"budget\":0}";

        mockMvc.perform(post("/trips/trip")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated())
            .andExpect(content().string("generated-id"))
            .andExpect(header().string("Location", "/trip/generated-id"));
    }

    @Test
    @DisplayName("GET /trips?username&tripId returns trip")
    void getTripByUsernameAndId_returnsTrip() throws Exception {
        Trip trip = sampleTrip("t2");
        when(tripService.listTripByIdAndUsername("john", "t2")).thenReturn(trip);

        mockMvc.perform(get("/trips").param("username", "john").param("tripId", "t2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("t2"))
            .andExpect(jsonPath("$.name").value(trip.name()));
    }

    @Test
    @DisplayName("PUT /trips/trip/{tripId} returns the activity payload")
    void addActivityToTrip_returnsActivity() throws Exception {
        Activity activity = new Activity("a1", "Museum", "Addr", "CAT", "john",
            LocalDateTime.now(), LocalDateTime.now(), new BigDecimal("10.00"), null, null, null);
        when(tripService.addActivityToTrip(Mockito.eq("t3"), any(Activity.class))).thenReturn(activity);

        String body = "{" +
            "\"name\":\"Museum\"," +
            "\"address\":\"Addr\"," +
            "\"category\":\"CAT\"," +
            "\"creatorUsername\":\"john\"," +
            "\"creationDate\":\"" + LocalDateTime.now() + "\"," +
            "\"activityDate\":\"" + LocalDateTime.now() + "\"," +
            "\"cost\":10}";

        mockMvc.perform(put("/trips/trip/{tripId}", "t3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Museum"))
            .andExpect(jsonPath("$.category").value("CAT"));
    }

    @Test
    @DisplayName("GET /trips/trip/{tripname} returns trip by name")
    void listAllTripsByName_returnsTrip() throws Exception {
        Trip trip = sampleTrip("tname");
        when(tripService.listAllTripsByTripName("Beach")).thenReturn(trip);

        mockMvc.perform(get("/trips/trip/{tripname}", "Beach"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("tname"))
            .andExpect(jsonPath("$.name").value(trip.name()));
    }

    @Test
    @DisplayName("PUT /trips/trip updates a trip")
    void updateTrip_returnsOk() throws Exception {
        String body = "{" +
            "\"id\":\"tid\"," +
            "\"name\":\"Trip\"," +
            "\"description\":\"Desc\"," +
            "\"creatorUsername\":\"john\"," +
            "\"creationDate\":\"" + LocalDateTime.now() + "\"," +
            "\"startDate\":\"" + LocalDate.now() + "\"," +
            "\"endDate\":\"" + LocalDate.now().plusDays(3) + "\"," +
            "\"participantUsernames\":[\"john\"]," +
            "\"accommodations\":[]," +
            "\"activityList\":[]," +
            "\"travelList\":[]," +
            "\"budget\":0}";

        mockMvc.perform(put("/trips/trip")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /trips/trip/partial/{tripId} returns OK and body")
    void updateTripPartial_returnsOkWithBody() throws Exception {
        when(tripService.updateTripWithPartialInfo(eq("t4"), any())).thenReturn("t4");

        String body = "{" +
            "\"name\":\"NewName\"," +
            "\"startDate\":\"" + LocalDate.now().plusDays(1) + "\"}";

        mockMvc.perform(put("/trips/trip/partial/{tripId}", "t4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(content().string("t4"));
    }

    @Test
    @DisplayName("PUT /trips/trip/{tripId}/accommodation adds accommodation")
    void addAccommodation_returnsOk() throws Exception {
        when(tripService.updateTripWithAccommodation(eq("t5"), any())).thenReturn("t5");
        String body = "{" +
            "\"id\":\"a1\",\"name\":\"Hotel\",\"googlePlaceId\":\"gpid\"}";
        mockMvc.perform(put("/trips/trip/{tripId}/accommodation", "t5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(content().string("t5"));
    }

    @Test
    @DisplayName("PUT /trips/trip/{tripId}/accommodation/update updates accommodation")
    void updateAccommodation_returnsOk() throws Exception {
        String body = "{" +
            "\"id\":\"a1\",\"name\":\"Hotel\",\"googlePlaceId\":\"gpid\"}";
        mockMvc.perform(put("/trips/trip/{tripId}/accommodation/update", "t5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /trips/trip/{tripId}/accommodation deletes accommodation")
    void deleteAccommodation_returnsOk() throws Exception {
        mockMvc.perform(delete("/trips/trip/{tripId}/accommodation", "t5")
                .param("accommodationId", "a1"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /trips/trip/{tripId}/accommodation returns list")
    void getAccommodations_returnsList() throws Exception {
        when(tripService.listAllTripAccomodations("t6")).thenReturn(List.of());
        mockMvc.perform(get("/trips/trip/{tripId}/accommodation", "t6"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("PUT /trips/activity/{tripId} updates activity and returns payload")
    void updateActivity_returnsPayload() throws Exception {
        String body = "{" +
            "\"id\":\"a1\"," +
            "\"name\":\"Museum\"," +
            "\"address\":\"Addr\"," +
            "\"category\":\"CAT\"," +
            "\"creatorUsername\":\"john\"," +
            "\"creationDate\":\"" + LocalDateTime.now() + "\"," +
            "\"activityDate\":\"" + LocalDateTime.now() + "\"}";
        mockMvc.perform(put("/trips/activity/{tripId}", "t3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value("a1"));
    }

    @Test
    @DisplayName("DELETE /trips/trip/{tripId}?activityId deletes and returns OK")
    void deleteActivity_returnsOk() throws Exception {
        mockMvc.perform(delete("/trips/trip/{tripId}", "t3")
                .param("activityId", "a1"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /trips/trip/{tripId}/travel adds travel")
    void addTravel_returnsOk() throws Exception {
        String body = "{" +
            "\"id\":\"tr1\"," +
            "\"name\":\"Flight\"," +
            "\"transport\":\"PLANE\"," +
            "\"fromLat\":\"1\"," +
            "\"fromLng\":\"2\"," +
            "\"departureDate\":\"" + LocalDateTime.now() + "\"," +
            "\"toLat\":\"3\"," +
            "\"toLng\":\"4\"," +
            "\"arrivalDate\":\"" + LocalDateTime.now().plusHours(2) + "\"," +
            "\"estimatedDuration\":\"2h\"," +
            "\"distance\":\"100km\"}";
        mockMvc.perform(put("/trips/trip/{tripId}/travel", "t7")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PUT /trips/trip/{tripId}/travel/update updates travel")
    void updateTravel_returnsOk() throws Exception {
        String body = "{" +
            "\"id\":\"tr1\"," +
            "\"name\":\"Flight\"," +
            "\"transport\":\"PLANE\"," +
            "\"fromLat\":\"1\"," +
            "\"fromLng\":\"2\"," +
            "\"departureDate\":\"" + LocalDateTime.now() + "\"," +
            "\"toLat\":\"3\"," +
            "\"toLng\":\"4\"," +
            "\"arrivalDate\":\"" + LocalDateTime.now().plusHours(2) + "\"," +
            "\"estimatedDuration\":\"2h\"," +
            "\"distance\":\"100km\"}";
        mockMvc.perform(put("/trips/trip/{tripId}/travel/update", "t7")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /trips/trip/{tripId}/travel deletes travel")
    void deleteTravel_returnsOk() throws Exception {
        mockMvc.perform(delete("/trips/trip/{tripId}/travel", "t7")
                .param("travelName", "Flight"))
            .andExpect(status().isOk());
    }

    private Trip sampleTrip(String id) {
        return new Trip(id, "Trip", "Desc", "john", LocalDateTime.now(),
            LocalDate.now(), LocalDate.now().plusDays(3), List.of("john"),
            new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), BigDecimal.ZERO);
    }
}
