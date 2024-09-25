package com.clf.api;

import com.clf.dto.OwnerDto;

import java.util.List;

public interface OwnerService {
    void addOwner(OwnerDto ownerDto);

    OwnerDto getOwnerById(Long id);

    List<OwnerDto> getAllOwners();

    void removeOwnerById(Long id);

    void addCatToOwner(Long ownerId, Long catId);

    void removeCatFromOwner(Long ownerId, Long catId);

    void updateOwner(OwnerDto ownerDto);
}
