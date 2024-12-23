package com.clf.controller;

import com.clf.dto.CatDto;
import com.clf.api.CatService;
import com.clf.service.filterChain.model.CatFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/cats")
@Validated
public class CatController {

    private final CatService catService;

    @Autowired
    public CatController(CatService catService) {
        this.catService = catService;
    }

    @PostMapping()
    public ResponseEntity<Void> addCat(@RequestBody CatDto catDto) {
        catService.addCat(catDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatDto> getCatById(@PathVariable Long id) {
        CatDto cat = catService.getCatById(id);
        return cat == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(cat);
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN', 'USER', 'ADMIN')")
    public ResponseEntity<List<CatDto>> getAllCats(
            @RequestParam(required = false, name = "name") String name,
            @RequestParam(required = false, name = "color") String color,
            @RequestParam(required = false, name = "breed") String breed,
            @RequestParam(required = false, name = "age") Long age,
            @RequestParam(required = false, name = "ownerId") Long ownerId,
            @RequestParam(required = false, name = "birthday") Date birthday
    ) {
        var filter = new CatFilter(name, color, breed, age, ownerId, birthday);
        return ResponseEntity.ok(catService.getAllCats(filter));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeCat(@PathVariable(name = "id") Long id) {
        catService.removeCat(id);
        return ResponseEntity.ok("Removed Cat " + id);
    }

    @PutMapping("/makeFriends")
    public ResponseEntity<String> makeFriends(@RequestParam Long cat1, @RequestParam Long cat2) {
        catService.makeFriends(cat1, cat2);
        return ResponseEntity.ok("Made friends " + cat1 + " and " + cat2);
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateCat(@RequestBody CatDto catDto) {
        catService.updateCat(catDto);
        return ResponseEntity.ok().build();
    }
}
