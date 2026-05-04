package com.prasadfencing.dto;

import lombok.Data;

@Data
public class ContactRequest {

    private String name;
    private String email;
    private String phone_no;
    private String subject;
    private String message;
}
