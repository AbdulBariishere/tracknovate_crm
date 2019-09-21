package com.example.tracknovate_crm;
import javafx.application.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringJUnit4ClassRunner.class)
public class TracknovateCrmApplicationtests {
    private MockMvc mockMvcUserController,
                    mockMvcLeadController;
/*
    @InjectMocks
    private UserController UserController;
    @InjectMocks
    private LeadController LeadController;


    @Before
    public void setup() throws Exception {
        mockMvcUserController = MockMvcBuilders.standaloneSetup(UserController)
                .build();
        mockMvcLeadController=MockMvcBuilders.standaloneSetup(LeadController)
                .build();
    }

    @Test
    public void TestUserController() {
        try {
            mockMvcUserController.perform(get("/getallusers")
                    .param("Email","abdul@gmail.com").param("Token","123456")
            ).andExpect(MockMvcResultMatchers
                    .status().isOk());

        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    @Test
    public void TestLeadController() {
        try {
            mockMvcLeadController.perform(get("/getallleads")
            .param("Email","abdul@gmail.com")
                            .param("Token","123456")
            ).andExpect(MockMvcResultMatchers
                    .status().isOk());
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

 */
}
