package com.clf.controller;

import com.clf.api.CatController;
import com.clf.dto.CatDto;
import com.clf.api.CatService;

import java.util.List;

public class CatControllerImpl implements CatController {

    private final CatService catService;

    public CatControllerImpl(CatService catService) {
        this.catService = catService;
    }

    @Override
    public void addCat(CatDto catDto) {
        catService.addCat(catDto);
    }

    @Override
    public CatDto getCatById(Long id) {
        return catService.getCatById(id);
    }

    @Override
    public List<CatDto> getAllCats() {
        return catService.getAllCats();
    }

    @Override
    public void removeCat(Long id) {
        catService.removeCat(id);
    }

    @Override
    public void makeFriends(Long catId1, Long catId2) {
        catService.makeFriends(catId1, catId2);
    }

    @Override
    public void updateCat(CatDto catDto) {
        catService.updateCat(catDto);
    }
}
