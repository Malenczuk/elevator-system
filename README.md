
# Elevator System
To compile and run use:
```bash 
sbt run
```
After starting program you will be asked to provide number of elevators you want i simulation (min = 1, max = 16) and number of bottom and top floor.   
After entering the configuration you can use this commands to add request, run simulation and show status.   
`pickup [floor] [-1|1]` - add pickup request at [*floor*] with direction [-1 = DOWN | 1 = UP]   
`status` - print out current system status   
`update [id] [floor] [-1|0|1]` - set floor and direction for elevator with given id   
`step | s` - run 1 step of simulation   
`dest [id] [floor]` - add destination floor for elevator with given id   
## Implementation
The Implementation of Elevator System is done i Scala.
There are two main classes `Elevator` and `ElevatorControlSystem`

##### ElevatorControlSystem
Menages up to 16 Elevators and deals with performing the system simulation step. It handles incoming destination request from users inside elevator and request for pickup from outside of elevators.


##### Elevator
Manages on which floor is located, in which direction it moves, the state which it is currently in and all the desired floors entered by the user.

Elevator can be in one of three states:
1. ***`EMPTY`*** - elevator is empty and waiting for pickup request
2. ***`OCCUPIED`*** - elevator is occupied and to destinations requested by users
3. ***`PICKUP`*** - elevator is empty and moving towards pickup floor

Elevator direction can have one of three values:
1. ***`UP`*** - going up
2. ***`HOLD`*** - being held
3. ***`DOWN`*** - going down


### Scheduling algorithm
#### Elevator algorithm
My though on scheduling elevators was to create something similar to SCAN algorithm better know as 'Elevator algorithm':

* Elevators continues to travel in its current direction (up or down) until empty
* Elevators stop only to let individuals off or to pick up new individuals heading in the same direction 

#### Pickup algorithm
When it comes to scheduling pick up requests the algorithm works as followed:

* pick the nearest out of two:
    1. If idling elevators are available, pick nearest elevator located to the pickup location
    2. If elevators on the same way are available, pick nearest of them 
* If non above is available request is postponed after simulation step

This algorithm isn't perfect and can be further optimized

#### First Come-First Serve comparison
Lets take for example this queue `[13, 8, 20, 2, 37, 5, 39, 15, 29, 16]`, we start at floor 13 and if we look at the path of elevator using FCFS algorithm.   
![Alt](/images/FCFS.JPG "FCFS")

The gist of this algorithm is that all incoming requests are placed at the end of the queue. Whatever number that is next in the queue will be the next number served.   
Now lets look how will it look using SCAN algorithm.   
![Alt](/images/SCAN.JPG "SCAN")

It is easy to say that SCAN is doing better, maybe that's why it's used in elevators these days. The overall distance that elevator has to travel is shorter when we compare SCAN to FCFS making it more efficient. This was the reason why I decided to use SCAN algorithm for my elevator system. 
