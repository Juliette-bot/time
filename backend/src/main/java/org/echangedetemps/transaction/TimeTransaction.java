package org.echangedetemps.transaction;

import jakarta.persistence.*;
import org.echangedetemps.discussion.Proposal;
import org.echangedetemps.user.User;

import java.time.Instant;

/** Time credited to one user and debited from another, once an exchange is done. */
@Entity
@Table(name = "time_transaction")
public class TimeTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proposal_id", nullable = false, unique = true)
    private Proposal proposal;

    @Column(nullable = false)
    private TransactionStatus status;

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    /** Receives the time (the one who gave the service) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creditor_id", nullable = false)
    private User creditor;

    /** Gives the time (the one who received the service) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "debtor_id", nullable = false)
    private User debtor;

    @Column(nullable = false)
    private boolean completed = false;

    @Column(name = "completed_at")
    private Instant completedAt;

    /** Sets both fields together, as required by the database CHECK constraint. */
    public void complete() {
        this.completed = true;
        this.completedAt = Instant.now();
    }

    // ----- Getters / setters -----

    public Long getId() {
        return id;
    }

    public Proposal getProposal() {
        return proposal;
    }

    public void setProposal(Proposal proposal) {
        this.proposal = proposal;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public User getCreditor() {
        return creditor;
    }

    public void setCreditor(User creditor) {
        this.creditor = creditor;
    }

    public User getDebtor() {
        return debtor;
    }

    public void setDebtor(User debtor) {
        this.debtor = debtor;
    }

    public boolean isCompleted() {
        return completed;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }
}
