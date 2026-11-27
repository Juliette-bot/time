package org.echangedetemps.transaction;

import jakarta.persistence.Converter;
import org.echangedetemps.common.LowercaseEnumConverter;

public enum TransactionStatus {
    REJECTED,
    VALIDATED;

    @Converter(autoApply = true)
    public static class DbConverter extends LowercaseEnumConverter<TransactionStatus> {
        public DbConverter() {
            super(TransactionStatus.class);
        }
    }
}
