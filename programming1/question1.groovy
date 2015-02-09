import groovy.transform.*
import java.util.concurrent.*

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
    fringe.addAll( node.successors().findAll{ SearchNode n -> !seen.contains(n.state) } )
    fringe
  }
}

def solve( def problem, def strategy ) {
  def fringe = new PriorityQueue<SearchNode>(1000, strategy)
  fringe.add( new SearchNode( state: problem.initial_state ) )
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

report ( solve( new Problem(), { a, b -> a.state.misplacedTiles <=> b.state.misplacedTiles } ), 'Greedy Best-First' )
report ( solve( new Problem(), { a, b -> a.state.misplacedTiles+a.depth <=> b.state.misplacedTiles+b.depth } ), 'A*' )
