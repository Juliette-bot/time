package org.echangedetemps.common;

import jakarta.persistence.AttributeConverter;

import java.util.Locale;

/**
 * Stores a Java enum as its lowercase name in the database.
 * Example: ListingStatus.PENDING  <->  'pending'
 * This matches the CHECK constraints written in the Flyway migrations.
 */
public abstract class LowercaseEnumConverter<E extends Enum<E>> implements AttributeConverter<E, String> {

    private final Class<E> type;

    protected LowercaseEnumConverter(Class<E> type) {
        this.type = type;
    }

    @Override
    public String convertToDatabaseColumn(E value) {
        return value == null ? null : value.name().toLowerCase(Locale.ROOT);
    }

    @Override
    public E convertToEntityAttribute(String dbValue) {
        return dbValue == null ? null : Enum.valueOf(type, dbValue.toUpperCase(Locale.ROOT));
    }
}
