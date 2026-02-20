package com.sofkianos.producer.service;

import com.sofkianos.producer.dto.KudoListItemDTO;
import com.sofkianos.producer.dto.KudoSearchCriteria;
import com.sofkianos.producer.entity.Kudo;
import com.sofkianos.producer.entity.KudoCategory;
import com.sofkianos.producer.repository.KudoQueryRepository;
import com.sofkianos.producer.service.impl.KudoQueryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KudoQueryServiceTest {

    @Mock
    private KudoQueryRepository kudoQueryRepository;

    private KudoQueryServiceImpl kudoQueryService;

    @BeforeEach
    void setUp() {
        kudoQueryService = new KudoQueryServiceImpl(kudoQueryRepository);
    }

    @Test
    @DisplayName("Should map Kudo entity to KudoListItemDTO without sensitive data")
    void searchKudos_MapsEntityToDTO_NoSensitiveData() {
        var entity = Kudo.builder()
                .id(42L)
                .fromUser("juan.perez@sofka.com")
                .toUser("maria.garcia@sofka.com")
                .category(KudoCategory.Teamwork)
                .message("Gran colaboración")
                .createdAt(LocalDateTime.of(2026, 2, 20, 10, 0))
                .build();

        var page = new PageImpl<>(List.of(entity));
        when(kudoQueryRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        var criteria = KudoSearchCriteria.builder().build();

        Page<KudoListItemDTO> result = kudoQueryService.searchKudos(criteria);

        assertThat(result.getContent()).hasSize(1);
        var dto = result.getContent().get(0);
        assertThat(dto.getReceptor()).doesNotContain("@");
        assertThat(dto.getEmisor()).doesNotContain("@");
        assertThat(dto.getMensaje()).isEqualTo("Gran colaboración");
        assertThat(dto.getCategoria()).isEqualTo("Teamwork");
        assertThat(dto.getFecha()).isNotNull();
    }

    @Test
    @DisplayName("Should handle null emisor (anonymous kudo) gracefully")
    void searchKudos_NullEmisor_ReturnsAnonymous() {
        var entity = Kudo.builder()
                .id(99L)
                .fromUser(null)
                .toUser("maria.garcia@sofka.com")
                .category(KudoCategory.Innovation)
                .message("Inspirador")
                .createdAt(LocalDateTime.now())
                .build();

        var page = new PageImpl<>(List.of(entity));
        when(kudoQueryRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        var criteria = KudoSearchCriteria.builder().build();

        Page<KudoListItemDTO> result = kudoQueryService.searchKudos(criteria);

        var dto = result.getContent().get(0);
        assertThat(dto.getEmisor()).isEqualTo("Anónimo");
    }

    @Test
    @DisplayName("Should default to page 0, size 20, DESC by createdAt")
    void searchKudos_DefaultCriteria_CorrectPagination() {
        when(kudoQueryRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        var criteria = KudoSearchCriteria.builder().build();
        kudoQueryService.searchKudos(criteria);
    }
}
