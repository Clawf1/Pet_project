package com.clf.controller;

import com.clf.dto.CatDto;
import com.clf.api.CatService;
import com.clf.filterChain.model.CatFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/cats")
public class CatController {

    private final CatService catService;

    @Autowired
    public CatController(CatService catService) {
        this.catService = catService;
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addCat(@RequestBody CatDto catDto) {
        catService.addCat(catDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CatDto> getCatById(@PathVariable Long id) {
        CatDto cat = catService.getCatById(id);
        return cat == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(cat);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CatDto>> getAllCats(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) Long age,
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) Date birthday
    ) {
        var filter = new CatFilter(name, color, breed, age, ownerId, birthday);
        return ResponseEntity.ok(catService.getAllCats(filter));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeCat(@RequestParam(name = "id") Long id) {
        catService.removeCat(id);
        return ResponseEntity.ok("Removed Cat " + id);
    }

    @PutMapping("/makeFriends")
    public ResponseEntity<String> makeFriends(@RequestParam Long cat1, @RequestParam Long cat2) {
        catService.makeFriends(cat1, cat2);
        return ResponseEntity.ok("Made friends " + cat1 + " and " + cat2);
    }

//    public void updateCat(CatDto catDto) {
//        catService.updateCat(catDto);
//    }
}
