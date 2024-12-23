package com.clf.controller;

import com.clf.Application;
import com.clf.api.CatService;
import com.clf.dto.CatDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class CatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CatService catService;

    @ParameterizedTest
    @MethodSource("provideCatDataWithNoProblems")
    public void testGetAllCatsWithNoFilterShouldReturnAllCats(List<CatDto> cats) throws Exception {
        when(catService.getAllCats(Mockito.any())).thenReturn(cats);

        String response = mockMvc.perform(get("/cats/all"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<CatDto> responseCats = objectMapper.readValue(response, new TypeReference<>() {});

        assertEquals(cats.size(), responseCats.size());
    }

    private static Stream<Arguments> provideCatDataWithNoProblems() {
        return Stream.of(
                Arguments.of(List.of(
                        new CatDto(1L, "Tom", "br", "green", null, null, List.of()),
                        new CatDto(2L, "Jack", "rc", "red", null, null, List.of())
                )),
                Arguments.of(List.of(
                        new CatDto(3L, "Nick", "sc", "blue", null, null, List.of())
                ))
        );
    }

    @ParameterizedTest
    @MethodSource("provideFilteredCatData")
    public void testGetFilteredCats(List<CatDto> filteredCats, String color, String breed) throws Exception {
        when(catService.getAllCats(Mockito.any())).thenReturn(filteredCats);

        String response = mockMvc.perform(get("/cats/all")
                        .param("color", color)
                        .param("breed", breed))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<CatDto> responseCats = objectMapper.readValue(response, new TypeReference<>() {});

        assertEquals(filteredCats.size(), responseCats.size());
    }

    private static Stream<Arguments> provideFilteredCatData() {
        return Stream.of(
                Arguments.of(List.of(
                        new CatDto(3L, "Nick", "rc", "red", null, null, List.of())
                ), "red", "rc"),
                Arguments.of(List.of(
                        new CatDto(4L, "Mike", "rc", "black", null, null, List.of())
                ), "black", "rc")
        );
    }
}
