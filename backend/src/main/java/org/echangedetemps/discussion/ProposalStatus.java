package org.echangedetemps.discussion;

import jakarta.persistence.Converter;
import org.echangedetemps.common.LowercaseEnumConverter;

public enum ProposalStatus {
    PENDING,
    ACCEPTED,
    REJECTED;

    @Converter(autoApply = true)
    public static class DbConverter extends LowercaseEnumConverter<ProposalStatus> {
        public DbConverter() {
            super(ProposalStatus.class);
        }
    }
}
