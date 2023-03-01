package edu.eci.ieti.lab.controllers.user;

import edu.eci.ieti.lab.exception.UserNotFoundException;
import edu.eci.ieti.lab.model.user.User;
import edu.eci.ieti.lab.model.user.UserDto;
import edu.eci.ieti.lab.service.user.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/users/")
public class UsersController {

    private final UsersService usersService;

    public UsersController(@Autowired UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        User savedUser = usersService.save(userDto);
        URI createdUserUri = URI.create("/v1/users/" + savedUser.getId());
        return ResponseEntity.created(createdUserUri).body(savedUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {

        return ResponseEntity.ok(usersService.all());
    }

    @GetMapping("{id}")
    public ResponseEntity<User> findById(@PathVariable("id") String id)  {
        Optional<User> user = usersService.findById(id);
        if (user.isPresent()){
            return ResponseEntity.ok(user.get());
        }else {
            throw new UserNotFoundException(id);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") String id, @RequestBody UserDto user) {
        Optional<User> userResponse = usersService.findById(id);
        if (userResponse.isPresent()){
            User userUpdated = usersService.update(user,id);
            return ResponseEntity.ok(userUpdated);
        }else {
            throw new UserNotFoundException(id);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id){
        Optional<User> userResponse = usersService.findById(id);
        if (userResponse.isPresent()){
            usersService.deleteById(id);
            return ResponseEntity.ok().build();
        }else {
            throw new UserNotFoundException(id);
        }
    }
}
