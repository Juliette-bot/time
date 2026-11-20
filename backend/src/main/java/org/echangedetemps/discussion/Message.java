package org.echangedetemps.discussion;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.echangedetemps.user.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "message")
public class Message extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "discussion_id", nullable = false)
    public Discussion discussion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    public User author;

    @Column(nullable = false)
    public String content;

    @CreationTimestamp
    @Column(name = "sent_at", nullable = false, updatable = false)
    public Instant sentAt;
}
