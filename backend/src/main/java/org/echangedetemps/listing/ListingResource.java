package org.echangedetemps.listing;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.echangedetemps.listing.dto.CreateListingRequest;
import org.echangedetemps.listing.dto.ListingResponse;

import java.net.URI;
import java.util.List;

/** REST API for listings (the "controller" layer). */
@Path("/api/listings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
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

    /**
     * POST /api/listings : creates a listing.
     * 201 Created + "Location" header pointing to the new listing,
     * or 400 Bad Request if the data is invalid (@Valid).
     */
    @POST
    public Response create(@Valid @NotNull CreateListingRequest request) {
        ListingResponse created = listingService.create(request);
        return Response.created(URI.create("/api/listings/" + created.id()))
                .entity(created)
                .build();
    }
}
