import groovy.transform.*
import java.util.concurrent.*

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
  final State initial_state = new State ( INITIAL_BOARD, INITIAL_BOARD.misplacedTiles(GOAL_BOARD) )
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
