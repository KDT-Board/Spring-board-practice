package board.springboardpractice.dto;

@Data
@AllArgsConstructor
public class BoardSearchRequest {

  private String sortType;
  private String searchType;
  private String keyword;
}
