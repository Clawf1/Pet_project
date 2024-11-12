package com.clf.dao;

import com.clf.model.Cat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CatDaoTest {

    private EntityManagerFactory emf;
    private CatDao catDao;

    @BeforeEach
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("cats-owners");
        catDao = new CatDao();
    }

    @AfterEach
    public void tearDown() {
        emf.close();
    }

    @Test
    public void testSaveAndFindCat() {
        Cat cat = new Cat();
        cat.setName("Whiskers");
        cat.setBreed("Siamese");
        cat.setColor("Brown");
        cat.setBirthDate(new Date());

        catDao.save(cat);

        Cat foundCat = catDao.findById(cat.getId());
        assertNotNull(foundCat);
        assertEquals("Whiskers", foundCat.getName());
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
        Cat cat1 = new Cat();
        var cat2 = new Cat();
        catDao.save(cat1);
        catDao.save(cat2);

        catDao.delete(cat1.getId());
        catDao.delete(cat2);

        var deadCat1 = catDao.findById(cat1.getId());
        var deadCat2 = catDao.findById(cat2.getId());

        assertNull(deadCat1);
        assertNull(deadCat2);
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
        catDao.update(cat1);

        Cat updatedCat1 = catDao.findById(cat1.getId());

        assertNotNull(updatedCat1);
        assertFalse(updatedCat1.getFriends().isEmpty());
        assertEquals(1, updatedCat1.getFriends().size());
        assertEquals("Jerry", updatedCat1.getFriends().get(0).getName());
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

        catDao.delete(cat.getId());
    }
}
