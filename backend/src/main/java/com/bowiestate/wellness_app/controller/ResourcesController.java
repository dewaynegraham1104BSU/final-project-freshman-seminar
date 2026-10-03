package com.bowiestate.wellness_app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resources")
public class ResourcesController {

    @GetMapping
    public ResourcesResponse getResources() {
        return new ResourcesResponse(List.of(
                new ResourceItem("Crisis support", "https://example.com/crisis", "Emergency"),
                new ResourceItem("Managing anxiety", "https://example.com/anxiety", "Anxiety"),
                new ResourceItem("Sleep support", "https://example.com/sleep", "Sleep"),
                new ResourceItem("Therapy basics", "https://example.com/therapy", "General")
        ));
    }

    public record ResourcesResponse(List<ResourceItem> items) {}
    public record ResourceItem(String title, String url, String category) {}
}
