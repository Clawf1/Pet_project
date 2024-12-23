package com.clf.dto;

import com.clf.model.Role;
import com.clf.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Role role;
    private Long ownerId;

    public UserDto(User user) {
        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        password = user.getPassword();
        role = user.getRole();
        ownerId = user.getId();
    }
}
