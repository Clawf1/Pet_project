package com.clf.dto;

import com.clf.model.Cat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
public class CatDto {
    private Long id;
    private String name;
    private String breed;
    private String color;
    private Date birthDate;
    private Long ownerId;
    private List<Long> friendIds;

    public CatDto(Cat cat) {
        id = cat.getId();
        name = cat.getName();
        breed = cat.getBreed();
        color = cat.getColor();
        birthDate = cat.getBirthDate();
        ownerId = cat.getOwner() != null ? cat.getOwner().getId() : null;
        friendIds = cat.getFriends().stream().map(Cat::getId).toList();
    }
}
