# Weather-Forecast Prototype
- Codecov test coverage:
![codecov](https://codecov.io/gh/ttran9/tt-forecast-spring/branch/test-coverage/graph/badge.svg)
    
- CircleCI build status:
![CircleCI](https://circleci.com/gh/ttran9/tt-forecast-spring.svg?style=svg)

# Brief Overview Of This Repository

- This repository is my implementation for a weather forecast prototype web application I previously
built in both [Ruby (using Rails with tests)](https://github.com/ttran9/rails-weather-forecast) and 
[C# (using ASP.NET and no tests)](https://github.com/ttran9/weather-forecast-aspnet).
    - Some technologies used would be Spring Boot, Java 8, Hibernate 5, PostgreSQL (used locally and 
    on Heroku).
        - For a local database I ran the latest PostgreSQL docker image inside of a docker container.

# Summary of this branch

- Since I did not follow TDD completely (such as testing all classes) this branch will focus on 
code cleanup and adding tests where applicable and refactoring if necessary.
    - Note: I'm aware that I should've written more detailed tests and more concise code if I 
    were following TDD.
    
    # What I learned:
        - I need to be more careful while naming my "integration test" files because I 
        misnamed this it caused for my tests to not be detected by Codecov's line coverage and my 
        coverage was appearing to be much lower than it was.

# Docker Information (for using the database in development mode)

    - Refer to the src/main/scripts/README directory for information on this.

# Details about this Repository

- For the applications.properties file simply change spring.profiles.active to a profile of your 
choice to use a certain type of datastore, I will be adding it to the .gitignore as I will be 
modifying it across branches and don't want to commit a minor change every time I push a new change.

- For this application there will also be another user account that I created that has limited 
permissions, such as DML and not DDL as I do not want to give all user accounts as much power as 
a default root/postgres account (the ability to change the database schema such as modifying the 
structure of table(s)).
    - Inside of the src/main/scripts directory I will have a simple SQL script in there that creates
    a database and initializes a user with the permissions I discussed above.
    - When creating the docker container I pass in some flags which create a root user and I use 
    that to run the commands which can be found inside of samplePostgresUser.sql.
        - Note for this that by default a created user can create tables which isn't the desired 
        behavior/permission of that newly created user (sample_user).

- A modification to this application will be that I will be using CircleCI to ensure that updated 
GIT commits are properly building on maven without any issues.
    - I am doing this because if I were to be working on this application in a team I would want to 
    make sure that any build I update (make a commit) would result in no issues and someone would
    be able to take my changes and build it on their local machine and test the application in 
    addition to running the automated tests and expecting to see no failed tests.
    
- Another modification will be that I will be using codecov to help me track the amount of code 
coverage of my automated tests.

- One other modification will be how I represent the data model, which was generated using
[J Hipster](https://start.jhipster.tech/#/design-entities) and "Create a new JDL model" if you are 
logged in and if you are not logged in you can go [here](https://start.jhipster.tech/jdl-studio/)
to start building the diagram.
