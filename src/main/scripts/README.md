# Creating a local Postgres Database
    - docker run --name dockerpsql -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=hidden -e 
    POSTGRES_DB=forecast_dev -p 5432:5432 -d postgres
    - note: this is just a simple example set of user credentials to be used locally (for testing).
    - the contents inside of the src/main/scripts such as all the scripts need to be run as this 
    admin user which will have the necessary permissions.
    - this application will be run as the sample_user which cannot create tables.
    
    - Details running the scripts:
        - Before running this application and the tests using the Postgres database you must have
         the user created as well as the tables generated and the proper permissions granted.
         
    - Run the contents of the three scripts: samplepostgresuser.sql, schemacreate.sql, and 
    sampleusergrant.sql step by step by how they are labeled (steps 1-3) for the corresponding 
    segments.
    
    - If you are getting errors and you don't want to drop the database you can always drop the 
    tables before running the three other steps mentioned in the previous bullet point.
        - Just execute the contents of the droptables.sql file to do this.
        - If you decide to drop the tables you don't need to re-create the user or re-run the 
        script corresponding to step 1 (samplepostgresuser.sql), but you should re-run steps 2-3.
     