package am.hhovhann.healthcare_management.entity;

import am.hhovhann.healthcare_management.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity // Marks this class as a JPA entity
@Table(name = "users") // Specifies the table name in the database
public class User implements UserDetails {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the ID
    private Long id;

    @Column(unique = true, nullable = false) // Ensures username is unique and not null
    private String username;

    @Column(nullable = false) // Ensures password is not null
    private String password;

    // In a real application, you might have roles like ADMIN, DOCTOR, PATIENT
    // For simplicity, we'll use a single 'ROLE_USER' here, or more specific roles for a healthcare
    // system
    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Returns a collection of GrantedAuthority for the user's role
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // For simplicity, accounts never expire
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // For simplicity, accounts are never locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // For simplicity, credentials never expire
    }

    @Override
    public boolean isEnabled() {
        return true; // For simplicity, accounts are always enabled
    }
}
