package org.echangedetemps.listing;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.echangedetemps.listing.dto.CreateListingRequest;
import org.echangedetemps.listing.dto.ListingResponse;
import org.echangedetemps.user.CurrentUser;

import java.util.List;

@ApplicationScoped
public class ListingService {

    private final ListingRepository listingRepository;
    private final CurrentUser currentUser;

    public ListingService(ListingRepository listingRepository, CurrentUser currentUser) {
        this.listingRepository = listingRepository;
        this.currentUser = currentUser;
    }

    public List<ListingResponse> listActive() {
        return listingRepository.findActive().stream()
                .map(ListingResponse::from)
                .toList();
    }

    public ListingResponse getById(Long id) {
        return listingRepository.findByIdWithAuthor(id)
                .map(ListingResponse::from)
                // Message shown to users: in French
                .orElseThrow(() -> new NotFoundException("Annonce introuvable"));
    }

    /**
     * Creates a listing for the current user.
     * Business rules decided here, never by the client:
     * - the author is the current user;
     * - a new listing always starts as PENDING (waiting for moderation).
     */
    @Transactional
    public ListingResponse create(CreateListingRequest request) {
        Listing listing = new Listing();
        listing.setTitle(request.title().strip());
        listing.setDescription(request.description().strip());
        listing.setType(request.type());
        listing.setDurationMinutes(request.durationMinutes());
        listing.setStatus(ListingStatus.PENDING);
        listing.setAuthor(currentUser.get());

        listingRepository.persist(listing);
        return ListingResponse.from(listing);
    }
}
