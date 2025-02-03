package board.springboardpractice.api.entity.board.infrastructure;

import board.springboardpractice.api.entity.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
  List<Board> findAll(String name);
}
