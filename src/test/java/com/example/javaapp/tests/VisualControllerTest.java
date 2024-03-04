package com.example.javaapp.tests;

import com.example.javaapp.controllers.VisualController;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(VisualController.class)
public class VisualControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testIndexPage() throws Exception {
        String response = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index.html"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Document document = Jsoup.parse(response);

        assertNotNull(document.getElementsByClass("title"), "Element with ID 'title' not found");
        assertEquals(1, document.getElementsByClass("title").size(), "There are more than one title");
        assertNotNull(document.getElementsByClass("description"), "Element with ID 'description' not found");
        assertEquals(1, document.getElementsByClass("description").size(), "There are more than one description");
    }
}
