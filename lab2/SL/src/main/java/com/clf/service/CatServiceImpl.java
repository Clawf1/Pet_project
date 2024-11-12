package com.clf.service;

import com.clf.api.CatService;
import com.clf.dao.CatDao;
import com.clf.dao.OwnerDao;
import com.clf.dto.CatDto;
import com.clf.model.Cat;
import com.clf.model.Owner;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class CatServiceImpl implements CatService {

    private CatDao catDao;
    private OwnerDao ownerDao;

    @Override
    public void addCat(CatDto catDto) {
        var cat = dtoToCat(catDto);
        catDao.save(cat);
    }

    @Override
    public CatDto getCatById(Long id) {
        var cat = catDao.findById(id);
        return new CatDto(cat);
    }

    @Override
    public List<CatDto> getAllCats() {
        var cats = catDao.findAll();

        return cats.stream().map(CatDto::new).toList();
    }

    @Override
    public void removeCat(CatDto catDto) {
        var cat = dtoToCat(catDto);
        catDao.delete(cat);
    }

    @Override
    public void removeCat(Long id) {
        catDao.delete(id);
    }

    @Override
    public void updateCat(CatDto catDto) {
        var cat = dtoToCat(catDto);
        catDao.update(cat);
    }

    @Override
    public void makeFriends(Long id1, Long id2) {
        var cat1 = catDao.findById(id1);
        var cat2 = catDao.findById(id2);

        if (cat1 != null && cat2 != null) {
            if (!cat1.getFriends().contains(cat2)) {
                cat1.getFriends().add(cat2);
                catDao.update(cat1);
            }
            if (!cat2.getFriends().contains(cat1)) {
                cat2.getFriends().add(cat1);
                catDao.update(cat2);
            }
        }
    }

    private Cat dtoToCat(CatDto catDto) {
        Cat cat = new Cat();
        cat.setId(catDto.getId());
        cat.setName(catDto.getName());
        cat.setBreed(catDto.getBreed());
        cat.setColor(catDto.getColor());
        cat.setBirthDate(catDto.getBirthDate());

        if (catDto.getOwnerId() != null) {
            Owner owner = ownerDao.findById(catDto.getOwnerId());
            if (owner != null) {
                cat.setOwner(owner);
            }
        }

        if (catDto.getFriendIds() != null) {
            List<Cat> friends = catDto.getFriendIds().stream()
                    .map(catDao::findById).filter(Objects::nonNull).toList();
            cat.setFriends(friends);
        }

        return cat;
    }
}