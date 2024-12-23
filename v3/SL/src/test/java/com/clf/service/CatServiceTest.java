package com.clf.service;

import com.clf.repository.CatRepository;
import com.clf.repository.OwnerRepository;
import com.clf.dto.CatDto;
import com.clf.model.Cat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class CatServiceTest {

    @MockBean
    private CatRepository catRepository;

    @MockBean
    private OwnerRepository ownerRepository;

    @Autowired
    private CatServiceImpl catService;

    @Test
    void testAddCat() {
        Cat cat = new Cat();
        cat.setName("Tom");

        CatDto catDto = new CatDto(cat);

        when(catRepository.save(any(Cat.class))).thenReturn(cat);

        catService.addCat(catDto);

        verify(catRepository, times(1)).save(cat);
    }

    @Test
    void testGetCatById() {
        Cat cat = new Cat();
        cat.setId(1L);
        cat.setName("Tom");
        when(catRepository.findById(1L)).thenReturn(Optional.of(cat));

        CatDto result = catService.getCatById(1L);

        assertNotNull(result);
        assertEquals("Tom", result.getName());
        verify(catRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllCats() {
        Cat cat1 = new Cat();
        cat1.setName("Tom");
        Cat cat2 = new Cat();
        cat2.setName("Jerry");

        when(catRepository.findAll()).thenReturn(List.of(cat1, cat2));

        List<CatDto> cats = catService.getAllCats();

        assertNotNull(cats);
        assertEquals(2, cats.size());
        assertEquals("Tom", cats.get(0).getName());
        assertEquals("Jerry", cats.get(1).getName());
        verify(catRepository, times(1)).findAll();
    }

    @Test
    void testRemoveCat() {
        Cat cat = new Cat();
        cat.setId(1L);
        CatDto catDto = new CatDto(cat);

        doNothing().when(catRepository).delete(any(Cat.class));

        catService.removeCat(catDto);

        verify(catRepository, times(1)).delete(cat);
    }

    @Test
    void testUpdateCat() {
        Cat cat = new Cat();
        cat.setId(1L);
        cat.setName("Tom");

        CatDto catDto = new CatDto(cat);

        when(catRepository.save(any(Cat.class))).thenReturn(cat);

        catService.updateCat(catDto);

        verify(catRepository, times(1)).save(cat);
    }

    @Test
    void testMakeFriends() {
        Cat cat1 = new Cat();
        cat1.setId(1L);
        cat1.setFriends(new ArrayList<>());

        Cat cat2 = new Cat();
        cat2.setId(2L);
        cat2.setFriends(new ArrayList<>());

        when(catRepository.findById(1L)).thenReturn(Optional.of(cat1));
        when(catRepository.findById(2L)).thenReturn(Optional.of(cat2));
        when(catRepository.save(cat1)).thenReturn(cat1);
        when(catRepository.save(cat2)).thenReturn(cat2);

        catService.makeFriends(1L, 2L);

        assertTrue(cat1.getFriends().contains(cat2));
        assertTrue(cat2.getFriends().contains(cat1));
        verify(catRepository, times(1)).save(cat1);
        verify(catRepository, times(1)).save(cat2);
    }

    @Test
    void testMakeFriendsWithExistingFriendship() {
        Cat cat1 = new Cat();
        cat1.setId(1L);
        Cat cat2 = new Cat();
        cat2.setId(2L);

        cat1.setFriends(new ArrayList<>(List.of(cat2)));
        cat2.setFriends(new ArrayList<>(List.of(cat1)));

        when(catRepository.findById(1L)).thenReturn(Optional.of(cat1));
        when(catRepository.findById(2L)).thenReturn(Optional.of(cat2));
        when(catRepository.save(cat1)).thenReturn(cat1);
        when(catRepository.save(cat2)).thenReturn(cat2);

        catService.makeFriends(1L, 2L);

        verify(catRepository, times(0)).save(cat1);
        verify(catRepository, times(0)).save(cat2);
    }

    @Test
    void testMakeFriendsWithNullCats() {
        when(catRepository.findById(1L)).thenReturn(Optional.empty());
        when(catRepository.findById(2L)).thenReturn(Optional.empty());

        catService.makeFriends(1L, 2L);

        verify(catRepository, times(0)).save(any(Cat.class));
    }
}