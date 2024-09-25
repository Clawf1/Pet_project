package com.clf.service;

import com.clf.api.OwnerService;
import com.clf.dao.CatDao;
import com.clf.dao.OwnerDao;
import com.clf.dto.OwnerDto;
import com.clf.filterChain.model.OwnerFilter;
import com.clf.model.Cat;
import com.clf.model.Owner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerServiceImpl implements OwnerService {
    private static final Logger logger = LoggerFactory.getLogger(OwnerServiceImpl.class);

    private final OwnerDao ownerDao;
    private final CatDao catDao;

    private final FilterServiceImpl<Owner, OwnerFilter> filterService;

    @Autowired
    public OwnerServiceImpl(OwnerDao ownerDao, CatDao catDao, FilterServiceImpl<Owner, OwnerFilter> filterService) {
        this.ownerDao = ownerDao;
        this.catDao = catDao;
        this.filterService = filterService;
    }

    @Override
    public void addOwner(OwnerDto ownerDto) {
        Owner owner = dtoToOwner(ownerDto);
        ownerDao.save(owner);
    }

    @Override
    public OwnerDto getOwnerById(Long id) {
        var owner = ownerDao.findById(id);
        return owner.map(OwnerDto::new).orElseGet(() -> {
            logger.warn("Owner with ID {} not found", id);
            return null;
        });
    }

    @Override
    public List<OwnerDto> getAllOwners(OwnerFilter filter) {
        List<Owner> owners = filterService.applyFilters(filter, Owner.class);

        return owners.stream().map(OwnerDto::new).toList();
    }

    public List<OwnerDto> getAllOwners() {
        var owners = ownerDao.findAll();
        return owners.stream().map(OwnerDto::new).toList();
    }

    @Override
    public void removeOwnerById(Long id) {
        ownerDao.deleteById(id);
    }

    @Override
    public void addCatToOwner(Long ownerId, Long catId) {
        Optional<Cat> optionalCat = catDao.findById(catId);
        Optional<Owner> optionalOwner = ownerDao.findById(ownerId);

        boolean bothPresents = true;

        if (optionalCat.isEmpty()) {
            bothPresents = false;
            logger.warn("Cat with ID {} not found", catId);
        }
        if (optionalOwner.isEmpty()) {
            bothPresents = false;
            logger.warn("Owner with ID {} not found", ownerId);
        }
        if (!bothPresents) {
            return;
        }


        var cat = optionalCat.get();
        var owner = optionalOwner.get();
        if (!owner.getCats().contains(cat) && cat.getOwner() == null) {
            owner.getCats().add(cat);
            ownerDao.save(owner);
            cat.setOwner(owner);
            catDao.save(cat);
        }
    }
    
    @Override
    public void removeCatFromOwner(Long ownerId, Long catId) {
        Optional<Cat> optionalCat = catDao.findById(catId);
        Optional<Owner> optionalOwner = ownerDao.findById(ownerId);

        boolean bothPresents = true;

        if (optionalCat.isEmpty()) {
            bothPresents = false;
            logger.warn("Cat with ID {} not found", catId);
        }
        if (optionalOwner.isEmpty()) {
            bothPresents = false;
            logger.warn("Owner with ID {} not found", ownerId);
        }
        if (!bothPresents) {
            return;
        }

        var cat = optionalCat.get();
        var owner = optionalOwner.get();
        if (owner.getCats().contains(cat) && cat.getOwner() != null) {
            owner.getCats().remove(cat);
            ownerDao.save(owner);
            cat.setOwner(null);
            catDao.save(cat);
        }
    }

    @Override
    public void updateOwner(OwnerDto ownerDto) {
        Owner owner = dtoToOwner(ownerDto);
        ownerDao.save(owner);
    }

    private Owner dtoToOwner(OwnerDto ownerDto) {
        Owner owner = new Owner();
        owner.setId(ownerDto.getId());
        owner.setName(ownerDto.getName());
        owner.setBirthDate(ownerDto.getBirthDate());

        if (ownerDto.getCatIds() != null) {
            List<Cat> cats = ownerDto.getCatIds().stream()
                    .map(id -> {
                        Optional<Cat> cat = catDao.findById(id);
                        if (cat.isEmpty()) {
                            logger.warn("Cat with ID {} not found for Owner ID: {}", id, ownerDto.getId());
                        }
                        return cat;
                    })
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            owner.setCats(cats);
        }

        return owner;
    }
}
