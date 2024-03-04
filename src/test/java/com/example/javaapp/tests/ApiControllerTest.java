package com.example.javaapp.tests;

import com.example.javaapp.controllers.ApiController;
import com.example.javaapp.models.MessageModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApiController.class)
@AutoConfigureMockMvc
public class ApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void greetingShouldReturnDefaultMessage() throws Exception {
        String responseJson = mockMvc.perform(get("/api/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        MessageModel responseObject = objectMapper.readValue(responseJson, MessageModel.class);

        assertEquals("Hi!", responseObject.text(), "The message isn't 'Hi!'");
        assertTrue(responseObject.success(), "The operation was not successful");
    }
}
