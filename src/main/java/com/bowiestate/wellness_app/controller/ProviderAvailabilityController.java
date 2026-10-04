package com.bowiestate.wellness_app.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/providers")
public class ProviderAvailabilityController {

    @GetMapping("/availability")
    public ProviderAvailabilityResponse availability() {
        return new ProviderAvailabilityResponse(List.of(
                new ProviderAvailabilityItem("MindCare Clinic", List.of("2026-10-01 10:00", "2026-10-01 14:00")),
                new ProviderAvailabilityItem("Riverline Therapy", List.of("2026-10-02 09:30", "2026-10-02 16:00")),
                new ProviderAvailabilityItem("CalmBridge Counseling", List.of("2026-10-03 11:00"))
        ));
    }

    public static class ProviderAvailabilityResponse {
        private final List<ProviderAvailabilityItem> items;

        public ProviderAvailabilityResponse(List<ProviderAvailabilityItem> items) {
            this.items = items;
        }

        public List<ProviderAvailabilityItem> getItems() {
            return items;
        }
    }

    public static class ProviderAvailabilityItem {
        private final String name;
        private final List<String> slots;

        public ProviderAvailabilityItem(String name, List<String> slots) {
            this.name = name;
            this.slots = slots;
        }

        public String getName() {
            return name;
        }

        public List<String> getSlots() {
            return slots;
        }
    }
}
