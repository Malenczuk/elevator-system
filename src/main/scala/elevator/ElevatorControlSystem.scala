package elevator

import elevator.ElevatorDirection.ElevatorDirection
import elevator.ElevatorStatus.ElevatorStatus

import scala.collection.mutable.ListBuffer

class ElevatorControlSystem(numberOfElevators: Int, floors: Range) extends ElevatorSystem {
  private val elevators: IndexedSeq[Elevator] = {
    for (_ <- 0 until math.min(math.max(numberOfElevators, 1), 16)) yield Elevator(floors.head)
  }
  private val pickups: ListBuffer[(Int, ElevatorDirection)] = new ListBuffer[(Int, ElevatorDirection)]

  override def step(): Unit = {
    elevators.foreach(_.step())
    pickups.foreach(pickBestElevator)
  }

  override def status(): Seq[(Int, Int, ElevatorDirection, ElevatorStatus)] = {
    elevators.map(e => (e.id, e.floor, e.direction, e.status))
  }

  override def update(id: Int, floor: Int, direction: ElevatorDirection): Unit = {
    if (checkIfValidData(floor, direction))
      elevators.find(_.id == id).foreach(_.update(floor, direction))
  }

  override def destination(id: Int, destinationFloor: Int): Unit = {
    if (checkIfValidFloor(destinationFloor))
      elevators.find(_.id == id).foreach(_.addDestination(destinationFloor))
  }

  override def pickup(pickupFloor: Int, pickupDirection: ElevatorDirection): Unit = {
    val pickupData = (pickupFloor, pickupDirection)
    if (checkIfValidData(pickupFloor, pickupDirection)) {
      if (checkIfPickupNotExists(pickupData)) {
        pickups += pickupData
        pickBestElevator(pickupFloor, pickupDirection)
      }
    }
  }

  def checkIfValidData(floor: Int, direction: ElevatorDirection): Boolean = {
    checkIfValidFloor(floor) && checkIfValidDirection(floor, direction)
  }

  def checkIfValidDirection(floor: Int, direction: ElevatorDirection): Boolean = {
    if (direction == ElevatorDirection.HOLD) true
    else if (floor == floors.head)
      direction == ElevatorDirection.compareFloors(floor, floors.last)
    else if (floor == floors.last)
      direction == ElevatorDirection.compareFloors(floor, floors.head)
    else
      true
  }

  def checkIfValidFloor(floor: Int): Boolean = {
    floors.contains(floor)
  }

  private def pickBestElevator(pickupData: (Int, ElevatorDirection)): Unit = {
    if (!checkIfResolvedPickup(pickupData)) {
      val onTheWayElevator = findNearestOnTheWay(pickupData)
      val idlingElevator = findNearestIdling(pickupData)

      (onTheWayElevator, idlingElevator) match {
        case (None, Some(idling)) => pickEmpty(idling, pickupData)
        case (Some(onTheWay), None) => pickOnTheWay(onTheWay, pickupData)
        case (Some(onTheWay), Some(idling)) =>
          if (onTheWay.distanceToFloor(pickupData._1) <= idling.distanceToFloor(pickupData._1))
            pickOnTheWay(onTheWay, pickupData)
          else
            pickEmpty(idling, pickupData)
        case _ =>
      }
    }
    if (checkIfResolvedPickup(pickupData)) pickups -= pickupData
  }

  private def checkIfResolvedPickup(pickupData: (Int, ElevatorDirection)): Boolean = {
    checkIfSummoned(pickupData) || checkIfOnItsWay(pickupData)
  }

  private def checkIfSummoned(pickupData: (Int, ElevatorDirection)): Boolean = {
    elevators.exists(_.checkIfPickup(pickupData._1, pickupData._2))
  }

  private def checkIfOnItsWay(pickupData: (Int, ElevatorDirection)): Boolean = {
    elevators.exists(e => e.checkIfDestinationExists(pickupData._1) && e.direction == pickupData._2)
  }

  private def findNearestOnTheWay(pickupData: (Int, ElevatorDirection)): Option[Elevator] = {
    val onTheWayElevators = elevators.filter(_.checkIfOnTheWay(pickupData._1, pickupData._2))
    findNearest(onTheWayElevators, pickupData._1)
  }

  private def findNearest(elevators: Seq[Elevator], pickupFloor: Int): Option[Elevator] = {
    elevators.sortBy(_.distanceToFloor(pickupFloor)).headOption
  }

  private def findNearestIdling(pickupData: (Int, ElevatorDirection)): Option[Elevator] = {
    val idlingElevators = elevators.filter(_.checkIfEmpty())
    findNearest(idlingElevators, pickupData._1)
  }

  private def pickOnTheWay(onTheWayElevator: Elevator, pickupData: (Int, ElevatorDirection)): Unit = {
    onTheWayElevator.addDestination(pickupData._1)
  }

  private def pickEmpty(idlingElevator: Elevator, pickupData: (Int, ElevatorDirection)): Unit = {
    idlingElevator.pickup(pickupData)
  }

  private def checkIfPickupNotExists(pickupData: (Int, ElevatorDirection)): Boolean = {
    !pickups.contains(pickupData)
  }
}
