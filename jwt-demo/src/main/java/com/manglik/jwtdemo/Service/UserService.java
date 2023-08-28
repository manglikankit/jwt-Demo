package com.manglik.jwtdemo.Service;

import com.manglik.jwtdemo.Repository.UserRepository;
import com.manglik.jwtdemo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

//    public UserService() {
//        store.add(new User(UUID.randomUUID().toString(), "Ankit", "a@m.com"));
//        store.add(new User(UUID.randomUUID().toString(), "A", "a@b.com"));
//        store.add(new User(UUID.randomUUID().toString(), "Manglik", "s@m.com"));
//        store.add(new User(UUID.randomUUID().toString(), "Anav", "v@m.com"));
//    }
    public List<User> getUsers(){
        return userRepository.findAll();
    }
    public User createUser(User user){
        user.setId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
