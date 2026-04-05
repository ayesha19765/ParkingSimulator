# Parking Simulator

### A Java-based Parking Simulator application using JavaFX for the graphical user interface and a customizable parking strategy. This project simulates a parking lot where cars attempt to park using different synchronization strategies (Semaphore or Mutex).

## Features:

* Customizable Parking Spot Display: Uses CustomRectangle class to represent parking spots with customizable colors and labels.
* Synchronization Strategies: Implements different synchronization strategies (Semaphore and Mutex) through the IStrategy interface and DefaultStrategy class.
* Interactive GUI: JavaFX-based GUI that prompts the user for the number of cars and parking spots, and displays the parking status in a grid layout.
* Logging: Logs parking attempts and waiting times to a file.
* Simulation and Animation: Simulates car parking using pauses and animations.
* Thread Management: Program oriented to thread management, where a critical section (parking) is managed using Mutex for synchronization.

## Project Structure:

* CustomRectangle.java: A custom JavaFX component representing a parking spot.
* DefaultStrategy.java: Implements the default parking strategy to find the next available spot.
* IStrategy.java: Interface for different parking strategies.
* Main.java: Main application class that sets up the JavaFX interface and starts the simulation.
* Parking.java: Manages parking spots and synchronization strategies.
* Voiture.java: Represents a car trying to park in the parking lot.


## Thread Management and Critical Section
The application is designed to manage multiple threads representing cars trying to park simultaneously. The critical section (parking operation) is protected using Mutex or Semaphore to ensure thread safety and prevent race conditions. This will avoid conflicts and ensure consistent state updates.

## Graphical User Interface (GUI):
* Grid Layout: The parking spots are displayed in a grid layout using JavaFX's GridPane. Each spot is represented by a CustomRectangle which can change color based on availability (green for available, grey for unavailable).
* User Prompts: On startup, the application prompts the user to input the number of cars and parking spots, and to choose a synchronization strategy.
* Statistics Button: A button labeled "Afficher la moyenne d'attente" allows the user to view the average waiting time for cars, calculated from the log file.

## How to Run :
You can directly use your IDE or:

Clone the repository:
```
git clone https://github.com/boussas/ParkingSimulator-Java.git
cd ParkingSimulator-Java
```
Build and run the project using Make: 
```
make all
make run
```

## Usage:
Upon running the application, you will be prompted to:

  1. Select a synchronization strategy (Semaphore or Mutex).
  2. Enter the number of cars.
  3. Enter the number of parking spots.

The application will then display a grid representing the parking lot, with cars attempting to park at random intervals.
You can view the average waiting time for cars by clicking the "Afficher la moyenne d'attente" button.

## Logging:
The application logs parking attempts and waiting times to app.log.
* Log Analysis:
The Analyze method reads the log file and calculates the average waiting time for cars. It displays detailed information about each car's waiting time in a dialog box.

# Synchronization Strategies:

## Semaphore:

A Semaphore is a more generalized synchronization primitive that can control access to a resource pool with multiple permits. It allows more than one thread to access a resource concurrently, up to a specified limit.
### Benefits:
  * Scalability: Semaphores can manage access to multiple resources simultaneously, making them suitable for scenarios where limited concurrency is desired.
  * Flexibility: They can be used to control access to a pool of resources, not just a single resource.
  * Fairness: Semaphores can be configured to ensure fair access to resources, preventing thread starvation.
  * Non-Binary: Unlike mutexes, semaphores can have more than two states (locked/unlocked), allowing for more nuanced control over resource access.

### Trade-offs:

  * Complexity: Managing semaphores can be more complex compared to mutexes, especially when dealing with multiple permits and ensuring correct usage patterns.

## Mutex

A Mutex (short for mutual exclusion) is a locking mechanism used to enforce exclusive access to a resource. It ensures that only one thread can access a critical section of code at any given time.
### Benefits

  * Simplicity: Mutexes provide a straightforward mechanism for ensuring exclusive access to resources, making them easy to implement and understand.
  * Efficiency: They typically have lower overhead compared to semaphores since they only need to manage two states (locked/unlocked).
  * Deterministic: Mutexes ensure that only one thread can enter the critical section, providing deterministic behavior for critical resource access.
  * Deadlock Prevention: Properly used mutexes can prevent deadlocks by ensuring that a thread holds only one lock at a time.

### Trade-offs:

  * Limited Concurrency: Mutexes enforce strict mutual exclusion, which can limit concurrency in scenarios where limited parallel access might be acceptable.
  * Potential for Deadlock: If not used correctly (e.g., if a thread tries to acquire a mutex it already holds), mutexes can lead to deadlocks.
  * Starvation: Without proper handling, some threads might starve if they are constantly preempted by other threads acquiring the mutex.

## Use Case in Parking Simulator:

### Semaphore in Parking Simulator:

Using a semaphore in the parking simulator could allow multiple cars to attempt parking simultaneously, up to the number of available parking spots. Each car would acquire a permit before parking and release it upon leaving.

#### Pros:

  * Higher Throughput: Multiple cars can park concurrently, potentially increasing the overall throughput of the parking lot.
  * Resource Utilization: Better utilization of available parking spots, as cars can park as long as there are permits available.

#### Cons:

   * Complexity in Management: Managing the correct acquisition and release of permits can be more complex.
   * Race Conditions: Increased risk of race conditions if the semaphore is not managed correctly.

### Mutex in Parking Simulator:

Using a mutex would ensure that only one car can attempt to park at any given time, simplifying the critical section management.
#### Pros:

   * Simplicity: Easier to implement and understand, reducing the risk of concurrency issues.
   * Deterministic Access: Ensures that only one car can park at a time, making state management straightforward.

#### Cons:

   * Limited Concurrency: Only one car can park at a time, potentially reducing throughput.
   * Potential for Bottlenecks: Can create a bottleneck if many cars are trying to park simultaneously, leading to increased waiting times.

## Conclusion:

The choice between Semaphore and Mutex in the parking simulator depends on your specific requirements and constraints:

  * Use Semaphore if you need to allow multiple cars to park concurrently and can manage the complexity of permits.
  * Use Mutex if you prefer a simpler, deterministic approach with exclusive access to the parking process, even at the cost of limited concurrency.

Each approach has its own set of trade-offs, and the best choice will depend on the desired balance between concurrency, simplicity, and resource management.

Classes Path: src/main/java/com/example/ParkingSimulator
