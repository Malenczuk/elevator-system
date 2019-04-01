package elevator

import elevator.ElevatorDirection.{DOWN, ElevatorDirection, HOLD, UP, compareFloors}
import elevator.ElevatorStatus.{EMPTY, ElevatorStatus, OCCUPIED, PICKUP}

import scala.collection.mutable.ListBuffer

class Elevator(val id: Int, var floor: Int) {
  private val destinations: ListBuffer[Int] = ListBuffer[Int]()
  private var _direction: ElevatorDirection = HOLD
  private var _status: ElevatorStatus = EMPTY
  private var currentPickup: (Int, ElevatorDirection) = _

  def status: ElevatorStatus = _status

  def addDestination(destinationFloor: Int): Unit = {
    if (checkIfDestinationNotExists(destinationFloor)) destinations += destinationFloor
  }

  def checkIfDestinationNotExists(destinationFloor: Int): Boolean = {
    !checkIfDestinationExists(destinationFloor)
  }

  def checkIfDestinationExists(destinationFloor: Int): Boolean = {
    destinations.contains(destinationFloor)
  }

  def pickup(pickupData: (Int, ElevatorDirection)): Unit = {
    currentPickup = pickupData
    _status = PICKUP
  }

  def checkIfOnTheWay(pickupFloor: Int, pickupDirection: ElevatorDirection): Boolean = (_direction, _status) match {
    case (DOWN, OCCUPIED) => pickupDirection == _direction && checkIfInBetweenFloors(destinations.min, pickupFloor, floor)
    case (UP, OCCUPIED) => pickupDirection == _direction && checkIfInBetweenFloors(floor, pickupFloor, destinations.max)
    case _ => false
  }

  def checkIfInBetweenFloors(lowerFloor: Int, floor: Int, higherFloor: Int): Boolean = {
    lowerFloor <= floor && floor <= higherFloor
  }

  def checkIfEmpty(): Boolean = _status == EMPTY

  def checkIfOccupied(): Boolean = _status == OCCUPIED

  def checkIfPickup(pickupFloor: Int, pickupDirection: ElevatorDirection): Boolean = {
    checkIfPickup() && currentPickup == (pickupFloor, pickupDirection)
  }

  def checkIfPickup(): Boolean = _status == PICKUP

  def update(floor: Int, direction: ElevatorDirection): Unit = {
    this.floor = floor
    this._direction = direction
  }

  def step(): Unit = {
    move(_direction)
    _direction = nextDirection
  }

  def move(direction: ElevatorDirection): Unit = direction match {
    case DOWN => moveDOWN()
    case UP => moveUP()
    case HOLD =>
  }

  def moveUP(): Unit = floor += 1

  def moveDOWN(): Unit = floor -= 1

  private def nextDirection: ElevatorDirection = _status match {
    case EMPTY => emptyDirection
    case OCCUPIED => occupiedDirection
    case PICKUP => pickupDirection
  }

  private def emptyDirection: ElevatorDirection = {
    if (destinations.nonEmpty) {
      _status = OCCUPIED
      compareFloor(destinations.head)
    } else HOLD
  }

  private def occupiedDirection: ElevatorDirection = {
    destinations -= floor
    if (destinations.exists(f => compareFloor(f) == _direction)) {
      _direction
    } else {
      _status = EMPTY
      HOLD
    }
  }

  private def pickupDirection: ElevatorDirection = {
    if (floor == currentPickup._1) _status = OCCUPIED
    compareFloor(currentPickup._1)
  }

  def compareFloor(f: Int): ElevatorDirection = compareFloors(floor, f)

  def distanceToFloor(f: Int): Int = math.abs(floor - f)

  override def toString = s"(ID: $id, Floor: $floor, $direction)"

  def direction: ElevatorDirection = _direction
}

object Elevator {
  private var id = -1

  def apply(floor: Int): Elevator = {
    id += 1
    new Elevator(id, floor)
  }
}