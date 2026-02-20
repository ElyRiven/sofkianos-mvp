package com.sofkianos.producer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO representing a single Kudo item for public display.
 * Contains only safe, non-sensitive fields.
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KudoListItemDTO {

    private String receptor;
    private String emisor;
    private String mensaje;
    private LocalDateTime fecha;
    private String categoria;
}
