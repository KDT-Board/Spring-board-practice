package board.springboardpractice.api.entity.user.infrastructure;

import board.springboardpractice.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);


  Optional<User> findByEmail(String kakaoEmail);
}
