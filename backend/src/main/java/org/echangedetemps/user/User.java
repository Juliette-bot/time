package org.echangedetemps.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Optional;

@Entity
@Table(name = "users")
public class User extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true)
    public String email;

    @JsonIgnore // never send the hash to the front-end
    @Column(name = "password_hash", nullable = false)
    public String passwordHash;

    @Column(name = "first_name")
    public String firstName;

    @Column(name = "last_name")
    public String lastName;

    @Column(nullable = false)
    public Role role = Role.USER;

    @Column(name = "postal_code", length = 5)
    public String postalCode;

    @Column(nullable = false)
    public boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    public Instant createdAt;

    public static Optional<User> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }
}
