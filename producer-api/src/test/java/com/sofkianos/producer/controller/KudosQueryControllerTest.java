package com.sofkianos.producer.controller;

import com.sofkianos.producer.dto.KudoListItemDTO;
import com.sofkianos.producer.dto.PagedKudoResponse;
import com.sofkianos.producer.service.KudoQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KudosQueryController.class)
class KudosQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KudoQueryService kudoQueryService;

    @Nested
    @DisplayName("AC-01: Public endpoint accessibility")
    class PublicAccess {

        @Test
        @DisplayName("GET /api/v1/kudos should return 200 OK without authentication")
        void getKudos_NoAuth_Returns200() throws Exception {
            var emptyPage = new PageImpl<KudoListItemDTO>(
                    Collections.emptyList(),
                    PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt")),
                    0
            );
            when(kudoQueryService.searchKudos(any())).thenReturn(emptyPage);

            mockMvc.perform(get("/api/v1/kudos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.totalElements").value(0));
        }
    }

    @Nested
    @DisplayName("AC-02: Response contains safe fields only")
    class ResponseStructure {

        @Test
        @DisplayName("Should return receptor, mensaje, fecha, emisor without emails or IDs")
        void getKudos_ResponseStructure_NoSensitiveData() throws Exception {
            var kudo = KudoListItemDTO.builder()
                    .receptor("María García")
                    .emisor("Juan Pérez")
                    .mensaje("Excelente trabajo en equipo")
                    .fecha(LocalDateTime.of(2026, 2, 20, 10, 30, 0))
                    .categoria("Teamwork")
                    .build();

            var page = new PageImpl<>(List.of(kudo),
                    PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt")), 1);
            when(kudoQueryService.searchKudos(any())).thenReturn(page);

            mockMvc.perform(get("/api/v1/kudos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].receptor").value("María García"))
                    .andExpect(jsonPath("$.content[0].emisor").value("Juan Pérez"))
                    .andExpect(jsonPath("$.content[0].mensaje").value("Excelente trabajo en equipo"))
                    .andExpect(jsonPath("$.content[0].fecha").exists())
                    .andExpect(jsonPath("$.content[0].categoria").value("Teamwork"))
                    .andExpect(jsonPath("$.content[0].id").doesNotExist())
                    .andExpect(jsonPath("$.content[0].email").doesNotExist())
                    .andExpect(jsonPath("$.content[0].fromUser").doesNotExist())
                    .andExpect(jsonPath("$.content[0].toUser").doesNotExist());
        }
    }

    @Nested
    @DisplayName("AC-03: Default ordering DESC by date")
    class Ordering {

        @Test
        @DisplayName("Kudos should be ordered DESC by fecha by default")
        void getKudos_DefaultOrder_DescByDate() throws Exception {
            var kudo1 = KudoListItemDTO.builder()
                    .receptor("User A").emisor("User C")
                    .mensaje("Older kudo").categoria("Innovation")
                    .fecha(LocalDateTime.of(2026, 2, 18, 9, 0, 0))
                    .build();
            var kudo2 = KudoListItemDTO.builder()
                    .receptor("User B").emisor("User D")
                    .mensaje("Newer kudo").categoria("Teamwork")
                    .fecha(LocalDateTime.of(2026, 2, 20, 15, 0, 0))
                    .build();

            var page = new PageImpl<>(List.of(kudo2, kudo1),
                    PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt")), 2);
            when(kudoQueryService.searchKudos(any())).thenReturn(page);

            mockMvc.perform(get("/api/v1/kudos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].mensaje").value("Newer kudo"))
                    .andExpect(jsonPath("$.content[1].mensaje").value("Older kudo"));
        }
    }

    @Nested
    @DisplayName("AC-04: Pagination support")
    class Pagination {

        @Test
        @DisplayName("Should accept page and size query params")
        void getKudos_WithPagination_ReturnsCorrectPage() throws Exception {
            var page = new PageImpl<KudoListItemDTO>(
                    Collections.emptyList(),
                    PageRequest.of(2, 10, Sort.by(Sort.Direction.DESC, "createdAt")),
                    50
            );
            when(kudoQueryService.searchKudos(any())).thenReturn(page);

            mockMvc.perform(get("/api/v1/kudos")
                            .param("page", "2")
                            .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalElements").value(50))
                    .andExpect(jsonPath("$.totalPages").value(5))
                    .andExpect(jsonPath("$.number").value(2))
                    .andExpect(jsonPath("$.size").value(10));
        }

        @Test
        @DisplayName("Should enforce max page size of 50")
        void getKudos_ExceedsMaxSize_CapsAt50() throws Exception {
            when(kudoQueryService.searchKudos(any())).thenReturn(
                    new PageImpl<>(Collections.emptyList()));
            mockMvc.perform(get("/api/v1/kudos")
                            .param("size", "200"))
                    .andExpect(status().isOk());
        }
    }
}
