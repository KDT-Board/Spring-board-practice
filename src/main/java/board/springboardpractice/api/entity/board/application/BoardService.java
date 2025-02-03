package board.springboardpractice.api.entity.board.application;

import board.springboardpractice.api.entity.board.domain.Board;
import board.springboardpractice.api.entity.board.dto.response.BoardPostResponse;
import board.springboardpractice.api.entity.board.infrastructure.BoardRepository;
import board.springboardpractice.api.entity.global.Level;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
  private final BoardRepository boardRepository;


  public List<Board> findAll(Level bronze) {
    return boardRepository.findAll(bronze.name());
  }

  public BoardPostResponse save(Board board) {
    Board save = boardRepository.save(board);
    return BoardPostResponse.toResponse(board);
  }
}
