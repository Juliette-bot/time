package org.echangedetemps.listing.dto;

import org.echangedetemps.listing.Listing;
import org.echangedetemps.listing.ListingStatus;
import org.echangedetemps.listing.ListingType;
import org.echangedetemps.user.User;

import java.time.Instant;

/**
 * What the front-end receives for a listing.
 * Only public information: no email, no password hash, no postal code of the author.
 */
public record ListingResponse(
        Long id,
        String title,
        String description,
        ListingType type,
        ListingStatus status,
        Integer durationMinutes,
        AuthorSummary author,
        Instant createdAt
) {

    public record AuthorSummary(Long id, String firstName) {

        static AuthorSummary from(User user) {
            return new AuthorSummary(user.getId(), user.getFirstName());
        }
    }

    public static ListingResponse from(Listing listing) {
        return new ListingResponse(
                listing.getId(),
                listing.getTitle(),
                listing.getDescription(),
                listing.getType(),
                listing.getStatus(),
                listing.getDurationMinutes(),
                AuthorSummary.from(listing.getAuthor()),
                listing.getCreatedAt()
        );
    }
}
