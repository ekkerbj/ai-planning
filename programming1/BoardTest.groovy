import org.junit.*

public class BoardTest
{
  @Test
  public void canCheckEqual() {
    def board = new Board([0,1,2,3,4,5,6,7,8])
    assert board == new Board([0,1,2,3,4,5,6,7,8])
  }

  @Test public void canCheckNotEqual() {
    def board = new Board([0,1,2,3,4,5,6,7,8])
    assert board != new Board([1,0,2,3,4,5,6,7,8])
  }

  @Test public void canFindMisplacedTiles() {
    def board = new Board([0,1,2,3,4,5,6,7,8])
    assert 8 == new Board([7,2,4,5,0,6,8,3,1]).misplacedTiles( board )
  }

  /*
  @Test public void canFindManhattanDistance() {
    def board =  new Board([0,1,2,3,4,5,6,7,8])
    assert 18 == new Board([7,2,4,5,0,6,8,3,1]).manhattanDistance( board )
    }*/

  @Test public void cannotMoveLeft() {
    assert new Board([0,1,1,1,1,1,1,1,1]).moveLeft() == null
    assert new Board([1,1,1,0,1,1,1,1,1]).moveLeft() == null
    assert new Board([1,1,1,1,1,1,0,1,1]).moveLeft() == null
  }

  @Test public void canMoveLeft() {
    assert new Board([1,0,1,1,1,1,1,1,1]).moveLeft() == new Board([0,1,1,1,1,1,1,1,1])
    assert new Board([1,1,0,1,1,1,1,1,1]).moveLeft() == new Board([1,0,1,1,1,1,1,1,1])
    assert new Board([1,1,1,1,0,1,1,1,1]).moveLeft() == new Board([1,1,1,0,1,1,1,1,1])
    assert new Board([1,1,1,1,1,0,1,1,1]).moveLeft() == new Board([1,1,1,1,0,1,1,1,1])
    assert new Board([1,1,1,1,1,1,1,0,1]).moveLeft() == new Board([1,1,1,1,1,1,0,1,1])
    assert new Board([1,1,1,1,1,1,1,1,0]).moveLeft() == new Board([1,1,1,1,1,1,1,0,1])
  }

  @Test public void cannotMoveRight() {
    assert new Board([1,1,0,1,1,1,1,1,1]).moveRight() == null
    assert new Board([1,1,1,1,1,0,1,1,1]).moveRight() == null
    assert new Board([1,1,1,1,1,1,1,1,0]).moveRight() == null
  }

  @Test public void canMoveRight() {
    assert new Board([0,1,1,1,1,1,1,1,1]).moveRight() == new Board([1,0,1,1,1,1,1,1,1])
    assert new Board([1,0,1,1,1,1,1,1,1]).moveRight() == new Board([1,1,0,1,1,1,1,1,1])
    assert new Board([1,1,1,0,1,1,1,1,1]).moveRight() == new Board([1,1,1,1,0,1,1,1,1])
    assert new Board([1,1,1,1,0,1,1,1,1]).moveRight() == new Board([1,1,1,1,1,0,1,1,1])
    assert new Board([1,1,1,1,1,1,0,1,1]).moveRight() == new Board([1,1,1,1,1,1,1,0,1])
    assert new Board([1,1,1,1,1,1,1,0,1]).moveRight() == new Board([1,1,1,1,1,1,1,1,0])
  }

  @Test public void cannotMoveUp() {
    assert new Board([0,1,1,1,1,1,1,1,1]).moveUp() == null
    assert new Board([1,0,1,1,1,1,1,1,1]).moveUp() == null
    assert new Board([1,1,0,1,1,1,1,1,1]).moveUp() == null
  }

  @Test public void canMoveUp() {
    assert new Board([1,1,1,0,1,1,1,1,1]).moveUp() == new Board([0,1,1,1,1,1,1,1,1])
    assert new Board([1,1,1,1,0,1,1,1,1]).moveUp() == new Board([1,0,1,1,1,1,1,1,1])
    assert new Board([1,1,1,1,1,0,1,1,1]).moveUp() == new Board([1,1,0,1,1,1,1,1,1])
    assert new Board([1,1,1,1,1,1,0,1,1]).moveUp() == new Board([1,1,1,0,1,1,1,1,1])
    assert new Board([1,1,1,1,1,1,1,0,1]).moveUp() == new Board([1,1,1,1,0,1,1,1,1])
    assert new Board([1,1,1,1,1,1,1,1,0]).moveUp() == new Board([1,1,1,1,1,0,1,1,1])
  }

  @Test public void cannotMoveDown() {
    assert new Board([1,1,1,1,1,1,0,1,1]).moveDown() == null
    assert new Board([1,1,1,1,1,1,1,0,1]).moveDown() == null
    assert new Board([1,1,1,1,1,1,1,1,0]).moveDown() == null
  }

  @Test public void canMoveDown() {
    assert new Board([0,1,1,1,1,1,1,1,1]).moveDown() == new Board([1,1,1,0,1,1,1,1,1])
    assert new Board([1,0,1,1,1,1,1,1,1]).moveDown() == new Board([1,1,1,1,0,1,1,1,1])
    assert new Board([1,1,0,1,1,1,1,1,1]).moveDown() == new Board([1,1,1,1,1,0,1,1,1])
    assert new Board([1,1,1,0,1,1,1,1,1]).moveDown() == new Board([1,1,1,1,1,1,0,1,1])
    assert new Board([1,1,1,1,0,1,1,1,1]).moveDown() == new Board([1,1,1,1,1,1,1,0,1])
    assert new Board([1,1,1,1,1,0,1,1,1]).moveDown() == new Board([1,1,1,1,1,1,1,1,0])
  }
}
