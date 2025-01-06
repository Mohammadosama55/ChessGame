 # Chess Game

Welcome to the Chess Game project! This is a web-based chess game developed using Java technologies. The project is built using JSP (JavaServer Pages), JSTL (JavaServer Pages Standard Tag Library), EL (Expression Language), Servlets, MySQL for database management, and JDBC for database connectivity. JUnit is used for writing test cases. The project is developed in the Eclipse IDE.

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Setup and Installation](#setup-and-installation)
- [Database Configuration](#database-configuration)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## Features

- **User Authentication**: Register and log in to play the game.
- **Chess Board**: Interactive chess board with all standard chess rules implemented.
- **Game Play**: Two players can play against each other in real-time.
- **Game History**: View past games and their outcomes.
- **Responsive Design**: The game is accessible on different devices.

## Technology Stack

- **Frontend**: JSP, JSTL, EL, HTML, CSS, JavaScript
- **Backend**: Java Servlets
- **Database**: MySQL
- **Database Connectivity**: JDBC
- **Testing**: JUnit
- **IDE**: Eclipse

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK)**: Version 8 or higher.
- **Eclipse IDE**: For Java EE Developers.
- **Apache Tomcat**: Version 10.1 or higher.
- **MySQL**: Version 8.x or higher.
- **MySQL Connector/J**: JDBC driver for MySQL.
- **JUnit**: Version 4 or higher.

## Setup and Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Mohammadosama55/ChessGame.git

## Import the Project into Eclipse:

1. Open Eclipse IDE.
2. Go to `File -> Import -> General -> Existing Projects into Workspace`.
3. Select the cloned repository directory and click `Finish`.

## Configure Tomcat Server:

1. In Eclipse, go to the `Servers` view.
2. Right-click and select `New -> Server`.
3. Choose Apache Tomcat and configure it with your Tomcat installation directory.

## Add MySQL Connector/J:

1. Download the MySQL Connector/J driver.
2. Add the JAR file to the `WebContent/WEB-INF/lib` directory of your project.

## Database Configuration

### Create a MySQL Database:

```sql
CREATE DATABASE chess_game;
USE chess_game;
```

### Import the Database Schema:

The SQL script for creating the necessary tables is located in the `database` folder. Run the script in your MySQL server to create the required tables.

### Configure Database Connection:

Update the `db.properties` file located in `src/main/resources` with your MySQL database credentials:

```properties
db.url=jdbc:mysql://localhost:3306/chess_game
db.username=root
db.password=root
```

## Running the Application

### Start the Tomcat Server:

Right-click on the project in Eclipse and select `Run As -> Run on Server`. Choose your configured Tomcat server and click `Finish`.

### Access the Application:

Open your web browser and go to `http://localhost:8080/GameChess`.

## Testing

The project includes JUnit test cases to ensure the functionality of the core components. To run the tests:

1. Navigate to the `Test Directory`: The test cases are located in the `src/test/java` directory.
2. Run the Tests: Right-click on the test class and select `Run As -> JUnit Test`.
