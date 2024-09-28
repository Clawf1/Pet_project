package com.clf.service;

import com.clf.api.CatService;
import com.clf.dto.CatDto;
import com.clf.model.Cat;
import com.clf.model.Role;
import com.clf.model.User;
import com.clf.repository.CatRepository;
import com.clf.repository.OwnerRepository;
import com.clf.service.filterChain.model.CatFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;

@Service
public class CatServiceImpl implements CatService {
    private static final Logger logger = LoggerFactory.getLogger(CatServiceImpl.class);

    private final CatRepository catRepository;
    private final OwnerRepository ownerRepository;

    private final FilterServiceImpl<Cat, CatFilter> filterService;

    @Autowired
    public CatServiceImpl(CatRepository catRepository, OwnerRepository ownerRepository, FilterServiceImpl<Cat, CatFilter> filterService) {
        this.catRepository = catRepository;
        this.ownerRepository = ownerRepository;
        this.filterService = filterService;
    }

    @Override
    public void addCat(CatDto catDto) {
        if (!Objects.equals(catDto.getOwnerId(), getCurrentOwnerId())) {
            throw new AccessDeniedException("You do not have permission to add a cat for another owner");
        }
        if (catDto.getId() != null && catRepository.existsById(catDto.getId())) {
            logger.warn("Cat with ID {} already exists. Use updateCat() instead.", catDto.getId());
            return;
        }
        var cat = dtoToCat(catDto);
        catRepository.save(cat);
    }

    @Override
    public CatDto getCatById(Long id) {
        Long currentOwnerId = getCurrentOwnerId();

        var cat = catRepository.findById(id);
        return cat
                .filter(c -> c.getOwner().getId().equals(currentOwnerId))
                .map(CatDto::new)
                .orElseGet(() -> {
                    logger.warn("Cat with ID {} not found", id);
                    return null;
                });
    }

    @Override
    public List<CatDto> getAllCats(CatFilter filter) {
        Long currentOwnerId = getCurrentOwnerId();

        List<Cat> cats = filterService.applyFilters(filter, Cat.class);
        return cats
                .stream()
                .filter(c -> c.getOwner().getId().equals(currentOwnerId))
                .map(CatDto::new)
                .toList();
    }

    public List<CatDto> getAllCats() {
        Long currentOwnerId = getCurrentOwnerId();

        var cats = catRepository.findAll();
        return cats
                .stream()
                .filter(c -> c.getOwner().getId().equals(currentOwnerId))
                .map(CatDto::new)
                .toList();
    }

    @Override
    public void removeCat(CatDto catDto) {
        Long currentOwnerId = getCurrentOwnerId();

        var cat = catRepository.findById(catDto.getId()).orElseThrow(() -> new EntityNotFoundException("Cat not found"));
        if (!currentOwnerId.equals(cat.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this cat");
        }

        var updatedCat = dtoToCat(catDto);
        catRepository.delete(updatedCat);
    }

    @Override
    public void removeCat(Long id) {
        var cat = catRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cat not found"));

        Long currentOwnerId = getCurrentOwnerId();
        if (!cat.getOwner().getId().equals(currentOwnerId)) {
            throw new AccessDeniedException("You do not have permission to delete this cat");
        }
        catRepository.deleteById(id);
    }

    @Override
    public void updateCat(CatDto catDto) {
        if (catDto.getId() == null || !catRepository.existsById(catDto.getId())) {
            logger.warn("Cat with ID {} does not exist. Use addCat() instead.", catDto.getId());
            return;
        }
        var cat = catRepository.findById(catDto.getId()).orElseThrow(() -> new EntityNotFoundException("Cat not found"));
        Long currentOwnerId = getCurrentOwnerId();

        if (!cat.getOwner().getId().equals(currentOwnerId)) {
            throw new AccessDeniedException("You do not have permission to update this cat");
        }

        var updatedCat = dtoToCat(catDto);
        catRepository.save(updatedCat);
    }

    @Override
    public void makeFriends(Long id1, Long id2) {
        var cat1 = catRepository.findById(id1).orElseThrow(() -> new EntityNotFoundException("Cat with ID " + id1 + " not found"));
        var cat2 = catRepository.findById(id2).orElseThrow(() -> new EntityNotFoundException("Cat with ID " + id2 + " not found"));

        if (!cat1.getFriends().contains(cat2)) {
            cat1.getFriends().add(cat2);
            catRepository.save(cat1);
        }

        if (!cat2.getFriends().contains(cat1)) {
            cat2.getFriends().add(cat1);
            catRepository.save(cat2);
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
            ownerRepository.findById(catDto.getOwnerId())
                    .ifPresentOrElse(cat::setOwner,
                            () -> logger.warn("Owner not found for Cat ID: {}", catDto.getId()));
        }

        if (catDto.getFriendIds() != null && !catDto.getFriendIds().isEmpty()) {
            List<Cat> friends = catRepository.findAllById(catDto.getFriendIds());

            List<Long> missingFriends = catDto.getFriendIds().stream()
                    .filter(id -> friends.stream().noneMatch(friend -> friend.getId().equals(id)))
                    .toList();
            if (!missingFriends.isEmpty()) {
                missingFriends.forEach(id -> logger.warn("Friend with ID {} not found for Cat ID: {}", id, catDto.getId()));
            }

            cat.setFriends(friends);
        }

        return cat;
    }

    public Long getCurrentOwnerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((User) authentication.getPrincipal()).getOwner().getId();
        }
        return null;
    }

    public Role getCurrentRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((User) authentication.getPrincipal()).getRole();
        }
        return null;
    }
}