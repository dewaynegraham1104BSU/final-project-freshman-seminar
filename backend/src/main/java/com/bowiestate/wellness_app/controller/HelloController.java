package com.bowiestate.wellness_app.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HelloController {

    @PostMapping("/message")
    public MessageResponse saveMessage(@RequestBody MessageRequest req) {
        // Here you could save to DB later, or process input
        return new MessageResponse("You entered: " + req.getText());
    }

    public static class MessageRequest {
        private String text;

        public MessageRequest() {
        }

        public MessageRequest(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class MessageResponse {
        private String message;

        public MessageResponse() {
        }

        public MessageResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
