package org.echangedetemps.listing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.echangedetemps.listing.ListingType;

/**
 * What the front-end sends to create a listing.
 * Only the fields a user is allowed to fill in: no id, no status, no author.
 * Validation messages are shown to users, so they are in French.
 */
public record CreateListingRequest(

        @NotBlank(message = "Le titre est obligatoire")
        @Size(max = 255, message = "Le titre ne doit pas dépasser 255 caractères")
        String title,

        @NotBlank(message = "La description est obligatoire")
        @Size(max = 5000, message = "La description ne doit pas dépasser 5000 caractères")
        String description,

        @NotNull(message = "Le type d'annonce est obligatoire (OFFER ou REQUEST)")
        ListingType type,

        // Optional: null means "duration not specified"
        @Positive(message = "La durée doit être supérieure à 0")
        Integer durationMinutes
) {
}
