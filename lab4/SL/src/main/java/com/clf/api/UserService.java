package com.clf.api;

import com.clf.dto.UserDto;
import com.clf.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    User create(UserDto userDto);

    User getUserByUsername(String username);

    UserDetailsService userDetailsService();

    UserDto getUserById(Long id);

    UserDto getUser(String name);

    List<UserDto> getAllUsers();

    void deleteUserById(Long id);
}
