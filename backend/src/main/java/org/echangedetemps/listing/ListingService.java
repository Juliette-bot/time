package org.echangedetemps.listing;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import org.echangedetemps.listing.dto.ListingResponse;

import java.util.List;

@ApplicationScoped
public class ListingService {

    private final ListingRepository listingRepository;

    public ListingService(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
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
}
