package com.clf.service;

import com.clf.api.CatService;
import com.clf.dao.CatDao;
import com.clf.dao.OwnerDao;
import com.clf.dto.CatDto;
import com.clf.filterChain.model.CatFilter;
import com.clf.model.Cat;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CatServiceImpl implements CatService {
    private static final Logger logger = LoggerFactory.getLogger(CatServiceImpl.class);

    private final CatDao catDao;
    private final OwnerDao ownerDao;

    private final FilterServiceImpl<Cat, CatFilter> filterService;

    @Autowired
    public CatServiceImpl(CatDao catDao, OwnerDao ownerDao, FilterServiceImpl<Cat, CatFilter> filterService) {
        this.catDao = catDao;
        this.ownerDao = ownerDao;
        this.filterService = filterService;
    }

    @Override
    public void addCat(CatDto catDto) {
        var cat = dtoToCat(catDto);
        catDao.save(cat);
    }

    @Override
    public CatDto getCatById(Long id) {
        var cat = catDao.findById(id);
        return cat.map(CatDto::new).orElseGet(() -> {
            logger.warn("Cat with ID {} not found", id);
            return null;
        });
    }

    @Override
    public List<CatDto> getAllCats(CatFilter filter) {
        List<Cat> cats = filterService.applyFilters(filter, Cat.class);

        return cats.stream().map(CatDto::new).toList();
    }

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
        catDao.deleteById(id);
    }

    @Override
    public void updateCat(CatDto catDto) {
        var cat = dtoToCat(catDto);
        catDao.save(cat);
    }

    @Override
    public void makeFriends(Long id1, Long id2) {
        Optional<Cat> optionalCat1 = catDao.findById(id1);
        Optional<Cat> optionalCat2 = catDao.findById(id2);

        boolean catsPresent = true;

        if (optionalCat1.isEmpty()) {
            catsPresent = false;
            logger.warn("Cat with ID {} not found", id1);
        }
        if (optionalCat2.isEmpty()) {
            catsPresent = false;
            logger.warn("Cat with ID {} not found", id2);
        }
        if (!catsPresent) {
            return;
        }

        var cat1 = optionalCat1.get();
        var cat2 = optionalCat2.get();
        if (!cat1.getFriends().contains(cat2)) {
            cat1.getFriends().add(cat2);
            catDao.save(cat1);
        }
        if (!cat2.getFriends().contains(cat1)) {
            cat2.getFriends().add(cat1);
            catDao.save(cat2);
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
            ownerDao.findById(catDto.getOwnerId())
                    .ifPresentOrElse(cat::setOwner,
                            () -> logger.warn("Owner not found for Cat ID: {}", catDto.getId()));
        }

        if (catDto.getFriendIds() != null) {
            List<Cat> friends = catDto.getFriendIds().stream()
                    .map(id -> {
                        Optional<Cat> friend = catDao.findById(id);
                        if (friend.isEmpty()) {
                            logger.warn("Friend with ID {} not found for Cat ID: {}", id, catDto.getId());
                        }
                        return friend;
                    })
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            cat.setFriends(friends);
        }

        return cat;
    }
}