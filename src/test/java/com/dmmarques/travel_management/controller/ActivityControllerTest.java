package com.dmmarques.travel_management.controller;

import com.dmmarques.travel_management.model.Activity;
import com.dmmarques.travel_management.service.ActivityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ActivityControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ActivityService activityService;

    @InjectMocks
    private ActivityController activityController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(activityController).build();
    }

    @Test
    @DisplayName("GET /activities?username&tripName returns list of activities")
    void listActivities_returnsOk() throws Exception {
        Activity a = new Activity("a1", "Museum", "Addr", "CAT", "john", LocalDateTime.now(), LocalDateTime.now(), null, null, null, null);
        when(activityService.listAllActivitiesByTripName("john", "Trip")).thenReturn(List.of(a));

        mockMvc.perform(get("/activities").param("username", "john").param("tripName", "Trip"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value("a1"))
            .andExpect(jsonPath("$[0].name").value("Museum"));
    }

    @Test
    @DisplayName("POST /activities creates activity id and returns 201")
    void createActivity_returnsCreated() throws Exception {
        when(activityService.createActivity(any(Activity.class))).thenReturn("aid");

        String body = "{" +
            "\"name\":\"Museum\"," +
            "\"address\":\"Addr\"," +
            "\"category\":\"CAT\"," +
            "\"creatorUsername\":\"john\"," +
            "\"creationDate\":\"" + LocalDateTime.now() + "\"," +
            "\"activityDate\":\"" + LocalDateTime.now() + "\"}";

        mockMvc.perform(post("/activities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated())
            .andExpect(content().string("aid"));
    }
}
