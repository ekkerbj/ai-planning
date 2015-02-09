import groovy.transform.*

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
