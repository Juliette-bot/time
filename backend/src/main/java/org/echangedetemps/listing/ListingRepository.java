package org.echangedetemps.listing;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ListingRepository implements PanacheRepository<Listing> {

    /**
     * Active listings, newest first.
     * "join fetch" loads the author in the same SQL query,
     * instead of one extra query per listing (the "N+1" problem).
     */
    public List<Listing> findActive() {
        return find("""
                select l from Listing l
                join fetch l.author
                where l.status = ?1
                order by l.createdAt desc
                """, ListingStatus.ACTIVE).list();
    }

    public Optional<Listing> findByIdWithAuthor(Long id) {
        return find("""
                select l from Listing l
                join fetch l.author
                where l.id = ?1
                """, id).firstResultOptional();
    }
}
