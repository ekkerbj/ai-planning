import groovy.transform.*

@Immutable class Bank {

  int missionaries = 0
  int cannibals = 0
  boolean has_boat = false

  boolean isValid() {
    if ( missionaries > 3 || missionaries < 0 ) return false
    if ( cannibals > 3 || cannibals < 0 ) return false
    if (( missionaries > 0 ) && ( cannibals > missionaries )) return false
    return true
  }

  @Override String toString() {
    def results = [ "${missionaries}m", "${cannibals}c" ]
    if (has_boat ) results.add('b')
    results.join(',')
  }
}

@Immutable class State {
  Bank left = new Bank()
  Bank right = new Bank()

  def isValid() {
    left.isValid() && right.isValid()
  }

  def successors() {
    def successors = [:]
    def lr_possible_actions = ['1m': new State( left: new Bank(this.left.missionaries-1,this.left.cannibals, false),
                                                right: new Bank(this.right.missionaries+1, this.right.cannibals, true) ),
                               '1c': new State( left: new Bank(this.left.missionaries,this.left.cannibals-1, false),
                                                right: new Bank(this.right.missionaries, this.right.cannibals+1, true) ),
                               '1m1c': new State( left: new Bank(this.left.missionaries-1,this.left.cannibals-1, false),
                                                  right: new Bank(this.right.missionaries+1, this.right.cannibals+1, true) ),
                               '2m': new State( left: new Bank(this.left.missionaries-2,this.left.cannibals, false),
                                                right: new Bank(this.right.missionaries+2, this.right.cannibals, true) ),
                               '2c':new State( left: new Bank(this.left.missionaries,this.left.cannibals-2, false),
                                               right: new Bank(this.right.missionaries, this.right.cannibals+2, true) ) ]

      def rl_possible_actions = ['1m': new State( left: new Bank(this.left.missionaries+1,this.left.cannibals, true),
                                                  right: new Bank(this.right.missionaries-1, this.right.cannibals, false) ),
                                 '1c': new State( left: new Bank(this.left.missionaries,this.left.cannibals+1, true),
                                                  right: new Bank(this.right.missionaries, this.right.cannibals-1, false) ),
                                 '1m1c': new State( left: new Bank(this.left.missionaries+1,this.left.cannibals+1, true),
                                                  right: new Bank(this.right.missionaries-1, this.right.cannibals-1, false) ),
                                 '2m': new State( left: new Bank(this.left.missionaries+2,this.left.cannibals, true),
                                                  right: new Bank(this.right.missionaries-2, this.right.cannibals, false) ),
                                 '2c':new State( left: new Bank(this.left.missionaries,this.left.cannibals+2, true),
                                                  right: new Bank(this.right.missionaries, this.right.cannibals-2, false) ) ]

    if (left.has_boat)
      successors = lr_possible_actions.findAll { action, state -> state.isValid() }
    else
      successors = rl_possible_actions.findAll { action, state -> state.isValid() }

    return successors
  }

  @Override String toString() {
    "(L:$left-R:$right)"
  }
}

@Immutable class Node {

  Node parent = null
  String action = 'start'
  State state

  def successors() {
    return state.successors().collect { action, state -> new Node( action:action, state:state, parent:this ) }
  }

  def pathTo() {
    List<String> results = [this.toString()]
    pathTo( results )
  }

  def pathTo( List<String> results ) {
    if ( parent == null ) return results.reverse()
    results.add( this.toString() )
    parent.pathTo(results)
  }

  @Override String toString() {
    "${parent?.state} -> <$action : $state>"
  }
}

class Problem {
  final State initial_state = new State ( left: new Bank( 3, 3, true ) )
  final State goal_state = new State ( right: new Bank( 3, 3, true ) )

  private def seen = []

  boolean goalTest( def state ) {
    (state == goal_state)
  }

  def expand( def fringe, def node ) {
    if (seen.contains(node.state)) return fringe
    seen.add( node.state )
    (fringe + node.successors()).findAll { n -> !seen.contains(n.state) }
  }
}

@Immutable class FifoStrategy {
  def select( def fringe ) {
    fringe.first()
  }
}

@Immutable class LifoStrategy {
  def select( def fringe ) {
    fringe.last()
  }
}

def solve( def problem, def strategy ) {
  def fringe = [ new Node( state: problem.initial_state ) ]
  while( !fringe.isEmpty() ) {
    def node = strategy.select( fringe )
    if ( problem.goalTest( node.state ) ) return node.pathTo()
    fringe = problem.expand( fringe, node )
  }
  ['******* Unable to find a solution.']
}

def report( def strategy, def solution ) {
  println ""
  println "Solved with $strategy in ${solution.size()} steps: "
  println solution.join('\n')
}

report ( 'Fifo', solve( new Problem(), new FifoStrategy() ) )
report ( 'Lifo', solve( new Problem(), new LifoStrategy() ) )
