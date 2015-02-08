import groovy.transform.*
import java.util.concurrent.*

@Immutable class Board {
  List board
  private static final int ROWS = 3

  def moveLeft() {
    def new_board = new ArrayList(board)
    int index_of_space = new_board.indexOf(0)
    if ( (index_of_space) % ROWS == 0 ) return null
    new Board(new_board.swap( index_of_space-1, index_of_space ))
  }

  def moveRight() {
    def new_board = new ArrayList(board)
    int index_of_space = new_board.indexOf(0)
    if ((index_of_space+1) % ROWS == 0 ) return null
    new Board(new_board.swap( index_of_space+1, index_of_space ))
  }

  def moveDown() {
    def new_board = new ArrayList(board)
    int index_of_space = new_board.indexOf(0)
    if (index_of_space+ROWS > board.size()-1) return null
    new Board(new_board.swap( index_of_space+ROWS, index_of_space ))
  }

  def moveUp() {
    def new_board = new ArrayList(board)
    int index_of_space = new_board.indexOf(0)
    if (index_of_space-ROWS < 0) return null
    new Board(new_board.swap( index_of_space-ROWS, index_of_space ))
  }

  @Override String toString() {
    def writer = new StringWriter()
    def printer = new PrintWriter( writer )
    printer.println('')
    board.eachWithIndex { tile, index ->
      if ( (index+1) % ROWS == 0 ) printer.println(tile) else printer.print(tile)
    }
    writer.toString()
  }

  int misplacedTiles() {
    int misplaced = 0
    Problem.GOAL_BOARD.board.eachWithIndex { tile, index ->
      if (tile != board[index]) misplaced++
    }
    (misplaced == 0) ? 0 : misplaced-1
  }
}

@Immutable final class State {
  Board board
  int misplacedTiles

  def successors() {
    def successors = [:]

    def left = board.moveLeft()
    if (left != null ) successors.put( 'left', new State( board:left, misplacedTiles:left.misplacedTiles() ))

    def right = board.moveRight()
    if (right != null ) successors.put('right', new State( board:right, misplacedTiles:right.misplacedTiles() ))

    def up = board.moveUp()
    if (up != null ) successors.put('up', new State( board:up, misplacedTiles:up.misplacedTiles() ))

    def down = board.moveDown()
    if (down != null ) successors.put('down', new State( board:down, misplacedTiles:down.misplacedTiles() ))

    successors
  }

  @Override String toString() {
    board
  }
}

@Immutable class Node {
  Node parent = null
  String action = 'start'
  State state
  int depth=0

  def successors() {
    return state.successors().collect { action, state -> new Node( action:action, state:state, parent:this, depth:this.depth+1 ) }
  }

  def pathTo( List<String> results = []) {
    if ( parent == null ) return results.reverse()
    results.add( this.toString() )
    parent.pathTo(results)
  }

  @Override String toString() {
    "${parent?.state} move $action $state"
  }
}

class Problem {
  public final static Board GOAL_BOARD    = new Board([0, 1, 2, 3, 4, 5, 6, 7, 8])
  public final static Board INITIAL_BOARD = new Board([1, 6, 4, 8, 7, 0, 3, 2, 5])
  final State initial_state = new State ( INITIAL_BOARD, INITIAL_BOARD.misplacedTiles() )
  final State goal_state = new State ( GOAL_BOARD, 0 )

  private Set seen = new HashSet()

  boolean goalTest( def state ) {
    (state == goal_state)
  }

  def expand( def fringe, def node ) {
    if (seen.contains(node.state)) return fringe
    seen.add( node.state )
    fringe.addAll( node.successors().findAll{ Node n -> !seen.contains(n.state) } )
    fringe
  }
}

def solve( def problem, def strategy ) {
  def fringe = new PriorityQueue<Node>(strategy)
  fringe.add( new Node( state: problem.initial_state ) )
  while( !fringe.isEmpty() ) {
    def node = fringe.poll()
    if ( problem.goalTest( node.state ) ) return node.pathTo()
    fringe = problem.expand( fringe, node )
  }
  ["******* Unable to find a solution."]
}

def report( def solution, def strategy ) {
  println ""
  println "Completed with $strategy in ${solution.size()} steps: "
  println solution.join('\n')
}

report ( solve( new Problem(), { a, b -> a.state.misplacedTiles <=> b.state.misplacedTiles } ), 'MisplacedTiles' )
