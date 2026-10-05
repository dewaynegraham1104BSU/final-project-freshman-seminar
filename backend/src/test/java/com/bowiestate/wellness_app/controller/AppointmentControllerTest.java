package com.bowiestate.wellness_app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMapping;

class AppointmentControllerTest {

    @Test
    void appointmentController_shouldUseApiRoute() {
        RequestMapping mapping = AppointmentController.class.getAnnotation(RequestMapping.class);

        assertNotNull(mapping);
        assertEquals("/api/appointments", mapping.value()[0]);
    }
}
