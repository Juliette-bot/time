package org.echangedetemps.discussion;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.echangedetemps.listing.Listing;
import org.echangedetemps.user.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "discussion")
public class Discussion extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    /** One discussion per listing */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id", unique = true)
    public Listing listing;

    /** Join table discussion_participant (was UserDiscussion in Django) */
    @ManyToMany
    @JoinTable(
            name = "discussion_participant",
            joinColumns = @JoinColumn(name = "discussion_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    public Set<User> participants = new HashSet<>();

    @OneToMany(mappedBy = "discussion")
    @OrderBy("sentAt ASC")
    public List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "discussion")
    public List<Proposal> proposals = new ArrayList<>();
}
