package dev.itatyo.demo.restservice.controller;

import dev.itatyo.demo.Clerk;
import dev.itatyo.demo.ClerkMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ClerkController {
    private final ClerkMapper clerkMapper;

    public ClerkController(ClerkMapper clerkMapper) {
        this.clerkMapper = clerkMapper;
    }

    @GetMapping("/clerk")
    public Clerk clerk(@RequestParam(value = "id", defaultValue = "1") int id) {
        Clerk clerk = clerkMapper.findByID(id);
        if (clerk.equals(null)) {
            throw new RuntimeException();
        }
        return clerk;
    }
}
