package am.hhovhann.healthcare_management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Marks this class as a REST Controller
@RequestMapping("/api/v1/healthcare") // Base path for healthcare-related endpoints
public class HealthcareController {

    @GetMapping("/dashboard")
    // @PreAuthorize can be used to restrict access based on roles.
    // For example, only users with 'ROLE_ADMIN' or 'ROLE_DOCTOR' can access this.
    // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR')")
    public ResponseEntity<String> getDashboardData() {
        // This endpoint requires a valid JWT to access.
        return ResponseEntity.ok("Welcome to the Healthcare Dashboard! This data is protected.");
    }

    @GetMapping("/patients")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DOCTOR')")
    public ResponseEntity<String> getPatientList() {
        // This endpoint requires users to have either ROLE_ADMIN or ROLE_DOCTOR.
        return ResponseEntity.ok("List of patient data (requires ADMIN or DOCTOR role).");
    }

    @GetMapping("/public")
    // This endpoint would be configured in SecurityConfiguration to permit all.
    // For demonstration, it's not explicitly restricted here, but usually, public endpoints
    // don't even reach the JWT filter for authentication checks.
    public ResponseEntity<String> getPublicData() {
        return ResponseEntity.ok("This is public data, accessible without authentication.");
    }
}