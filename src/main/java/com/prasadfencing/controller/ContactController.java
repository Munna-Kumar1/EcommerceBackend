package com.prasadfencing.controller;

import com.prasadfencing.dto.ContactRequest;
import com.prasadfencing.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private  final ContactService contactService;

    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody ContactRequest request){
        contactService.sendContactEmails(request);

        return ResponseEntity.ok("Message send successfully");
    }
}
