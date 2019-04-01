import scala.io.StdIn


object Application extends App {
  val pickupCmd = raw"""pickup (\d+) (1|-1)""".r
  val statusCmd = raw"""status""".r
  val updateCmd = raw"""update (\d+) (\d+) (1|-1|0)""".r
  val stepCmd = raw"""step|s""".r
  val destCmd = raw"""dest (\d+) (\d+)""".r

  print("Number of Elevators: ")
  val numberOfElevators = StdIn.readInt()
  print("Start Floor: ")
  val start = StdIn.readInt()
  print("End Floor: ")
  val end = StdIn.readInt()
  val floors = Range(math.min(start, end), math.max(start, end))

  val ecs = new ElevatorControlSystem(numberOfElevators, floors)

  while (true) {
    val line = StdIn.readLine()
    line match {
      case stepCmd() => ecs.step()
      case statusCmd() => println(ecs.status())
      case pickupCmd(floor, direction) => ecs.pickup(floor.toInt, ElevatorDirection(direction.toInt))
      case updateCmd(id, floor, direction) => ecs.update(id.toInt, floor.toInt, ElevatorDirection(direction.toInt))
      case destCmd(id, floor) => ecs.destination(id.toInt, floor.toInt)
      case _ =>
    }
  }
}
