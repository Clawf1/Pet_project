package com.clf.api;

import com.clf.dto.CatDto;
import com.clf.service.filterChain.model.CatFilter;

import java.util.List;

public interface CatService {
    void addCat(CatDto catDto);

    CatDto getCatById(Long id);

    List<CatDto> getAllCats(CatFilter filter);

    List<CatDto> getAllCats();

    void removeCat(CatDto catDto);

    void removeCat(Long id);

    void updateCat(CatDto catDto);

    void makeFriends(Long id1, Long id2);
}
