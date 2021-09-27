# FareEstimation

### NOTE: 
In current implementation, test and code uses same success / skipped files, currently user needs to manually delete success / skipped files else code will just append to existing file.

Also, in case you process same file twice, it will append to existing success / skipped file. Make sure to provide a new file-name or delete existing files before processing a new event file.
This will be fixed soon with "accepting file name from user in command-line parameters" issue. 

## Problem statement
Our drivers perform thousands of rides per day. In order to ensure that our passengers always receive the highest quality of service possible, we need to be able to identify mis-behaving drivers that overcharge passengers and block them from our service.

Our servers keep detailed record of drivers' position throughout the progress of each ride. Due to the high volume of daily rides we need to implement an automated way of estimating the fare for each ride so that we may flag suspicious rides for review by our support teams.

Moreover, our drivers sometimes use low-cost devices that frequently report invalid/skewed GPS coordinates to our servers. The fare estimation algorithm should be capable of detecting erroneous coordinates and remove them before attempting to evaluate the ride fare.

In this exercise you are asked to create a Fare Estimation script. Its input will consist of a list of tuples of the form (id_ride, lat, lng, timestamp) representing the position of the taxi-cab during a ride.

Two consecutive tuples belonging to the same ride form a segment S. For each segment, we define the elapsed time Δt as the absolute difference of the segment endpoint timestamps and the distance covered Δs as the Haversine distance of the segment endpoint coordinates.

Your first task is to filter the data on the input so that invalid entries are discarded before the fare estimation process begins. Filtering should be performed as follows: consecutive tuples p1, p2 should be used to calculate the segment’s speed U. If U > 100km/h, p2 should be removed from the set.

Once the data has been filtered you should proceed in estimating the fare by aggregating the individual estimates for the ride segments using rules tabulated below:

At the start of each ride, a standard ‘flag’ amount of 1.30 is charged to the ride’s fare. The minimum ride fare should be at least 3.47.

### How to calculate fare?
```aidl
If car is moving (U > 10km/h), and time of day between [05:00, 00:00] -> 0.74 per km
If car is moving (U > 10km/h), and time of day between [00:00, 05:00] -> 1.30 per km
If car is moving (U <= 10km/h), it will be considered as idle and 11.90 per hour of idle time
```

## Assumptions:
1. Once the rideId of two consecutive events has changed, it means that previous ride has completed. If same rideId comes back, we will consider it as new ride.
2. FARE IS CURRENTLY CALCULATED BASED ON LAST EVENT TIME (THIS WILL CHANGE IN NEXT IMPLEMENTATION).
   (i.e. if we received an event at 23:45 Hrs and next event at 00:15 Hrs, we will charge complete distance travelled between 23:45 Hrs to 00:15 Hrs with night-time fare,
    Similarly, if we receive event at 04:45 hrs and next event at 05:15 Hrs, we will charge complete distance travelled between 04:45 Hrs to 05:15 Hrs with day-time fare)
3. Events will be separated by span of 1 second at-least else we will skip that event.

## Drawback of current implementations:
1. Change fare calculation logic to be separated eventually over time-difference between two events. Currently, its works on last event time only.
2. Accept file-name of success / event / skipped files from command-line, with current implementation user needs to change path manually and re-build jar everytime. Also, it will help multiple file name problem.
3. FareCalculationUtility should be migrated to MVEL library (rule based fare calculation), Fare calculation logic is expected to change quite often and it could be made dynamic with MVEL library.
4. Dockerize the application.

## High Level Design:
### Reading Events
1. We have created a factory to read events, so as to have multiple implementation, 
   We expect in that in future events will come via a queue rather than CSV as it will allow us to do concurrent processing of separate rides.
   We have provided CSV reader implementation to EventReader Interface in `com.kirtesh.fareestimation.events` package.
   We have read file as Stream (record-by-record) as in case we have too many events, we do not run out of heap memory.
   
2. Processing events
   We have created a workflow for event processing, framework for workflow is in `com.kirtesh,fareestimation.workflow.framework`
   Idea behind work-flow was that we may require more steps (like data-filtering etc) in future. So it gives us a way to make code easy to maintain and we could add changes quickly.
   Whenever we get an event, that event is passed on workflow for processing / filtering / result generation.
   
3. Output file generation
   We are generating 3 output log files,
   3.1. success.csv at path defined in FareEstimate.java containing ride_id and its final cost via Logger.
   3.2. skipped.csv at path defined in log4j.properties containing events, which we skipped or failed to process 
        (In next phase we will also add reason to why we skipped / failed those event against those events in same file as a new column.)
   3.3. app.log in project run directory for debugging purposes.
   

## How to start the application?
###Notes:
1. Instructions / guidelines mentioned below is as per Mac OS. Steps should be executable on other OS, but syntax may differ.

### 1. Using Terminal
    1.1. Prerequisites:
         Make sure you have following sofware /tools installed on your system:
            1.1.1. Java JDK (Recommended version: JDK 11.0.11)
            1.1.2. Maven (Recommended version: 3.8.1)

    1.2. Go to project directory/workspace. Project directory path would be something like: 
    `/Users/<YOUR_SYSTEM_PATH_HERE>/FareEstimation`

    1.3. Intermediate prerequestie (THIS STEP WILL BE DELETED, WE WILL ACCEPT PARAMETER VIA COMMAND LINE): 
        1.3.1. Make sure you have provided correct path to events file for processing in FareEstimation.java class.
        1.3.2. Make sure you have provided correct path to response and skipped files in log4j.properties file at line 25 / 33.

    1.4. In the project directory, execute following commands:
        1.4.1. `mvn clean compile`
        1.4.2. `mvn package`

    1.5. To run the project use command in same directory / workspace:
        1.5.1. `java -jar target/FareEstimation-1.0-jar-with-dependencies.jar`


### 2. Using IntelliJ IDEA
    2.1. Prerequisites:
         Make sure you have IntelliJ to import / open maven based java - maven based projects.
         Make sure you have JDK and Maven plugins installed or you could manually install JDK and Maven.   

    2.2. Once you import the project in IntelliJ, follow the below mentioned steps:
        2.2.1. Intermediate prerequestie (THIS STEP WILL BE DELETED, WE WILL ACCEPT PARAMETER VIA COMMAND LINE):
            2.2.1.1. Make sure you have provided correct path to events file for processing in FareEstimation.java class.
            2.2.1.2. Make sure you have provided correct path to response and skipped files in log4j.properties file at line 25 / 33.

        2.2.2. Go to FareEstimation.java file, and Run FareEstimation class. 
               (FareEstimation is entry point for application)

    2.3. To verify application has successfully started, 
        2.3.1. You will see code will start processing file.

# How to run test?
Use command `mvn test` in terminal to run all test cases.

# Performance:
We could process >400 events per second with single thread.