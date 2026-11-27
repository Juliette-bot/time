package org.echangedetemps.listing;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.echangedetemps.listing.dto.CreateListingRequest;
import org.echangedetemps.listing.dto.ListingResponse;
import org.echangedetemps.listing.dto.UpdateListingRequest;
import org.echangedetemps.user.CurrentUser;
import org.echangedetemps.user.Role;
import org.echangedetemps.user.User;

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
        return ListingResponse.from(findOrThrow(id));
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

    /**
     * Modifies a listing.
     * Rules: only its author can do it (403), and not once it is CLOSED (409).
     * No persist() needed: the entity is "managed", Hibernate saves
     * the changes automatically at the end of the transaction.
     */
    @Transactional
    public ListingResponse update(Long id, UpdateListingRequest request) {
        Listing listing = findOrThrow(id);
        User user = currentUser.get();

        if (!isAuthor(listing, user)) {
            throw new ForbiddenException("Vous ne pouvez modifier que vos propres annonces");
        }
        if (listing.getStatus() == ListingStatus.CLOSED) {
            throw new ClientErrorException("Une annonce clôturée ne peut plus être modifiée",
                    Response.Status.CONFLICT);
        }

        listing.setTitle(request.title().strip());
        listing.setDescription(request.description().strip());
        listing.setType(request.type());
        listing.setDurationMinutes(request.durationMinutes());
        return ListingResponse.from(listing);
    }

    /**
     * Deletes a listing.
     * Rules: its author or an admin (403), and only if no exchange has started (409).
     * Why: the database deletes in cascade the discussions, proposals
     * and time transactions linked to a listing, so deleting it would
     * erase the time-credit history of other users.
     */
    @Transactional
    public void delete(Long id) {
        Listing listing = findOrThrow(id);
        User user = currentUser.get();

        if (!isAuthor(listing, user) && user.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Vous ne pouvez supprimer que vos propres annonces");
        }
        if (listing.getRequester() != null || listing.getStatus() == ListingStatus.CLOSED) {
            throw new ClientErrorException("Cette annonce fait l'objet d'un échange, elle ne peut pas être supprimée",
                    Response.Status.CONFLICT);
        }

        listingRepository.delete(listing);
    }

    // ----- Helpers -----

    private Listing findOrThrow(Long id) {
        return listingRepository.findByIdWithAuthor(id)
                .orElseThrow(() -> new NotFoundException("Annonce introuvable"));
    }

    private static boolean isAuthor(Listing listing, User user) {
        return listing.getAuthor().getId().equals(user.getId());
    }
}
