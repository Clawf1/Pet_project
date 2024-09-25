package com.clf.dao;

import com.clf.model.Owner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OwnerDaoTest {

    private EntityManagerFactory emf;
    private OwnerDao ownerDao;

    @BeforeEach
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("cats-owners");
        ownerDao = new OwnerDao();
    }

    @AfterEach
    public void tearDown() {
        emf.close();
    }

    @Test
    public void testSaveAndFindOwner() {
        Owner owner = new Owner();
        owner.setName("John Doe");
        owner.setBirthDate(new Date());

        ownerDao.save(owner);

        Owner foundOwner = ownerDao.findById(owner.getId());
        assertNotNull(foundOwner);
        assertEquals("John Doe", foundOwner.getName());
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

        ownerDao.delete(owner1.getId());
        ownerDao.delete(owner2);

        var deadOwner1 = ownerDao.findById(owner1.getId());
        var deadOwner2 = ownerDao.findById(owner2.getId());

        assertNull(deadOwner1);
        assertNull(deadOwner2);
    }

    @Test
    void testUpdateOwner() {
        var owner = new Owner();
        ownerDao.save(owner);
        owner.setName("Jane Smith");
        ownerDao.update(owner);
        Owner foundOwner = ownerDao.findById(owner.getId());
        assertEquals("Jane Smith", foundOwner.getName());
    }

    @Test
    void testToString() {
        Owner owner = new Owner();
        owner.setName("Tom");
        ownerDao.save(owner);

        String expected = String.format("Owner(id=%2d, name=%s, birthDate=null)",
                owner.getId(), owner.getName());
        assertEquals(owner.toString(), expected);

        ownerDao.delete(owner.getId());
    }
}
