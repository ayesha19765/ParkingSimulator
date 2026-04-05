# Variables
PROJECT_DIR := $(CURDIR)
SRC_DIR := $(PROJECT_DIR)/src/main/java/com/example/ParkingSimulator
MAIN_CLASS := com.example.ParkingSimulator.Main
JAR_FILE := target/ParkingSystem.jar

# Check if Maven is installed
MVN := $(shell command -v mvn 2> /dev/null)

# Check if Java is installed
JAVA := $(shell command -v java 2> /dev/null)
JAVAC := $(shell command -v javac 2> /dev/null)

# Default target
all: check-requirements compile package

# Check requirements
check-requirements:
ifndef MVN
	@echo "Maven is not installed. Installing Maven..."
	sudo apt-get update && sudo apt-get install -y maven
else
	@echo "Maven is already installed."
endif

ifndef JAVA
	@echo "Java is not installed. Installing Java..."
	sudo apt-get update && sudo apt-get install -y default-jdk
else
	@echo "Java is already installed."
endif

# Compile the project using Maven
compile:
	@echo "Compiling the project..."
	mvn clean compile

# Package the project into a JAR file
package: compile
	@echo "Packaging the project..."
	mvn package

# Clean target
clean:
	@echo "Cleaning up..."
	mvn clean

# Run the application
run: package
	@echo "Running application..."
	mvn javafx:run -DmainClass=$(MAIN_CLASS)

.PHONY: all check-requirements compile package clean run
