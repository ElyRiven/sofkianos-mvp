package com.sofkianos.producer.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Search criteria for querying kudos with pagination and filtering.
 */
@Getter
@Builder
public class KudoSearchCriteria {

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 20;

    @Builder.Default
    private String sortDirection = "DESC";

    private String category;
    private String searchText;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    /**
     * Returns the effective page size capped at 50.
     *
     * @return page size, max 50
     */
    public int getEffectiveSize() {
        return Math.min(size, 50);
    }
}
