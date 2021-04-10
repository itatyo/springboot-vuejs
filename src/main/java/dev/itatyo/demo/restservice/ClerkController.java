package dev.itatyo.demo.restservice;

import dev.itatyo.demo.Clerk;
import dev.itatyo.demo.ClerkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClerkController {
    private final ClerkMapper clerkMapper;

    public ClerkController(ClerkMapper clerkMapper) {
        this.clerkMapper = clerkMapper;
    }

    @GetMapping("/clerk")
    public Clerk clerk(@RequestParam(value = "id", defaultValue = "1") int id) {
        return clerkMapper.findByID(id);
    }
}
