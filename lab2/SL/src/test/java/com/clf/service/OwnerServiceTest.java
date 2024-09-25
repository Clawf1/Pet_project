package com.clf.service;

import com.clf.dao.CatDao;
import com.clf.dao.OwnerDao;
import com.clf.dto.OwnerDto;
import com.clf.model.Cat;
import com.clf.model.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OwnerServiceTest {

    @Mock
    private OwnerDao ownerDao;

    @Mock
    private CatDao catDao;

    @InjectMocks
    private OwnerServiceImpl ownerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOwner() {
        Owner owner = new Owner();
        owner.setName("John");

        var ownerDto = new OwnerDto(owner);

        doNothing().when(ownerDao).save(any(Owner.class));

        ownerService.addOwner(ownerDto);

        verify(ownerDao, times(1)).save(owner);
    }

    @Test
    void testGetOwnerById() {
        Owner owner = new Owner();
        owner.setName("John");
        when(ownerDao.findById(1L)).thenReturn(owner);

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
    void testRemoveOwnerById() {
        doNothing().when(ownerDao).delete(any(Long.class));

        ownerService.removeOwnerById(1L);

        verify(ownerDao, times(1)).delete(1L);
    }

    @Test
    void testUpdateOwner() {
        Owner owner = new Owner();
        owner.setId(1L);
        owner.setName("John");

        var ownerDto = new OwnerDto(owner);

        doNothing().when(ownerDao).update(any(Owner.class));

        ownerService.updateOwner(ownerDto);

        verify(ownerDao, times(1)).update(owner);
    }

    @Test
    void testAddCatToOwner() {
        Owner owner = new Owner();
        owner.setId(1L);
        owner.setName("John Doe");
        owner.setCats(new ArrayList<>());

        Cat cat = new Cat();
        cat.setId(2L);

        when(ownerDao.findById(1L)).thenReturn(owner);
        when(catDao.findById(2L)).thenReturn(cat);
        doNothing().when(ownerDao).update(any(Owner.class));
        doNothing().when(catDao).update(any(Cat.class));

        ownerService.addCatToOwner(1L, 2L);

        assertTrue(owner.getCats().contains(cat));
        assertEquals(owner, cat.getOwner());
        verify(ownerDao, times(1)).update(owner);
        verify(catDao, times(1)).update(cat);
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

        when(ownerDao.findById(1L)).thenReturn(owner);
        when(catDao.findById(2L)).thenReturn(cat);
        doNothing().when(ownerDao).update(any(Owner.class));
        doNothing().when(catDao).update(any(Cat.class));

        ownerService.removeCatFromOwner(1L, 2L);

        assertFalse(owner.getCats().contains(cat));
        assertNull(cat.getOwner());
        verify(ownerDao, times(1)).update(owner);
        verify(catDao, times(1)).update(cat);
    }
}
