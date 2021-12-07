package io.turntabl.tsops.ClientConnectivity.service;

import io.turntabl.tsops.ClientConnectivity.entity.User;
import io.turntabl.tsops.ClientConnectivity.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    //get all users
    public List<User> getAllUsers(){
        return userRepository.findAll();
    };
}
