package com.clf.api;

import com.clf.dto.CatDto;

import java.util.List;

public interface CatController {
    void addCat(CatDto catDto);
    void removeCat(Long id);
    List<CatDto> getAllCats();
    void updateCat(CatDto catDto);
    CatDto getCatById(Long id);
    void makeFriends(Long catId1, Long catId2);
}
