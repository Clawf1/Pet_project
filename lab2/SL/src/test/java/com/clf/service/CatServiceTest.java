package com.clf.service;

import com.clf.dao.CatDao;
import com.clf.dao.OwnerDao;
import com.clf.dto.CatDto;
import com.clf.model.Cat;
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

class CatServiceTest {

    @Mock
    private CatDao catDao;

    @Mock
    private OwnerDao ownerDao;

    @InjectMocks
    private CatServiceImpl catService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCat() {
        Cat cat = new Cat();
        cat.setName("Tom");

        CatDto catDto = new CatDto(cat);

        doNothing().when(catDao).save(any(Cat.class));

        catService.addCat(catDto);

        verify(catDao, times(1)).save(cat);
    }

    @Test
    void testGetCatById() {
        Cat cat = new Cat();
        cat.setId(1L);
        cat.setName("Tom");
        when(catDao.findById(1L)).thenReturn(cat);

        CatDto result = catService.getCatById(1L);

        assertNotNull(result);
        assertEquals("Tom", result.getName());
        verify(catDao, times(1)).findById(1L);
    }

    @Test
    void testGetAllCats() {
        Cat cat1 = new Cat();
        cat1.setName("Tom");
        Cat cat2 = new Cat();
        cat2.setName("Jerry");

        when(catDao.findAll()).thenReturn(List.of(cat1, cat2));

        List<CatDto> cats = catService.getAllCats();

        assertNotNull(cats);
        assertEquals(2, cats.size());
        assertEquals("Tom", cats.get(0).getName());
        assertEquals("Jerry", cats.get(1).getName());
        verify(catDao, times(1)).findAll();
    }

    @Test
    void testRemoveCat() {
        Cat cat = new Cat();
        cat.setId(1L);

        CatDto catDto = new CatDto(cat);

        doNothing().when(catDao).delete(any(Cat.class));
        doNothing().when(catDao).delete(cat.getId());

        catService.removeCat(catDto);
        catService.removeCat(cat.getId());

        verify(catDao, times(1)).delete(cat);
        verify(catDao, times(1)).delete(cat.getId());
    }

    @Test
    void testUpdateCat() {
        Cat cat = new Cat();
        cat.setId(1L);
        cat.setName("Tom");

        CatDto catDto = new CatDto(cat);

        doNothing().when(catDao).update(any(Cat.class));

        catService.updateCat(catDto);

        verify(catDao, times(1)).update(cat);
    }

    @Test
    void testMakeFriends() {
        Cat cat1 = new Cat();
        cat1.setId(1L);
        cat1.setFriends(new ArrayList<>());

        Cat cat2 = new Cat();
        cat2.setId(2L);
        cat2.setFriends(new ArrayList<>());

        when(catDao.findById(1L)).thenReturn(cat1);
        when(catDao.findById(2L)).thenReturn(cat2);
        doNothing().when(catDao).update(any(Cat.class));

        catService.makeFriends(1L, 2L);

        assertTrue(cat1.getFriends().contains(cat2));
        assertTrue(cat2.getFriends().contains(cat1));
        verify(catDao, times(1)).update(cat1);
        verify(catDao, times(1)).update(cat2);
    }

    @Test
    void testMakeFriendsWithExistingFriendship() {
        Cat cat1 = new Cat();
        cat1.setId(1L);
        Cat cat2 = new Cat();
        cat2.setId(2L);

        cat1.setFriends(new ArrayList<>(List.of(cat2)));
        cat2.setFriends(new ArrayList<>(List.of(cat1)));

        when(catDao.findById(1L)).thenReturn(cat1);
        when(catDao.findById(2L)).thenReturn(cat2);
        doNothing().when(catDao).update(any(Cat.class));

        catService.makeFriends(1L, 2L);

        verify(catDao, times(0)).update(cat1);
        verify(catDao, times(0)).update(cat2);
    }

    @Test
    void testMakeFriendsWithNullCats() {
        when(catDao.findById(1L)).thenReturn(null);
        when(catDao.findById(2L)).thenReturn(null);

        catService.makeFriends(1L, 2L);

        verify(catDao, times(0)).update(any(Cat.class));
    }
}