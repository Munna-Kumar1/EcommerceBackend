package com.prasadfencing.service;

import com.prasadfencing.dto.*;
import com.prasadfencing.entity.User;
import com.prasadfencing.repository.UserRepository;
import com.prasadfencing.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final UserRepository repository;
    private final EmailService emailService;

    public void signup(SignupRequest request) {

        if(repository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists");
        }

        String otp = generateOtp();

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );
        user.setRole("ROLE_USER");
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());

        user.setVerified(false);

        repository.save(user);

        try {

            emailService.sendEmail(
                    request.getEmail(),
                    "Email Verification OTP",
                    "Hello " + request.getName() + ",\n\n" +

                            "Thank you for registering with Prasad Fencing.\n\n" +

                            "Your OTP for email verification is: " + otp + "\n\n" +

                            "Please enter this OTP to verify your account.\n" +
                            "This OTP is valid for 10 minutes.\n\n" +

                            "If you did not request this registration, please ignore this email.\n\n" +

                            "Regards,\n" +
                            "Prasad Fencing Team"
            );

            System.out.println("Email sent successfully");

        } catch (Exception e) {

            System.out.println("Email sending failed");

            e.printStackTrace();
        }
    }

    private void validateOtp(User user, String otp)
    {
        if(!user.getOtp().equals(otp)){
            throw  new RuntimeException("Invalid OTP");
        }

        if(user.getOtpGeneratedTime()
                .plusMinutes(5)
                .isBefore(LocalDateTime.now())){
            throw new RuntimeException("OTP Expired");
        }
    }

    public void verifyOtp(VerifyOtpDTO dto){

        User user = repository.findByEmail(dto.getEmail())
                .orElseThrow(()->
                        new RuntimeException("User not found"));

        validateOtp(user, dto.getOtp());

        user.setVerified(true);
        user.setOtp(null);
        repository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = repository.findByEmail(request.getEmail())
                        .orElseThrow(()->
                                new RuntimeException("User not found"));

        if (!user.isVerified()){
            throw new RuntimeException("Please verify your email first");
        }

        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        String token =
                jwtService.generateToken(
                        request.getEmail()
                );
        return new AuthResponse(token);
    }

    public String forgotPassword(String email)
    {
        User user = repository.findByEmail(email)
                .orElseThrow(()->
                        new RuntimeException("Email not found"));

        String otp = generateOtp();

        user.setOtp(otp);

        user.setOtpGeneratedTime(LocalDateTime.now());

        repository.save(user);
        emailService.sendEmail(
                user.getEmail(),
                "Password Reset OTP",

                "Hello " + user.getName() + ",\n\n" +

                        "Your OTP for password reset is: " + otp + "\n\n" +

                        "This OTP is valid for 5 minutes.\n\n" +

                        "Regards,\n" +
                        "Prasad Fencing Team"
        );

        return "OTP sent successfully";
    }

    public String verifyForgotPasswordOtp(VerifyOtpDTO dto){

        User user = repository.findByEmail(dto.getEmail())
                .orElseThrow(()->
                        new RuntimeException("User not found"));

        validateOtp(user, dto.getOtp());
        return "OTP Verified";
    }

    public String resetPassword(ResetPasswordDTO dto)
    {
        User user = repository.findByEmail(dto.getEmail())
                .orElseThrow(()->
                        new RuntimeException("User not found"));

        user.setPassword(
                passwordEncoder.encode(dto.getNewPassword())
        );

        user.setOtp(null);
        user.setOtpGeneratedTime(null);
        repository.save(user);
        return "Password reset successful";
    }

    private String generateOtp(){
        return String.valueOf((int)((Math.random()*900000)+100000));
    }
}
