package org.echangedetemps.listing;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.echangedetemps.user.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "listing")
public class Listing extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String title;

    @Column(nullable = false)
    public String description;

    @Column(nullable = false)
    public ListingType type = ListingType.OFFER;

    @Column(nullable = false)
    public ListingStatus status = ListingStatus.PENDING;

    @Column(name = "duration_minutes")
    public Integer durationMinutes;

    /** Who published the listing */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    public User author;

    /** Who answered it (null while nobody has) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    public User requester;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    public Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    public Instant updatedAt;
}
