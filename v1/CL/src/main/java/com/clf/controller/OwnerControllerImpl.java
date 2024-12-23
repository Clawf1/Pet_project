package com.clf.controller;

import com.clf.api.OwnerController;
import com.clf.dto.OwnerDto;
import com.clf.api.OwnerService;

import java.util.List;

public class OwnerControllerImpl implements OwnerController {

    private final OwnerService ownerService;

    public OwnerControllerImpl(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    public void addOwner(OwnerDto ownerDto) {
        ownerService.addOwner(ownerDto);
    }

    public OwnerDto getOwnerById(Long id) {
        return ownerService.getOwnerById(id);
    }

    public List<OwnerDto> getAllOwners() {
        return ownerService.getAllOwners();
    }

    public void removeOwner(Long id) {
        ownerService.removeOwnerById(id);
    }

    public void addCatToOwner(Long ownerId, Long catId) {
        ownerService.addCatToOwner(ownerId, catId);
    }

    public void removeCatFromOwner(Long ownerId, Long catId) {
        ownerService.removeCatFromOwner(ownerId, catId);
    }

    public void updateOwner(OwnerDto ownerDto) {
        ownerService.updateOwner(ownerDto);
    }
}
