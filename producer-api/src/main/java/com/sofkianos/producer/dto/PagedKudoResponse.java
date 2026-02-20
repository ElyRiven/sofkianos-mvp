package com.sofkianos.producer.dto;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Paginated response wrapper for Kudo list items.
 */
public record PagedKudoResponse(
        List<KudoListItemDTO> content,
        long totalElements,
        int totalPages,
        int number,
        int size
) {

    /**
     * Creates a {@link PagedKudoResponse} from a Spring Data {@link Page}.
     *
     * @param page the page result from the repository
     * @return a paginated response DTO
     */
    public static PagedKudoResponse from(Page<KudoListItemDTO> page) {
        return new PagedKudoResponse(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }
}
