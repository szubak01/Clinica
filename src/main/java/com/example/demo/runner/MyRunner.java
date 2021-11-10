package com.example.demo.runner;


import com.example.demo.model.User;
import com.example.demo.model.enums.Role;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MyRunner implements CommandLineRunner {

  @Autowired private final UserRepository userRepo;
  @Autowired private final UserService userService;

  @Override
  public void run(String... args) throws Exception {
    setAdmin();
  }

  private void setAdmin(){
    List<User> users = userService.getAllUsers();

    Optional<User> admin = users
        .stream()
        .filter(user -> user.getRole().equals(Role.ADMIN))
        .findFirst();

    if(admin.isEmpty()){
      userRepo.save(new User( "admin", "pass", Role.ADMIN));
    }
  }

}
