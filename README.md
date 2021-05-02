# Pathfinder-Backend

### What it is
Backend for managing Pathfinder profile data, built for AWS Lambda infra, for a cheap bastard expecting low traffic.

### Dependencies, some more or less necessary than others
* Docker
* mysql-client
* AWS SAM CLI
* some Java JDK
* maven
* terraform

# Local development
#### One-time local setup
Run:

    docker network create pathfinder
    docker run --name mysql --network pathfinder -d -p 3307:3306 mysql/mysql-server:8.0.23
   
We are mapping port 3307 to container port 3306 to avoid collision with any native mysql installation you may have.
You can stop the docker container or start it up again any time with `docker stop mysql` and `docker start mysql`.
Next, create the database and user credentials locally:

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

#### To build the code
    mvn clean package

#### To run locally
    sam local invoke --docker-network pathfinder -e {path/to/event.json}

Add the switch `-d 5005` to set the debug port. SAM will pause before starting the invocation for you to connect a remote debugger.

# Deployment
### To deploy to dev environment
You'll need to set up a database first. Use whatever solution you like for this. For a low-cost, zero-feature option, see https://github.com/thersites-ac/pathfinder-infra.
The database should be called `pathfinder` and you should create a user with remote access. Take note of the username and password, and the database's remote address.
Create a file `secrets.tf` in the `terraform/dev` directory, and populate Terraform variables `DB_USER`, `DB_PASSWORD`, and `DB_URL` with the values you created.
Finally, from the `terraform/dev` directory, run

    terraform init
    terraform apply

You can later tear the environment down with `terraform destroy`. You'll have to deal with the DB separately.

`secrets.tf` is ignored in .gitignore, so the sensitive data won't be tracked. Still, this is a flawed approach to secrets management and change.

# TODO
* incorporate Guice or some other dependency injection
* address fixmes littered throughout
* swap ENV variable secrets for a managed service