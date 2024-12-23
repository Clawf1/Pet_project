package com.clf.controller;

import com.clf.dto.OwnerDto;
import com.clf.api.OwnerService;
import com.clf.filterChain.model.OwnerFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/owners")
public class OwnerController {

    private final OwnerService ownerService;

    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addOwner(@RequestBody OwnerDto ownerDto) {
        ownerService.addOwner(ownerDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OwnerDto> getOwnerById(@PathVariable Long id) {
        OwnerDto owner = ownerService.getOwnerById(id);
        return owner == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(owner);
    }

    @GetMapping("/all")
    public ResponseEntity<List<OwnerDto>> getAllOwners(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Date birthday
            ) {
        var filter = new OwnerFilter(name, birthday);
        return ResponseEntity.ok(ownerService.getAllOwners(filter));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeOwner(@RequestParam(name = "id") Long id) {
        ownerService.removeOwnerById(id);
        return ResponseEntity.ok("Removed Owner " + id);
    }

    @PutMapping("/{ownerId}/cats/{catId}")
    public ResponseEntity<String> addCatToOwner(@PathVariable Long ownerId, @PathVariable Long catId) {
        ownerService.addCatToOwner(ownerId, catId);
        return ResponseEntity.ok("Added Cat " + catId + " to Owner " + ownerId);
    }

    @DeleteMapping("/{ownerId}/cats/{catId}")
    public ResponseEntity<String> removeCatFromOwner(@PathVariable Long ownerId, @PathVariable Long catId) {
        ownerService.removeCatFromOwner(ownerId, catId);
        return ResponseEntity.ok("Cat " + catId + " ran away from Owner " + ownerId);
    }
}
