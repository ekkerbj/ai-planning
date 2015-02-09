import groovy.transform.*

@Immutable final class State {
  Board board
  int misplacedTiles

  def successors() {
    def successors = [:]

    def left = board.moveLeft()
    if (left != null ) successors.put( 'left', new State( board:left, misplacedTiles:left.misplacedTiles(Problem.GOAL_BOARD) ))

    def right = board.moveRight()
    if (right != null ) successors.put('right', new State( board:right, misplacedTiles:right.misplacedTiles(Problem.GOAL_BOARD) ))

    def up = board.moveUp()
    if (up != null ) successors.put('up', new State( board:up, misplacedTiles:up.misplacedTiles(Problem.GOAL_BOARD) ))

    def down = board.moveDown()
    if (down != null ) successors.put('down', new State( board:down, misplacedTiles:down.misplacedTiles(Problem.GOAL_BOARD) ))

    successors
  }

  @Override String toString() {
    board
  }
}
