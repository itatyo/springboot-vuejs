package dev.itatyo.demo.restservice.controller;

import dev.itatyo.demo.Clerk;
import dev.itatyo.demo.ClerkMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class ClerkController {
    private final ClerkMapper clerkMapper;

    public ClerkController(ClerkMapper clerkMapper) {
        this.clerkMapper = clerkMapper;
    }

    @GetMapping("/clerk")
    public Clerk clerk(@RequestParam(value = "id", defaultValue = "1") int id) {
        return getClerk(id);
    }
    @GetMapping("/clerk/{id}")
    public Clerk clerk_id (@PathVariable("id") int id) {
        return getClerk(id);
    }

    private Clerk getClerk(int id) {
        Clerk clerk = clerkMapper.findByID(id);
        if (clerk.equals(null)) {
            throw new RuntimeException();
        }
        return clerk;
    }
}
