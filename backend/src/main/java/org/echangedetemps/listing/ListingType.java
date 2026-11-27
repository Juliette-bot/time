package org.echangedetemps.listing;

import jakarta.persistence.Converter;
import org.echangedetemps.common.LowercaseEnumConverter;

/** OFFER: "I offer my time"  /  REQUEST: "I need someone's time" */
public enum ListingType {
    OFFER,
    REQUEST;

    @Converter(autoApply = true)
    public static class DbConverter extends LowercaseEnumConverter<ListingType> {
        public DbConverter() {
            super(ListingType.class);
        }
    }
}
