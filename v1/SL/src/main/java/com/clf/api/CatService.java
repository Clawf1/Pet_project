package com.clf.api;

import com.clf.dto.CatDto;

import java.util.List;

public interface CatService {
    void addCat(CatDto catDto);

    CatDto getCatById(Long id);

    List<CatDto> getAllCats();

    void removeCat(CatDto catDto);

    void removeCat(Long id);

    void updateCat(CatDto catDto);

    void makeFriends(Long id1, Long id2);
}
