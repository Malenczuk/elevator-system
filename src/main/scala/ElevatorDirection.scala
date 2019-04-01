object ElevatorDirection extends Enumeration(-1) {
  type ElevatorDirection = Value
  val DOWN, HOLD, UP = Value

  def compareFloors(f1: Int, f2: Int): ElevatorDirection = {
    if (f1 < f2) UP else if (f1 > f2) DOWN else HOLD
  }
}
