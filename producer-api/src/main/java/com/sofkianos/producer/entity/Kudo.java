package com.sofkianos.producer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * JPA entity representing a persisted Kudo, mapped to the existing {@code kudos} table.
 */
@Entity
@Table(name = "kudos")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Kudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromUser;
    private String toUser;

    @Enumerated(EnumType.STRING)
    private KudoCategory category;

    private String message;
    private LocalDateTime createdAt;
}
