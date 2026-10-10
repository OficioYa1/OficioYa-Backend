package com.oficioYa.controller;

import com.oficioYa.dto.AuthRequest;
import com.oficioYa.dto.AuthResponse;
import com.oficioYa.dto.SwitchRoleRequest;
import com.oficioYa.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // OFY-50: Endpoint de Login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        // Authenticate the user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Normally we fetch user from DB here. Mocking for now:
        UserDetails userDetails = User.builder()
                .username(request.getEmail())
                .password("")
                .roles("CONTRATANTE") // Default mock role
                .build();

        // Add custom claim for role
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ROLE_CONTRATANTE");

        String jwtToken = jwtService.generateToken(extraClaims, userDetails);
        return ResponseEntity.ok(AuthResponse.builder().token(jwtToken).build());
    }

    // OFY-55: Alternar rol de usuario (Switch Role)
    // Needs valid token (Authentication) to switch role, so we check for Principal
    @PostMapping("/switch-role")
    public ResponseEntity<AuthResponse> switchRole(
            @RequestBody SwitchRoleRequest request, 
            @RequestHeader("Authorization") String authHeader) {
        
        // This is a simplified version. Normally you validate the DB and generate a new token
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        UserDetails userDetails = User.builder()
                .username(username)
                .password("")
                .roles(request.getNewRole().replace("ROLE_", "")) // The new requested role
                .build();

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ROLE_" + request.getNewRole().replace("ROLE_", ""));

        String newToken = jwtService.generateToken(extraClaims, userDetails);
        return ResponseEntity.ok(AuthResponse.builder().token(newToken).build());
    }
}
