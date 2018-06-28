# Weather-Forecast Prototype

# Brief Overview Of This Repository

- This repository is my implementation for a weather forecast prototype web application I previously built in both
[Ruby (using Rails and TDD)](https://github.com/ttran9/rails-weather-forecast) and 
[C# (using ASP.NET and no TDD)](https://github.com/ttran9/weather-forecast-aspnet).
    - Some technologies used would be Spring Boot, Java 8, Hibernate 5, PostgreSQL (used locally and on Heroku).
        - For a local database I ran the latest PostgreSQL docker image inside of a docker container.
        
# Details about this Repository

- For this application there will also be another user account that I created that has limited permissions, such as
DML and not DDL as I do not want to give all user accounts as much power as a default root/postgres account (the ability 
to change the database schema such as modifying the structure of table(s)).
    - Inside of the src/main/scripts directory I will have a simple SQL script in there that creates a database and
    initializes a user with the permissions I discussed above.
    - When creating the docker container I pass in some flags which create a root user and I use that to run the commands
    which can be found inside of samplePostgresUser.sql.
        - Note for this that by default a created user can create tables which isn't the desired behavior/permission
        of that newly created user (sample_user).

- A modification to this application will be that I will be using CircleCI to ensure that updated GIT commits are 
properly building on maven without any issues.
    - I am doing this because if I were to be working on this application in a team I would want to make sure that any
    build I update (make a commit) would result in no issues and someone would be able to take my changes and build it
    on their local machine and test the application in addition to running the automated tests and expecting to see no
    failed tests.
    
- Another modification will be that I will be using codecov to help me track the amount of code coverage of my automated
tests.

- One other modification will be how I represent the data model, which was generated using
[J Hipster](https://start.jhipster.tech/#/design-entities) and "Create a new JDL model" if you are logged in and if you
are not logged in you can go [here](https://start.jhipster.tech/jdl-studio/) to start building the diagram.

# Docker Information (for using the database in development mode)

- I created a development database using the command below:
    - docker run --name dockerpsql -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=hidden -e POSTGRES_DB=forecast_dev -p 5432:5432 -d postgres
    - note: this is just a simple example set of user credentials, for your own you would want to have a more secure password
    for the postgres user.
        - if you use the above command keep in mind that the latest postgres version is pulled down so your version will
         change in case some undesired results occur.

- This application doesn't focus too much on Postgres usage so I will be using the public schema.

# Codecov test coverage:
![codecov](https://codecov.io/gh/ttran9/tt-forecast-spring/branch/master/graph/badge.svg)

# CircleCI build status:
![CircleCI](https://circleci.com/gh/ttran9/tt-forecast-spring.svg?style=svg)

