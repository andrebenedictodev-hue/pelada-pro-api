package com.ab.peladapro.peladaproapi.domain.dao;

import com.ab.peladapro.peladaproapi.domain.model.RankingGlobal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RankingGlobalDAO extends JpaRepository<RankingGlobal, Long> {
    Optional<RankingGlobal> findFirstByUserId(String userId);
}
