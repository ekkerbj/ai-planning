import groovy.transform.*

@Immutable class Board {
  List board
  private static final int ROWS = 3

  def moveLeft() {
    int index_of_space = board.indexOf(0)
    if ( (index_of_space) % ROWS == 0 ) return null
    new Board(new ArrayList(board).swap( index_of_space-1, index_of_space ))
  }

  def moveRight() {
    int index_of_space = board.indexOf(0)
    if ((index_of_space+1) % ROWS == 0 ) return null
    new Board(new ArrayList(board).swap( index_of_space+1, index_of_space ))
  }

  def moveDown() {
    int index_of_space = board.indexOf(0)
    if (index_of_space+ROWS > board.size()-1) return null
    new Board(new ArrayList(board).swap( index_of_space+ROWS, index_of_space ))
  }

  def moveUp() {
    int index_of_space = board.indexOf(0)
    if (index_of_space-ROWS < 0) return null
    new Board(new ArrayList(board).swap( index_of_space-ROWS, index_of_space ))
  }

  @Override String toString() {
    "$board"
  }

  def printBoard() {
    def writer = new StringWriter()
    def printer = new PrintWriter( writer )
    printer.println('')
    board.eachWithIndex { tile, index ->
      if ( (index+1) % ROWS == 0 ) printer.println(tile) else printer.print(tile)
    }
    writer.toString()
  }

  int misplacedTiles(Board from) {
    int misplaced = 0
    from.board.eachWithIndex { tile, index ->
      if (tile != board[index]) misplaced++
    }
    (misplaced == 0) ? 0 : misplaced-1
  }
}
