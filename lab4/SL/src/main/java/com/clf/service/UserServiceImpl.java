package com.clf.service;

import com.clf.api.UserService;
import com.clf.dto.UserDto;
import com.clf.model.Owner;
import com.clf.model.User;
import com.clf.repository.OwnerRepository;
import com.clf.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, OwnerRepository ownerRepository) {
        this.userRepository = userRepository;
        this.ownerRepository = ownerRepository;
    }

    @Override
    @Transactional
    public User create(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) return new User();

        Owner owner = null;
        if (userDto.getOwnerId() != null) {
            owner = ownerRepository.findById(userDto.getOwnerId())
                    .orElseThrow(() -> new EntityNotFoundException("Owner with ID " + userDto.getOwnerId() + " not found"));
        }

        var user = new User();
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setOwner(owner);

        return userRepository.save(user);
    }

    @Override
    public UserDto update(UserDto userDto) {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userDto.getId() + " not found"));

        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());

        if (userDto.getRole() != null) user.setRole(userDto.getRole());

        if (userDto.getUsername() != null) user.setUsername(userDto.getUsername());

        if (userDto.getPassword() != null) user.setPassword(userDto.getPassword());

        if (userDto.getOwnerId() != null) {
            Owner owner = ownerRepository.findById(userDto.getOwnerId())
                    .orElseThrow(() -> new EntityNotFoundException("Owner with ID " + userDto.getOwnerId() + " not found"));
            user.setOwner(owner);
        }

        User updatedUser = userRepository.save(user);

        return new UserDto(updatedUser);
    }

    @Override
    public UserDto getUser(String username) {
        var user = userRepository.findByUsername(username);
        return user.map(UserDto::new).orElse(null);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserDto::new).toList();
    }

    @Override
    public UserDto getUserById(Long id) {
        var user = userRepository.findById(id);
        return user.map(UserDto::new).orElse(null);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with name " + username + " not found"));
    }

    public boolean existsByUsernameOrEmail(String username, String email) {
        return existsByUsername(username) || existsByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public UserDetailsService userDetailsService() {
        return this::getUserByUsername;
    }
}
