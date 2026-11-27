package org.echangedetemps.discussion;

import jakarta.persistence.*;
import org.echangedetemps.listing.Listing;
import org.echangedetemps.user.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "discussion")
public class Discussion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** One discussion per listing */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id", unique = true)
    private Listing listing;

    /** Join table discussion_participant (was UserDiscussion in Django) */
    @ManyToMany
    @JoinTable(
            name = "discussion_participant",
            joinColumns = @JoinColumn(name = "discussion_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> participants = new HashSet<>();

    @OneToMany(mappedBy = "discussion")
    @OrderBy("sentAt ASC")
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "discussion")
    private List<Proposal> proposals = new ArrayList<>();

    // ----- Getters / setters -----

    public Long getId() {
        return id;
    }

    public Listing getListing() {
        return listing;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<Proposal> getProposals() {
        return proposals;
    }
}
