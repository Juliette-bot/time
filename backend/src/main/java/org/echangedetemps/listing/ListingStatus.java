package org.echangedetemps.listing;

import jakarta.persistence.Converter;
import org.echangedetemps.common.LowercaseEnumConverter;

public enum ListingStatus {
    PENDING,
    ACTIVE,
    CLOSED;

    @Converter(autoApply = true)
    public static class DbConverter extends LowercaseEnumConverter<ListingStatus> {
        public DbConverter() {
            super(ListingStatus.class);
        }
    }
}
