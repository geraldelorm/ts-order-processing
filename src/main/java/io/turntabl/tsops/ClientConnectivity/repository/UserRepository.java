package io.turntabl.tsops.ClientConnectivity.repository;

import io.turntabl.tsops.ClientConnectivity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Override
    List<User> findAll();


    @Override
    void delete(User user);
}
