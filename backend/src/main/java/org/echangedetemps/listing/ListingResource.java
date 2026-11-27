package org.echangedetemps.listing;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.echangedetemps.listing.dto.ListingResponse;

import java.util.List;

/** REST API for listings (the "controller" layer). */
@Path("/api/listings")
public class ListingResource {

    private final ListingService listingService;

    public ListingResource(ListingService listingService) {
        this.listingService = listingService;
    }

    /** GET /api/listings : all active listings, newest first */
    @GET
    public List<ListingResponse> list() {
        return listingService.listActive();
    }

    /** GET /api/listings/42 : one listing, or 404 */
    @GET
    @Path("/{id}")
    public ListingResponse get(@PathParam("id") Long id) {
        return listingService.getById(id);
    }
}
