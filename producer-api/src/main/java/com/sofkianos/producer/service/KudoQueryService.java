package com.sofkianos.producer.service;

import com.sofkianos.producer.dto.KudoListItemDTO;
import com.sofkianos.producer.dto.KudoSearchCriteria;
import org.springframework.data.domain.Page;

/**
 * Service interface for querying Kudos for public display.
 */
public interface KudoQueryService {

    /**
     * Searches kudos matching the given criteria with pagination.
     *
     * @param criteria the search and pagination parameters
     * @return a page of {@link KudoListItemDTO} with safe, non-sensitive fields
     */
    Page<KudoListItemDTO> searchKudos(KudoSearchCriteria criteria);
}
