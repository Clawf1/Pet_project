package com.clf.dto;

import com.clf.model.Cat;
import lombok.*;


import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
