package com.clf.service;

import com.clf.SLApplication;
import com.clf.dao.CatDao;
import com.clf.dao.OwnerDao;
import com.clf.dto.OwnerDto;
import com.clf.model.Cat;
import com.clf.model.Owner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = SLApplication.class)
class OwnerServiceTest {

    @MockBean
    private OwnerDao ownerDao;

    @MockBean
    private CatDao catDao;

    @Autowired
    private OwnerServiceImpl ownerService;

    @Test
    void testAddOwner() {
        Owner owner = new Owner();
        owner.setName("John");

        var ownerDto = new OwnerDto(owner);

        when(ownerDao.save(owner)).thenReturn(owner);

        ownerService.addOwner(ownerDto);

        verify(ownerDao, times(1)).save(owner);
    }

    @Test
    void testGetOwnerById() {
        Owner owner = new Owner();
        owner.setName("John");
        when(ownerDao.findById(1L)).thenReturn(Optional.of(owner));

        OwnerDto result = ownerService.getOwnerById(1L);

        assertNotNull(result);
        assertEquals("John", result.getName());
        verify(ownerDao, times(1)).findById(1L);
    }

    @Test
    void testGetAllOwners() {
        Owner owner1 = new Owner();
        owner1.setName("John");
        Owner owner2 = new Owner();
        owner2.setName("Jane");

        when(ownerDao.findAll()).thenReturn(List.of(owner1, owner2));

        List<OwnerDto> owners = ownerService.getAllOwners();

        assertNotNull(owners);
        assertEquals(2, owners.size());
        assertEquals("John", owners.get(0).getName());
        assertEquals("Jane", owners.get(1).getName());
        verify(ownerDao, times(1)).findAll();
    }

    @Test
    void testUpdateOwner() {
        Owner owner = new Owner();
        owner.setId(1L);
        owner.setName("John");

        var ownerDto = new OwnerDto(owner);

        when(ownerDao.save(owner)).thenReturn(owner);

        ownerService.updateOwner(ownerDto);

        verify(ownerDao, times(1)).save(owner);
    }

    @Test
    void testAddCatToOwner() {
        Owner owner = new Owner();
        owner.setId(1L);
        owner.setName("John Doe");
        owner.setCats(new ArrayList<>());

        Cat cat = new Cat();
        cat.setId(2L);

        when(ownerDao.findById(1L)).thenReturn(Optional.of(owner));
        when(catDao.findById(2L)).thenReturn(Optional.of(cat));
        when(ownerDao.save(owner)).thenReturn(owner);
        when(catDao.save(cat)).thenReturn(cat);

        ownerService.addCatToOwner(1L, 2L);

        assertTrue(owner.getCats().contains(cat));
        assertEquals(owner, cat.getOwner());
        verify(ownerDao, times(1)).save(owner);
        verify(catDao, times(1)).save(cat);
    }

    @Test
    void testRemoveCatFromOwner() {
        Owner owner = new Owner();
        owner.setId(1L);
        owner.setName("John Doe");
        Cat cat = new Cat();
        cat.setId(2L);
        cat.setOwner(owner);

        owner.setCats(new ArrayList<>(List.of(cat)));

        when(ownerDao.findById(1L)).thenReturn(Optional.of(owner));
        when(catDao.findById(2L)).thenReturn(Optional.of(cat));
        when(ownerDao.save(owner)).thenReturn(owner);
        when(catDao.save(cat)).thenReturn(cat);

        ownerService.removeCatFromOwner(1L, 2L);

        assertFalse(owner.getCats().contains(cat));
        assertNull(cat.getOwner());
        verify(ownerDao, times(1)).save(owner);
        verify(catDao, times(1)).save(cat);
    }
}
