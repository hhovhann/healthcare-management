package am.hhovhann.healthcare_management.controller;

import am.hhovhann.healthcare_management.dto.AuthRequest;
import am.hhovhann.healthcare_management.dto.AuthResponse;
import am.hhovhann.healthcare_management.entity.User;
import am.hhovhann.healthcare_management.enums.Role;
import am.hhovhann.healthcare_management.repository.UserRepository;
import am.hhovhann.healthcare_management.service.UserService;
import am.hhovhann.healthcare_management.utils.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Marks this class as a REST Controller
@RequestMapping("/api/v1/auth") // Base path for all endpoints in this controller
public class AuthController {
    private final AuthenticationManager
            authenticationManager; // Spring Security's AuthenticationManager
    private final UserService userService; // Our custom UserDetailsService
    private final JwtUtil jwtUtil; // Utility for JWT operations
    private final PasswordEncoder passwordEncoder; // Spring Security's password encoder
    private final UserRepository userRepository; // To save new users

    public AuthController(
            AuthenticationManager authenticationManager,
            UserService userService,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody AuthRequest authenticationRequest) throws Exception {
        try {
            // Attempt to authenticate the user using provided username and password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.username(), authenticationRequest.password()));
        } catch (BadCredentialsException e) {
            // If authentication fails, throw an exception
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Incorrect username or password");
        }

        // If authentication is successful, load user details and generate a JWT
        final UserDetails userDetails =
                userService.loadUserByUsername(authenticationRequest.username());
        final String jwt = jwtUtil.generateToken(userDetails);

        // Return the JWT in the response
        return ResponseEntity.ok(
                new AuthResponse(jwt, userDetails.getUsername(), "Login successful"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest registrationRequest) {
        // Check if user already exists
        if (userRepository.findByUsername(registrationRequest.username()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        // Create a new user
        User newUser = new User();
        newUser.setUsername(registrationRequest.username());
        // Encode the password before saving
        newUser.setPassword(passwordEncoder.encode(registrationRequest.password()));
        // Assign a default role. In a real system, roles might be assigned differently.
        newUser.setRole(Role.ROLE_USER); // Default role for new registrations

        userRepository.save(newUser); // Save the new user to the database

        // Optionally, immediately log in the user and return a token
        final UserDetails userDetails = userService.loadUserByUsername(newUser.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse(jwt, newUser.getUsername(), "User registered successfully"));
    }
}
