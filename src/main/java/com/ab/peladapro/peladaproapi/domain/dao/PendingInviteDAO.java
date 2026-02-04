package com.ab.peladapro.peladaproapi.domain.dao;

import com.ab.peladapro.peladaproapi.domain.model.PendingInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PendingInviteDAO extends JpaRepository<PendingInvite, Long> {
    List<PendingInvite> findByEmail(String email);
    void deleteByEmail(String email);
}
