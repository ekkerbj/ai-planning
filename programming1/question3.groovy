import groovy.transform.*
import java.util.concurrent.*

class Problem {
  public final static Board INITIAL_BOARD    = new Board([0, 1, 2, 3, 4, 5, 6, 7, 8])
  public final static Board GOAL_BOARD = new Board([1, 6, 4, 8, 7, 0, 3, 2, 5])

  int counter = 0

  final State initial_state = new State ( INITIAL_BOARD, INITIAL_BOARD.misplacedTiles(GOAL_BOARD) )
  final State goal_state = new State ( GOAL_BOARD, 0 )

  private Set seen = new HashSet()

  boolean goalTest( def state ) {
    (state == goal_state)
  }

  def expand( def fringe, def node ) {
    if (seen.contains(node.state)) return fringe
    seen.add( node.state )
    if (node.depth==27) counter++
    fringe.addAll( node.successors().findAll{ n -> !seen.contains(n.state) } )
    fringe
  }
}

def solve( def problem, def strategy ) {
  def fringe = new PriorityQueue<SearchNode>(50000, strategy)
  fringe.add( new SearchNode( state: problem.initial_state ) )
  int counter = 1
  while( !fringe.isEmpty() ) {
    def node = fringe.poll()
    fringe = problem.expand( fringe, node )
    counter++
  }
  println "${problem.counter} answers in 27 steps."
  println "Spun for $counter iterations."
}

solve( new Problem(), { a, b -> a.state.misplacedTiles+a.depth <=> b.state.misplacedTiles+b.depth } )
