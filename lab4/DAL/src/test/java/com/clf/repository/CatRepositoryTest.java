package com.clf.repository;

import com.clf.model.Cat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CatRepositoryTest {
    private final CatRepository catRepository;

    @Autowired
    public CatRepositoryTest(CatRepository catRepository) { this.catRepository = catRepository; }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
        catRepository.deleteAll();
    }

    @Test
    public void testSaveAndFindCat() {
        Cat cat = new Cat();
        cat.setName("Whiskers");
        cat.setBreed("Siamese");
        cat.setColor("Brown");
        cat.setBirthDate(new Date());

        catRepository.save(cat);

        Optional<Cat> foundCat = catRepository.findById(cat.getId());
        assertTrue(foundCat.isPresent());
        assertEquals("Whiskers", foundCat.get().getName());
    }

    @Test
    public void testFindAllCats() {
        Cat cat1 = new Cat();
        cat1.setName("Whiskers");
        cat1.setBreed("Siamese");
        cat1.setColor("Brown");
        cat1.setBirthDate(new Date());
        catRepository.save(cat1);

        Cat cat2 = new Cat();
        cat2.setName("Fluffy");
        cat2.setBreed("Persian");
        cat2.setColor("White");
        cat2.setBirthDate(new Date());
        catRepository.save(cat2);

        List<Cat> cats = catRepository.findAll();
        assertTrue(cats.size() >= 2);
    }

    @Test
    void testDeleteCat() {
        Cat cat = new Cat();
        catRepository.save(cat);
        catRepository.delete(cat);

        Optional<Cat> deadCat1 = catRepository.findById(cat.getId());
        assertFalse(deadCat1.isPresent());
    }

    @Test
    void testAddCatFriend() {
        Cat cat1 = new Cat();
        cat1.setName("Tom");
        cat1.setBreed("British Shorthair");
        cat1.setColor("Gray");
        catRepository.save(cat1);

        Cat cat2 = new Cat();
        cat2.setName("Jerry");
        cat2.setBreed("Sphynx");
        cat2.setColor("Cream");
        catRepository.save(cat2);

        cat1.setFriends(List.of(cat2));
        catRepository.save(cat1);

        var updatedCat1 = catRepository.findById(cat1.getId());

        assertTrue(updatedCat1.isPresent());
        assertFalse(updatedCat1.get().getFriends().isEmpty());
        assertEquals(1, updatedCat1.get().getFriends().size());
        assertEquals("Jerry", updatedCat1.get().getFriends().getFirst().getName());
    }

    @Test
    void testToString() {
        Cat cat = new Cat();
        cat.setName("Tom");
        cat.setBreed("British Shorthair");
        cat.setColor("Gray");
        catRepository.save(cat);

        String expected = String.format("Cat(id=%2d, name=%s, breed=%s, color=%s, birthDate=null, owner=null)",
                cat.getId(), cat.getName(), cat.getBreed(), cat.getColor());
        assertEquals(cat.toString(), expected);

        catRepository.delete(cat);
    }
}
