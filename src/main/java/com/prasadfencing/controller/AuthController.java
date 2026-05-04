package com.prasadfencing.controller;

import com.prasadfencing.dto.*;
import com.prasadfencing.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request){
        authService.signup(request);
        return ResponseEntity.ok("OTP sent to email");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpDTO dto)
    {
        authService.verifyOtp(dto);
        return ResponseEntity.ok("Account verified successfully");
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request)
    {
        return authService.login(request);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email)
    {
        return ResponseEntity.ok(authService.forgotPassword(email));
    }

    @PostMapping("/verify-forgot-otp")
    public ResponseEntity<String> verifyForgotPasswordOtp(@RequestBody VerifyOtpDTO dto)
    {
        return ResponseEntity.ok(authService.verifyForgotPasswordOtp(dto));
    }

    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO dto)
    {
        return ResponseEntity.ok(authService.resetPassword(dto));
    }

}
