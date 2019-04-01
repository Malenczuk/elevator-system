import ElevatorDirection.ElevatorDirection
import ElevatorStatus.ElevatorStatus

trait ElevatorSystem {
  def step(): Unit

  def status(): Seq[(Int, Int, ElevatorDirection, ElevatorStatus)]

  def update(id: Int, floor: Int, direction: ElevatorDirection): Unit

  def destination(id: Int, destinationFloor: Int): Unit

  def pickup(floor: Int, direction: ElevatorDirection): Unit
}