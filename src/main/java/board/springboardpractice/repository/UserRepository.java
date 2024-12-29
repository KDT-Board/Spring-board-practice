package board.springboardpractice.repository;

import board.springboardpractice.domain.User;
import board.springboardpractice.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByLoginId(String loginId);

  Boolean existByLoginId(String loginId);
  Boolean existByNickname(String nickname);
  Long countAllByUserRole(UserRole userRole);

}
