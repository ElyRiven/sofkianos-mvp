package com.sofkianos.producer.controller;

import com.sofkianos.producer.dto.KudoSearchCriteria;
import com.sofkianos.producer.dto.PagedKudoResponse;
import com.sofkianos.producer.service.KudoQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for public Kudo queries.
 *
 * <p>Provides a read-only, unauthenticated endpoint to retrieve kudos
 * for public display. No sensitive data (emails, IDs) is exposed.</p>
 */
@RestController
@RequestMapping("/api/v1/kudos")
@RequiredArgsConstructor
public class KudosQueryController {

    private final KudoQueryService kudoQueryService;

    /**
     * Returns a paginated list of kudos ordered by date descending.
     *
     * @param page           zero-based page number (default 0)
     * @param size           page size, capped at 50 (default 20)
     * @param sortDirection  sort direction: ASC or DESC (default DESC)
     * @param category       optional category filter
     * @param searchText     optional free-text search
     * @return paginated kudos response
     */
    @GetMapping
    public ResponseEntity<PagedKudoResponse> getKudos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String searchText) {

        KudoSearchCriteria criteria = KudoSearchCriteria.builder()
                .page(page)
                .size(size)
                .sortDirection(sortDirection)
                .category(category)
                .searchText(searchText)
                .build();

        var result = kudoQueryService.searchKudos(criteria);
        return ResponseEntity.ok(PagedKudoResponse.from(result));
    }
}
