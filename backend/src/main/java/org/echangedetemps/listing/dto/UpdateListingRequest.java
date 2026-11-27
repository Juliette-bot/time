package org.echangedetemps.listing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.echangedetemps.listing.ListingType;

/**
 * What the front-end sends to modify a listing (full replacement, PUT).
 * Same fields as CreateListingRequest today, but kept separate:
 * the two can evolve independently (e.g. a field editable only at creation).
 */
public record UpdateListingRequest(

        @NotBlank(message = "Le titre est obligatoire")
        @Size(max = 255, message = "Le titre ne doit pas dépasser 255 caractères")
        String title,

        @NotBlank(message = "La description est obligatoire")
        @Size(max = 5000, message = "La description ne doit pas dépasser 5000 caractères")
        String description,

        @NotNull(message = "Le type d'annonce est obligatoire (OFFER ou REQUEST)")
        ListingType type,

        @Positive(message = "La durée doit être supérieure à 0")
        Integer durationMinutes
) {
}
