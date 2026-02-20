package com.sofkianos.producer.service.impl;

import com.sofkianos.producer.dto.KudoListItemDTO;
import com.sofkianos.producer.dto.KudoSearchCriteria;
import com.sofkianos.producer.repository.KudoQueryRepository;
import com.sofkianos.producer.service.KudoQueryService;
import com.sofkianos.producer.specification.KudoSpecifications;
import com.sofkianos.producer.util.EmailMaskingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link KudoQueryService} that retrieves kudos for public display.
 *
 * <p>Maps entity fields to safe DTOs using {@link EmailMaskingUtil} to prevent
 * exposure of sensitive information such as email addresses.</p>
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KudoQueryServiceImpl implements KudoQueryService {

    private final KudoQueryRepository kudoQueryRepository;

    @Override
    @Cacheable("kudosList")
    public Page<KudoListItemDTO> searchKudos(KudoSearchCriteria criteria) {
        Sort.Direction direction = "ASC".equalsIgnoreCase(criteria.getSortDirection())
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        PageRequest pageable = PageRequest.of(
                criteria.getPage(),
                criteria.getEffectiveSize(),
                Sort.by(direction, "createdAt")
        );

        return kudoQueryRepository
                .findAll(KudoSpecifications.fromCriteria(criteria), pageable)
                .map(kudo -> KudoListItemDTO.builder()
                        .receptor(EmailMaskingUtil.toDisplayName(kudo.getToUser()))
                        .emisor(EmailMaskingUtil.toDisplayName(kudo.getFromUser()))
                        .mensaje(kudo.getMessage())
                        .fecha(kudo.getCreatedAt())
                        .categoria(kudo.getCategory() != null ? kudo.getCategory().name() : null)
                        .build());
    }
}
