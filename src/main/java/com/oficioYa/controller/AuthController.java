package com.oficioya.controller;

import com.oficioya.dto.AuthRequest;
import com.oficioya.dto.AuthResponse;
import com.oficioya.dto.SwitchRoleRequest;
import com.oficioya.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import com.oficioya.persistence.entity.UsuarioEntity;
import com.oficioya.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired(required = false)
    private UsuarioRepository usuarioRepository;

    // OFY-50: Endpoint de Login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        // Authenticate the user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Fetch real user from DB to include their authentic role
        UsuarioEntity usuario = usuarioRepository != null ?
                usuarioRepository.findByCorreo(request.getEmail()).orElse(null) : null;

        String rawRole = (usuario != null && usuario.getRol() != null) ?
                usuario.getRol().name() : "CONTRATANTE";
        String roleAuthority = "ROLE_" + rawRole.replace("ROLE_", "");

        UserDetails userDetails = User.builder()
                .username(request.getEmail())
                .password("")
                .authorities(roleAuthority)
                .build();

        // Add custom claim for role
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", roleAuthority);

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
