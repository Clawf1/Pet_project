package com.clf.repository;

import com.clf.DALApplication;
import com.clf.model.Owner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DALApplication.class)
public class OwnerRepositoryTest {

    private final OwnerRepository ownerRepository;

    @Autowired
    public OwnerRepositoryTest(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @AfterEach
    public void tearDown() {
        ownerRepository.deleteAll();
    }

    @Test
    public void testSaveAndFindOwner() {
        Owner owner = new Owner();
        owner.setName("John Doe");
        owner.setBirthDate(new Date());

        ownerRepository.save(owner);

        Optional<Owner> foundOwner = ownerRepository.findById(owner.getId());
        assertTrue(foundOwner.isPresent());
        assertEquals("John Doe", foundOwner.get().getName());
    }

    @Test
    public void testFindAllOwners() {
        Owner owner1 = new Owner();
        owner1.setName("John Doe");
        owner1.setBirthDate(new Date());
        ownerRepository.save(owner1);

        Owner owner2 = new Owner();
        owner2.setName("Jane Smith");
        owner2.setBirthDate(new Date());
        ownerRepository.save(owner2);

        List<Owner> owners = ownerRepository.findAll();
        assertTrue(owners.size() >= 2);
    }

    @Test
    void testDeleteOwner() {
        var owner1 = new Owner();
        var owner2 = new Owner();
        ownerRepository.save(owner1);
        ownerRepository.save(owner2);

        ownerRepository.deleteById(owner1.getId());
        ownerRepository.delete(owner2);

        Optional<Owner> deadOwner1 = ownerRepository.findById(owner1.getId());
        Optional<Owner> deadOwner2 = ownerRepository.findById(owner2.getId());

        assertTrue(deadOwner1.isEmpty());
        assertTrue(deadOwner2.isEmpty());
    }

    @Test
    void testUpdateOwner() {
        var owner = new Owner();
        ownerRepository.save(owner);
        owner.setName("Jane Smith");
        ownerRepository.save(owner);
        Optional<Owner> foundOwner = ownerRepository.findById(owner.getId());
        assertTrue(foundOwner.isPresent());
        assertEquals("Jane Smith", foundOwner.get().getName());
    }

    @Test
    void testToString() {
        Owner owner = new Owner();
        owner.setName("Tom");
        ownerRepository.save(owner);

        String expected = String.format("Owner(id=%2d, name=%s, birthDate=null)",
                owner.getId(), owner.getName());
        assertEquals(owner.toString(), expected);

        ownerRepository.deleteById(owner.getId());
    }
}
