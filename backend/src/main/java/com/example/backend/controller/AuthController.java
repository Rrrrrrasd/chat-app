package com.example.backend.controller;


import com.example.backend.dto.AuthRequestDto;
import com.example.backend.dto.AuthResponseDto;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;



import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody AuthRequestDto requestDto){
        Optional<User> existingUser = userRepository.findByUsername(requestDto.getUsername());
        if(existingUser.isPresent()){
            return ResponseEntity.badRequest().body(new AuthResponseDto("Username already exists", null));
        }

        User newUser = new User();
        newUser.setUsername(requestDto.getUsername());
        newUser.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        userRepository.save(newUser);

        return ResponseEntity.ok(new AuthResponseDto("User registered successfully", null));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto requestDto) {
        Optional<User> foundUser = userRepository.findByUsername(requestDto.getUsername());
        if (foundUser.isPresent() && passwordEncoder.matches(requestDto.getPassword(), foundUser.get().getPassword())) {
            String token = jwtUtil.generateToken(foundUser.get().getUsername());
            return ResponseEntity.ok(new AuthResponseDto("Login successful", token));
        }
        return ResponseEntity.status(401).body(new AuthResponseDto("Invalid credentials", null));
    }
}
