package com.prasadfencing.dto;

import lombok.Data;

@Data
public class VerifyOtpDTO {
    private String email;
    private String otp;
}
