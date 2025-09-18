package com.SweetShopManagementSystem.repository_test;

//import com.SweetShopManagementSystem.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void saveAndFindByEmail() {
        User u = new User(null, "Rahul", "r@example.com", "secret");
        userRepository.save(u);

        Optional<User> found = userRepository.findByEmail("r@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Rahul");
    }
}
