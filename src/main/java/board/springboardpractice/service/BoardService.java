package board.springboardpractice.service;

import board.springboardpractice.domain.Board;
import board.springboardpractice.domain.user.User;
import board.springboardpractice.dto.req.BoardRequestDto;
import board.springboardpractice.repository.BoardRepository;
import board.springboardpractice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
  private final BoardRepository boardRepository;
  private final UserRepository userRepository;

  public void save(BoardRequestDto boardRequestDto, String loginId) throws Exception {
    User userFound = userRepository.findByLoginId(loginId)
            .orElseThrow(Exception::new);
    Board newBoard = Board.of(boardRequestDto.getTitle(), boardRequestDto.getBody(),userFound);
    boardRepository.save(newBoard);
  }

  public List<Board> getAllBoards( String loginId)throws Exception {
    User userFound = userRepository.findByLoginId(loginId)
            .orElseThrow(Exception::new);
    return boardRepository.findAll();
  }
}
