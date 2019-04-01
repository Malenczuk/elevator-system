# Elevator System

# Implementation
The Implementation is done i Scala. There are two main classes Elevator and ElevatorControlSystem
## Scheduling algorithm
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