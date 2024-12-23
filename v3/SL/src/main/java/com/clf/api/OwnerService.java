package com.clf.api;

import com.clf.dto.OwnerDto;
import com.clf.service.filterChain.model.OwnerFilter;

import java.util.List;

public interface OwnerService {
    void addOwner(OwnerDto ownerDto);

    OwnerDto getOwnerById(Long id);

    List<OwnerDto> getAllOwners(OwnerFilter filter);

    void removeOwnerById(Long id);

    void addCatToOwner(Long ownerId, Long catId);

    void removeCatFromOwner(Long ownerId, Long catId);

    void updateOwner(OwnerDto ownerDto);
}
