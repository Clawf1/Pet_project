package com.clf.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "cats")
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String breed;
    private String color;

    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "cat_friends",
            joinColumns = @JoinColumn(name = "cat_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    @ToString.Exclude
    private List<Cat> friends = new ArrayList<>();
}
