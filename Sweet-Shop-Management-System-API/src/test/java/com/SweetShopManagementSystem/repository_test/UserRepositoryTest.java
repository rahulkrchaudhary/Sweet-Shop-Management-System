package com.SweetShopManagementSystem.repository_test;

//import com.SweetShopManagementSystem.model.User;
import com.SweetShopManagementSystem.model.User;
import com.SweetShopManagementSystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void saveAndFindByEmail() {
        User u = new User();
        u.setName("Rahul");
        u.setEmail("r@example.com");
        u.setPassword("secret");
        userRepository.save(u);

        Optional<User> found = userRepository.findByEmail("r@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Rahul");
    }
}
