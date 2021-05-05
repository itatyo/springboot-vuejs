package dev.itatyo.demo.restservice.controller;

import dev.itatyo.demo.Clerk;
import dev.itatyo.demo.ClerkMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ClerkControllerTest {
    private MockMvc mockMvc;
    @InjectMocks
    private ClerkController clerkController;
    @Mock
    private ClerkMapper clerkMapper;

    @BeforeEach
    void initMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(clerkController)
                .setControllerAdvice(new ControllerExceptionHandler()).build();
    }

    @Test
    void getClerk() throws Exception {
        when(clerkMapper.findByID(1)).thenReturn(new Clerk());
        mockMvc.perform(get("/clerk"))
                .andExpect(status().isOk());
    }
}