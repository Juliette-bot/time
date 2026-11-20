package org.echangedetemps.transaction;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.echangedetemps.discussion.Proposal;
import org.echangedetemps.user.User;

import java.time.Instant;

/** Time credited to one user and debited from another, once an exchange is done. */
@Entity
@Table(name = "time_transaction")
public class TimeTransaction extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proposal_id", nullable = false, unique = true)
    public Proposal proposal;

    @Column(nullable = false)
    public TransactionStatus status;

    @Column(name = "duration_minutes", nullable = false)
    public int durationMinutes;

    /** Receives the time (the one who gave the service) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creditor_id", nullable = false)
    public User creditor;

    /** Gives the time (the one who received the service) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "debtor_id", nullable = false)
    public User debtor;

    @Column(nullable = false)
    public boolean completed = false;

    @Column(name = "completed_at")
    public Instant completedAt;

    /** Sets both fields together, as required by the database CHECK constraint. */
    public void complete() {
        this.completed = true;
        this.completedAt = Instant.now();
    }
}
