package com.clf.dao;

import com.clf.Application;
import com.clf.model.Owner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Application.class)
public class OwnerDaoTest {

    private final OwnerDao ownerDao;

    @Autowired
    public OwnerDaoTest(OwnerDao ownerDao) {
        this.ownerDao = ownerDao;
    }

    @AfterEach
    public void tearDown() {
        ownerDao.deleteAll();
    }

    @Test
    public void testSaveAndFindOwner() {
        Owner owner = new Owner();
        owner.setName("John Doe");
        owner.setBirthDate(new Date());

        ownerDao.save(owner);

        Optional<Owner> foundOwner = ownerDao.findById(owner.getId());
        assertTrue(foundOwner.isPresent());
        assertEquals("John Doe", foundOwner.get().getName());
    }

    @Test
    public void testFindAllOwners() {
        Owner owner1 = new Owner();
        owner1.setName("John Doe");
        owner1.setBirthDate(new Date());
        ownerDao.save(owner1);

        Owner owner2 = new Owner();
        owner2.setName("Jane Smith");
        owner2.setBirthDate(new Date());
        ownerDao.save(owner2);

        List<Owner> owners = ownerDao.findAll();
        assertTrue(owners.size() >= 2);
    }

    @Test
    void testDeleteOwner() {
        var owner1 = new Owner();
        var owner2 = new Owner();
        ownerDao.save(owner1);
        ownerDao.save(owner2);

        ownerDao.deleteById(owner1.getId());
        ownerDao.delete(owner2);

        Optional<Owner> deadOwner1 = ownerDao.findById(owner1.getId());
        Optional<Owner> deadOwner2 = ownerDao.findById(owner2.getId());

        assertTrue(deadOwner1.isEmpty());
        assertTrue(deadOwner2.isEmpty());
    }

    @Test
    void testUpdateOwner() {
        var owner = new Owner();
        ownerDao.save(owner);
        owner.setName("Jane Smith");
        ownerDao.save(owner);
        Optional<Owner> foundOwner = ownerDao.findById(owner.getId());
        assertTrue(foundOwner.isPresent());
        assertEquals("Jane Smith", foundOwner.get().getName());
    }

    @Test
    void testToString() {
        Owner owner = new Owner();
        owner.setName("Tom");
        ownerDao.save(owner);

        String expected = String.format("Owner(id=%2d, name=%s, birthDate=null)",
                owner.getId(), owner.getName());
        assertEquals(owner.toString(), expected);

        ownerDao.deleteById(owner.getId());
    }
}
