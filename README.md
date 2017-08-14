# SOFTENG 306 Project 1 [![Build Status](https://travis-ci.com/hbao448/306A1.svg?token=cciKEDpQyfT6yqJyJd58&branch=master)](https://travis-ci.com/hbao448/306A1)

This project is about using artificial intelligence and parallel processing power to solve a difficult scheduling problem.

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Install 
Use Maven to build the project. Using pom.xml, build the project using the **install** goal. The jar file saved to the *target* directory

### Testing
To test that the jar file works, move the example.dot to the target folder and run the jar file

## Usage

### Command Line Interface
````
java −jar scheduler.jar INPUT.dot P [OPTION]
INPUT.dot   A task graph with integer weights in dot format
P           Number of processors to schedule the INPUT graph on

Optional:
−p N        Use N cores for execution in parallel (default is sequential)
−v          Visualise the search
−o OUTPUT   Output file is named OUTPUT (default is INPUT−output.dot)
````

## Collaborators
* Hunter Bao (hbao448) - hbao448@aucklanduni.ac.nz
* Jack Huang (j-x-huang) - xhua451@aucklanduni.ac.nz
* Injae Park (ipar569) - ipar569@aucklanduni.ac.nz
* David Qi (RustedWheel) - yqi955@aucklanduni.ac.nz
* Yao Jian Yap (RemRinRamChi) - yyap601@aucklanduni.ac.nz
* Justin Chuk (hchuk) - hchu167@aucklanduni.ac.nz
