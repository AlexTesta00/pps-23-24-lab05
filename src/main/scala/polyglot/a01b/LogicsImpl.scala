package polyglot.a01b

import polyglot.CellInGame
import util.Sequences.Sequence

//Svolto da solo
trait Point2D:
  def x: Int
  def y: Int

object Point2D:
  def apply(x: Int, y: Int): Point2D = Point2DImpl(x, y)

  private case class Point2DImpl(override val x: Int, override val y: Int)
      extends Point2D

trait Cell:
  def position: Point2D
  def isMine: Boolean
  def isRevealed: Boolean
  def isFlagged: Boolean
  def reveal(): Unit
  def flag(): Unit
  def isNeighbourOf(cell: Cell): Boolean

object Cell:
  def apply(
      position: Point2D,
      isMine: Boolean,
      isRevealed: Boolean,
      isFlagged: Boolean
  ): Cell =
    CellImpl(position, isMine, isRevealed = false, isFlagged = false)

  private case class CellImpl(
      override val position: Point2D,
      override val isMine: Boolean,
      var isRevealed: Boolean,
      var isFlagged: Boolean
  ) extends Cell:
    override def reveal(): Unit = isRevealed = true

    override def flag(): Unit = isFlagged = !isFlagged

    override def isNeighbourOf(cell: Cell): Boolean =
      !(position == cell.position) &&
        Math.abs(position.x - cell.position.x) <= 1 && Math.abs(
          position.y - cell.position.y
        ) <= 1

//Factory
object GameCell:
  def mine(position: Point2D): Cell =
    Cell(position, isMine = true, isRevealed = false, isFlagged = false)
  def default(position: Point2D): Cell =
    Cell(position, isMine = false, isRevealed = false, isFlagged = false)

trait Board:

  def board: Sequence[Cell]
  def minesPosition(): Sequence[Point2D]
  def allNeighboursOf(x: Int, y: Int): Sequence[Cell]
  def cellInfo(x: Int, y: Int): CellInGame

object Board:
  def apply(size: Int, mines: Int): Board = GameBoard(size, mines)

  private case class GameBoard(size: Int, mines: Int) extends Board:

    private val minesPositions: Sequence[Point2D] = randomPosition(mines)

    private def randomPosition(numberOfMines: Int): Sequence[Point2D] =
      val random = scala.util.Random
      var positions = Sequence.empty[Point2D]
      while positions.size() <= numberOfMines - 1 do
        val point = Point2D(random.nextInt(size), random.nextInt(size))
        if !positions.contains(point) then
          positions = positions.concat(Sequence.apply(point))
      positions

    private val gameBoard: Sequence[Cell] =
      var cells = Sequence.empty[Cell]
      0 until size map (x =>
        0 until size map (y =>
          if minesPositions.contains(Point2D(x, y)) then
            cells = cells.concat(Sequence.apply(GameCell.mine(Point2D(x, y))))
          else
            cells =
              cells.concat(Sequence.apply(GameCell.default(Point2D(x, y))))
        )
      )
      cells

    override def minesPosition(): Sequence[Point2D] = minesPositions

    override def allNeighboursOf(x: Int, y: Int): Sequence[Cell] =
      board.filter(_.isNeighbourOf(GameCell.default(Point2D(x, y))))

    override def cellInfo(x: Int, y: Int): CellInGame =
      val cell = board.find(_.position == Point2D(x, y)).get()
      val neighbours = allNeighboursOf(cell.position.x, cell.position.y)
        .filter(_.isMine)
        .size()
      CellInGame(cell.isRevealed, cell.isFlagged, cell.isMine, neighbours)

    override def board: Sequence[Cell] = gameBoard

class LogicsImpl(private val size: Int, private val mines: Int) extends Logics:

  private val minesBoard: Board = Board(size, mines)

  override def hit(x: Int, y: Int): Unit =
    val currentPosition = Point2D(x, y)
    val neighbours = minesBoard.allNeighboursOf(x, y)
    val currentCell = minesBoard.board.find(_.position == currentPosition).get()
    if !currentCell.isFlagged then
      currentCell.reveal()
      minesBoard.board
        .filter(c => neighbours.contains(c) && !c.isMine)
        .map(_.reveal())

  override def flagCell(x: Int, y: Int): Unit =
    minesBoard.board.map(c => if c.position == Point2D(x, y) then c.flag())
  override def won(): Boolean =
    minesBoard.board.filter(c => !c.isMine && !c.isRevealed).size() == 0

  override def lost(): Boolean =
    minesBoard.board.filter(c => c.isMine && c.isRevealed).size() != 0

  override def getCell(x: Int, y: Int): CellInGame = minesBoard.cellInfo(x, y)
