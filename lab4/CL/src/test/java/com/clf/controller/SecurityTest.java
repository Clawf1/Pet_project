package com.clf.controller;

import com.clf.Application;
import com.clf.model.Role;
import com.clf.model.User;
import com.clf.service.JwtService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;


    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void testAccessToAdminEndpointWithAdminRole() throws Exception {
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRole(Role.ROLE_ADMIN);

        String token = jwtService.generateToken(testUser);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(testUser.getUsername(), null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        mockMvc.perform(get("/users/admin").header(HttpHeaders.AUTHORIZATION, "Bearer " + token)).andExpect(status().isOk());

        SecurityContextHolder.clearContext();
    }

    @Test
    public void testAccessToAdminEndpointWithUserRole() {
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRole(Role.ROLE_USER);

        String token = jwtService.generateToken(testUser);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(testUser.getUsername(), null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        assertThrows(ServletException.class, () -> mockMvc.perform(get("/users/admin").header(HttpHeaders.AUTHORIZATION, "Bearer " + token)).andExpect(status().isForbidden()));

        SecurityContextHolder.clearContext();
    }

    @Test
    public void testAccessToCatsEndpoint() throws Exception {
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRole(Role.ROLE_USER);

        String token = jwtService.generateToken(testUser);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(testUser.getUsername(), null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        mockMvc.perform(get("/cats").header(HttpHeaders.AUTHORIZATION, "Bearer " + token)).andExpect(status().isOk());

        SecurityContextHolder.clearContext();
    }

    @Test
    public void testAccessToAdminEndpointWithUser() throws Exception {
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRole(Role.ROLE_USER);

        String token = jwtService.generateToken(testUser);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(testUser.getUsername(), null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        mockMvc.perform(get("/users/12").header(HttpHeaders.AUTHORIZATION, "Bearer " + token)).andExpect(status().isNotFound());

        SecurityContextHolder.clearContext();
    }

    @Test
    public void testAccessToPublicEndpointWithInvalidToken() throws Exception {
        String invalidToken = "invalid_token";

        mockMvc.perform(get("/auth/sign-in").header(HttpHeaders.AUTHORIZATION, "Bearer " + invalidToken)).andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void testAccessToPublicEndpointWithInvalidTokenTest() throws Exception {
        String invalidToken = "invalid_token";

        mockMvc.perform(post("/auth/sign-in").header(HttpHeaders.AUTHORIZATION, "Bearer " + invalidToken)).andExpect(status().isBadRequest());
    }

    @Test
    public void testAccessToNonExistentEndpointWithValidToken() throws Exception {
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRole(Role.ROLE_USER);

        String token = jwtService.generateToken(testUser);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(testUser.getUsername(), null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        mockMvc.perform(get("/nonexistent/endpoint").header(HttpHeaders.AUTHORIZATION, "Bearer " + token)).andExpect(status().isNotFound());

        // Clear the SecurityContext after the test
        SecurityContextHolder.clearContext();
    }

}