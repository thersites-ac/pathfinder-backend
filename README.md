# Pathfinder-Backend

### What it is
Backend for managing Pathfinder profile data.

### Dependencies
* Docker
* mysql-client
* AWS SAM CLI
* some Java JDK
* maven

### One-time setup
Run:

    docker network create pathfinder
    docker run --name mysql --network pathfinder -d mysql/mysql-server:8.0.23
   
You can stop the docker container or start it up again any time with `docker stop mysql` and `docker start mysql`.

### To create the database and user credentials locally
Run:

    docker logs mysql 2>/dev/null | grep GENERATED
    
Save the password you see. Next, run

    docker exec -it mysql mysql -uroot -p
    
This starts a MySQL session for the containerized database. You will be prompted for a password; give the one you saved from before.
Next, run the following MySQL commands:

    > ALTER USER 'root'@'localhost' IDENTIFIED BY 'somepassword';
    > CREATE DATABASE pathfinder;
    > USE pathfinder;
    > CREATE USER 'pathfinder'@'%' IDENTIFIED BY 'pathfinder';
    > GRANT ALL PRIVILEGES ON pathfinder.* TO 'pathfinder'@'%';

### To build the code
    mvn clean package

### To run locally
    sam local start-api --docker-network pathfinder

Now you can hit the endpoints on `localhost:3000` with your HTTP client of choice.
As of this writing, the application serves `/tomblywombly` as a REST endpoint.

### Debug
    sam local start-api --docker-network pathfinder -d 5005
    
The switch `-d 5005` sets the debug port.


### TODO
* testing
    * implement ProfileDaoTests
    * Add and implement AppTests
* split Service from App, which should help...
    * remove boilerplate
    * handle exceptions relating to path params