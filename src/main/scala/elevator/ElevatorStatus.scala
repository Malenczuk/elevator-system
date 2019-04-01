package elevator

object ElevatorStatus extends Enumeration {
  type ElevatorStatus = Value
  val EMPTY, OCCUPIED, PICKUP = Value
}
