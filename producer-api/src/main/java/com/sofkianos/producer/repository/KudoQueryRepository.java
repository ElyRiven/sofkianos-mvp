package com.sofkianos.producer.repository;

import com.sofkianos.producer.entity.Kudo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository for querying kudos with dynamic filtering and pagination.
 */
public interface KudoQueryRepository extends JpaRepository<Kudo, Long>, JpaSpecificationExecutor<Kudo> {
}
