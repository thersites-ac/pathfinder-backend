# Pathfinder-Backend

### What it is
Backend for managing Pathfinder profile data, built for AWS Lambda infra, for a cheap bastard expecting low traffic.

### Dependencies
* Docker
* mysql-client
* AWS SAM CLI
* some Java JDK
* maven

### One-time setup
Run:

    docker network create pathfinder
    docker run --name mysql --network pathfinder -d -p 3307:3306 mysql/mysql-server:8.0.23
   
You can stop the docker container or start it up again any time with `docker stop mysql` and `docker start mysql`.
Next, create the database and user credentials locally. Run:

    docker logs mysql 2>/dev/null | grep GENERATED
    
Save the password you see. Next, run

    docker exec -it mysql mysql -uroot -p
    
This starts a MySQL session for the containerized database. You will be prompted for a password; give the one you saved from before.
Finally, run the following MySQL commands:

    > ALTER USER 'root'@'localhost' IDENTIFIED BY 'somepassword';
    > CREATE DATABASE pathfinder;
    > USE pathfinder;
    > CREATE USER 'pathfinder'@'%' IDENTIFIED BY 'pathfinder';
    > GRANT ALL PRIVILEGES ON pathfinder.* TO 'pathfinder'@'%';

You can end the MySQL session now with `\q`; you won't need to come back to it.

### To build the code
    mvn clean package

### To run locally
    sam local start-api --docker-network pathfinder

Now you can hit the endpoint `localhost:3000/tomblywombly` with your HTTP client of choice.

### Remote Debug
    sam local start-api --docker-network pathfinder -d 5005
    
The switch `-d 5005` sets the debug port. Whenever a request comes in now, SAM will wait for you to connect a remote debugger over port 5005 before invoking the api.


### TODO
* testing
    * Add and implement Service tests
        - Do I need a distinct Bonus DAO?
        - Should I add a Skill DAO?
    * Tests validating linked behavior of Profile, Bonus, Skill
* incorporate Guice or some other dependency injection