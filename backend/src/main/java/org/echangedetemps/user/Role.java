package org.echangedetemps.user;

import jakarta.persistence.Converter;
import org.echangedetemps.common.LowercaseEnumConverter;

public enum Role {
    USER,
    ADMIN;

    @Converter(autoApply = true)
    public static class DbConverter extends LowercaseEnumConverter<Role> {
        public DbConverter() {
            super(Role.class);
        }
    }
}
