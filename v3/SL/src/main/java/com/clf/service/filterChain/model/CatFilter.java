package com.clf.service.filterChain.model;

import com.clf.service.filterChain.AbstractFilterHandler;
import com.clf.service.filterChain.handlers.*;
import com.clf.model.Cat;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class CatFilter implements BaseFilter<Cat> {
    private String name;
    private String color;
    private String breed;
    private Long age;
    private Long ownerId;
    private Date birthday;

    private AbstractFilterHandler<Cat, CatFilter> firstHandler;

    public CatFilter(String name, String color, String breed, Long age, Long ownerId, Date birthday) {
        this.name = name;
        this.birthday = birthday;
        this.color = color;
        this.breed = breed;
        this.age = age;
        this.ownerId = ownerId;
    }

    @Override
    public void createFilterChain() {
        firstHandler = new NameFilterHandler<>(CatFilter::getName);
        var colorHandler = new ColorFilterHandler<>(CatFilter::getColor);
        var breedHandler = new BreedFilterHandler<>(CatFilter::getBreed);
        var birthdayHandler = new BirthdayFilterHandler<>(CatFilter::getBirthday);
        var ageHandler = new AgeFilterHandler<>(CatFilter::getAge);
        var ownerIdHandler = new OwnerFilterHandler<>(CatFilter::getOwnerId);

        firstHandler.setNext(colorHandler);
        colorHandler.setNext(breedHandler);
        breedHandler.setNext(birthdayHandler);
        birthdayHandler.setNext(ageHandler);
        ageHandler.setNext(ownerIdHandler);
    }

    @Override
    public void applyFilter(CriteriaBuilder cb, CriteriaQuery<Cat> cq, Root<Cat> root) {
        if (firstHandler == null) createFilterChain();

        firstHandler.applyFilter(cb, cq, root, this);
    }
}
