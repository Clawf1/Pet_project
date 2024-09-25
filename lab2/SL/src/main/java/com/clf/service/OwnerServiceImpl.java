package com.clf.service;

import com.clf.api.OwnerService;
import com.clf.dao.CatDao;
import com.clf.dao.OwnerDao;
import com.clf.dto.OwnerDto;
import com.clf.model.Cat;
import com.clf.model.Owner;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class OwnerServiceImpl implements OwnerService {
    private OwnerDao ownerDao;
    private CatDao catDao;

    @Override
    public void addOwner(OwnerDto ownerDto) {
        Owner owner = dtoToOwner(ownerDto);
        ownerDao.save(owner);
    }

    @Override
    public OwnerDto getOwnerById(Long id) {
        var owner = ownerDao.findById(id);
        return new OwnerDto(owner);
    }

    @Override
    public List<OwnerDto> getAllOwners() {
        var owners = ownerDao.findAll();
        return owners.stream().map(OwnerDto::new).toList();
    }

    @Override
    public void removeOwnerById(Long id) {
        ownerDao.delete(id);
    }

    @Override
    public void addCatToOwner(Long ownerId, Long catId) {
        var cat = catDao.findById(catId);
        var owner = ownerDao.findById(ownerId);

        if (owner != null && cat != null) {
            if (!owner.getCats().contains(cat) && cat.getOwner() == null) {
                owner.getCats().add(cat);
                ownerDao.update(owner);
                cat.setOwner(owner);
                catDao.update(cat);
            }
        }
    }

    @Override
    public void removeCatFromOwner(Long ownerId, Long catId) {
        var cat = catDao.findById(catId);
        var owner = ownerDao.findById(ownerId);

        if (owner != null && cat != null) {
            if (owner.getCats().contains(cat) && cat.getOwner() != null) {
                owner.getCats().remove(cat);
                ownerDao.update(owner);
                cat.setOwner(null);
                catDao.update(cat);
            }
        }
    }

    @Override
    public void updateOwner(OwnerDto ownerDto) {
        Owner owner = dtoToOwner(ownerDto);
        ownerDao.update(owner);
    }

    private Owner dtoToOwner(OwnerDto ownerDto) {
        Owner owner = new Owner();
        owner.setId(ownerDto.getId());
        owner.setName(ownerDto.getName());
        owner.setBirthDate(ownerDto.getBirthDate());

        if (ownerDto.getCatIds() != null) {
            List<Cat> cats = ownerDto.getCatIds().stream()
                    .map(catDao::findById)
                    .filter(Objects::nonNull)
                    .toList();
            owner.setCats(cats);
        }

        return owner;
    }
}
