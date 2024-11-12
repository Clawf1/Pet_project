package com.clf.service;

import com.clf.api.OwnerService;
import com.clf.repository.CatRepository;
import com.clf.repository.OwnerRepository;
import com.clf.dto.OwnerDto;
import com.clf.service.filterChain.model.OwnerFilter;
import com.clf.model.Cat;
import com.clf.model.Owner;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerServiceImpl implements OwnerService {
    private static final Logger logger = LoggerFactory.getLogger(OwnerServiceImpl.class);

    private final OwnerRepository ownerRepository;
    private final CatRepository catRepository;

    private final FilterServiceImpl<Owner, OwnerFilter> filterService;

    @Autowired
    public OwnerServiceImpl(OwnerRepository ownerRepository, CatRepository catRepository, FilterServiceImpl<Owner, OwnerFilter> filterService) {
        this.ownerRepository = ownerRepository;
        this.catRepository = catRepository;
        this.filterService = filterService;
    }

    @Override
    public void addOwner(OwnerDto ownerDto) {
        if (ownerDto.getId() != null && ownerRepository.existsById(ownerDto.getId())) {
            logger.warn("Owner with ID {} already exists. Use updateOwner() instead.", ownerDto.getId());
            return;
        }
        Owner owner = dtoToOwner(ownerDto);
        ownerRepository.save(owner);
    }

    @Override
    public OwnerDto getOwnerById(Long id) {
        var owner = ownerRepository.findById(id);
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
        var owners = ownerRepository.findAll();
        return owners.stream().map(OwnerDto::new).toList();
    }

    @Override
    public void removeOwnerById(Long id) {
        ownerRepository.deleteById(id);
    }

    @Override
    public void addCatToOwner(Long ownerId, Long catId) {
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException("Cat with ID " + catId + " not found"));
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Owner with ID " + ownerId + " not found"));

        if (!owner.getCats().contains(cat) && cat.getOwner() == null) {
            owner.getCats().add(cat);
            ownerRepository.save(owner);
            cat.setOwner(owner);
            catRepository.save(cat);
        }
    }
    
    @Override
    public void removeCatFromOwner(Long ownerId, Long catId) {
        Cat cat = catRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException("Cat with ID " + catId + " not found"));
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Owner with ID " + ownerId + " not found"));

        if (owner.getCats().contains(cat) && cat.getOwner() != null) {
            owner.getCats().remove(cat);
            ownerRepository.save(owner);
            cat.setOwner(null);
            catRepository.save(cat);
        }
    }

    @Override
    public void updateOwner(OwnerDto ownerDto) {
        if (ownerDto.getId() == null || !ownerRepository.existsById(ownerDto.getId())) {
            logger.warn("Owner with ID {} does not exist. Use addOwner() instead.", ownerDto.getId());
            return;
        }
        Owner owner = dtoToOwner(ownerDto);
        ownerRepository.save(owner);
    }

    private Owner dtoToOwner(OwnerDto ownerDto) {
        Owner owner = new Owner();
        owner.setId(ownerDto.getId());
        owner.setName(ownerDto.getName());
        owner.setBirthDate(ownerDto.getBirthDate());

        if (ownerDto.getCatIds() != null && !ownerDto.getCatIds().isEmpty()) {
            List<Cat> cats = catRepository.findAllById(ownerDto.getCatIds());

            List<Long> missingCats = ownerDto.getCatIds().stream()
                    .filter(id -> cats.stream().noneMatch(cat -> cat.getId().equals(id)))
                    .toList();

            if (!missingCats.isEmpty()) {
                missingCats.forEach(id -> logger.warn("Cat with ID {} not found for Owner ID: {}", id, ownerDto.getId()));
            }

            owner.setCats(cats);
        }

        return owner;
    }
}
