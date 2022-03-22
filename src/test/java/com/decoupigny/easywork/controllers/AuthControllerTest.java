package com.decoupigny.easywork.controllers;

import com.decoupigny.easywork.models.user.User;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    public void setup() throws Exception {
        mongoTemplate.dropCollection(User.class);

        String jsonUser = new JSONObject()
                .put("firstName", "truc")
                .put("lastName", "Toyota")
                .put("email", "test@test.fr")
                .put("password", "123")
                .toString();

        mvc.perform(post("/api/auth/v1/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotAllowAccessToUnauthenticatedUsers() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/ticket/v1/getAll")).andExpect(status().isUnauthorized());
    }

    @Test
    public void existentUserCanGetTokenAndAuthentication() throws Exception {
        String jsonUser = new JSONObject()
                .put("email", "test@test.fr")
                .put("password", "123")
                .toString();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/auth/v1/signin")
                        .content(jsonUser)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String token = JsonPath.parse(response).read("$.token");

        mvc.perform(MockMvcRequestBuilders.get("/api/ticket/v1/getAll")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

}