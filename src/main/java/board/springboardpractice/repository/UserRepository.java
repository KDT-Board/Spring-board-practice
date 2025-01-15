package board.springboardpractice.repository;

import board.springboardpractice.domain.user.User;
import board.springboardpractice.domain.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByLoginId(String loginId);
  Boolean existsByLoginId(String loginId);
  Boolean existsByNickname(String nickname);
  Long countAllByUserRole(UserRole userRole);

}
