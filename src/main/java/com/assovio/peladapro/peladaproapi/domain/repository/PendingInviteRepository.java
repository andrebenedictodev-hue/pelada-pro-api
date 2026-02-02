package com.assovio.peladapro.peladaproapi.domain.repository;

import java.util.List;
import java.util.UUID;

public interface PendingInviteRepository {
    void addInvite(String email, UUID inviterId);
    List<UUID> getInviters(String email);
    void clearInviters(String email);
}
