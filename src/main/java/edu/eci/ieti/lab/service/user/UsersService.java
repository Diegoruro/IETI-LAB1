package edu.eci.ieti.lab.service.user;

import edu.eci.ieti.lab.model.user.User;
import edu.eci.ieti.lab.model.user.UserDto;

import java.util.List;
import java.util.Optional;

public interface UsersService {
    User save(UserDto user);

    Optional<User> findById(String id);

    List<User> all();

    void deleteById(String id);

    public Optional<User> findByEmail(String email);

    User update(UserDto user, String userId);
}

