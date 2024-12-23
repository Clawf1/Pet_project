package com.clf.dao;

import com.clf.DALApplication;
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

@SpringBootTest(classes = DALApplication.class)
public class CatDaoTest {
    private final CatDao catDao;

    @Autowired
    public CatDaoTest(CatDao catDao) { this.catDao = catDao; }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
        catDao.deleteAll();
    }

    @Test
    public void testSaveAndFindCat() {
        Cat cat = new Cat();
        cat.setName("Whiskers");
        cat.setBreed("Siamese");
        cat.setColor("Brown");
        cat.setBirthDate(new Date());

        catDao.save(cat);

        Optional<Cat> foundCat = catDao.findById(cat.getId());
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
        catDao.save(cat1);

        Cat cat2 = new Cat();
        cat2.setName("Fluffy");
        cat2.setBreed("Persian");
        cat2.setColor("White");
        cat2.setBirthDate(new Date());
        catDao.save(cat2);

        List<Cat> cats = catDao.findAll();
        assertTrue(cats.size() >= 2);
    }

    @Test
    void testDeleteCat() {
        Cat cat = new Cat();
        catDao.save(cat);
        catDao.delete(cat);

        Optional<Cat> deadCat1 = catDao.findById(cat.getId());
        assertFalse(deadCat1.isPresent());
    }

    @Test
    void testAddCatFriend() {
        Cat cat1 = new Cat();
        cat1.setName("Tom");
        cat1.setBreed("British Shorthair");
        cat1.setColor("Gray");
        catDao.save(cat1);

        Cat cat2 = new Cat();
        cat2.setName("Jerry");
        cat2.setBreed("Sphynx");
        cat2.setColor("Cream");
        catDao.save(cat2);

        cat1.setFriends(List.of(cat2));
        catDao.save(cat1);

        var updatedCat1 = catDao.findById(cat1.getId());

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
        catDao.save(cat);

        String expected = String.format("Cat(id=%2d, name=%s, breed=%s, color=%s, birthDate=null, owner=null)",
                cat.getId(), cat.getName(), cat.getBreed(), cat.getColor());
        assertEquals(cat.toString(), expected);

        catDao.delete(cat);
    }
}
