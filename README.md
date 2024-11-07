# Coding Assigment 1

This consists of:

- A backend transaction service (Java Spring Boot)
- A backend account service (Java Spring Boot)
- A frontent webapp (Angular)

The frontend webapp allows creation and browsing of customers, accounts and transactions.

## Requirements

To run on your machine:

- Java 21+
- Maven (optional, can use the included mvnw scripts)
- Node 20 (with included npm)
- Docker (optional, if wanted to build the docker iamges locally, or stand up the project stack using docker)

## Running

The project can either be run as independant services, or run using the included docker-compose file.

First, you will have to clone this git repo and cd into the root directory.

### Individually

For the backend services, you can package the jars with:

```bash
mvn clean package -Dmaven.test.skip -f ./backend/account/pom.xml
mvn clean package -Dmaven.test.skip -f ./backend/transaction/pom.xml
```

If you do no have mvn installed on your machine, `./backend/mvnw` can be used instead of `mvn`.

The packaged jars can then be run using java:

```bash
java -jar ./backend/account/target/account-0.0.1-SNAPSHOT.jar
java -jar ./backend/transaction/target/transaction-0.0.1-SNAPSHOT.jar
```

To run the frontend, you first need to install the dependancies

```bash
cd frontend
npm ci
```

The project can then be ran with

```bash
npm run start
```

The following are the default localhost ports:

- Account service - 8081
- Transaction service - 8082
- Frontend - 4200

The account service and frontend are authenticated, the transaction service sits behind some basic auth (more on that in assumptions section below).

### Docker

To start this up in docker, you can use the compose file:

```bash
docker compose up
```

This will build and serve the services on the above ports, but note that by default only the ui and account services are accessibly from the host machine, I have no forwarded the transaction service ports (and in face the acount service ports do not need to be forwarded: I have forwarded them for convenience).

Note that the images have been build with defualt configuration and pushed to the repo registry.

## Tests

The frontend is very lightly tested, as this is a more backend focused assignemnt.

To run the frontend test, it's a simple:

```bash
cd frontend
npm ci
npm run test
```

For the backend tests, they can be run using maven:

```bash
mvn --batch-mode --update-snapshots -f ./backend/account/pom.xml test
mvn --batch-mode --update-snapshots -f ./backend/transaction/pom.xml test
```

(Again, using mvnw if appropriate).

The tests are included in the pipeline.

## Assumptions & Notes

I have made a couple of assumptions:

- The UI is used by one user at a time: hence the lack of websockets (e.g for customer creation)
- Since I introduced the DTOs, I've removed the delete endpoints, as they were not used and are not mentioned in the spec.
- I've kept transactions simple: they are immediate deposits or withdrawals from a single account, rather than transfering between accounts or performing some form of validation on source/destination. I see no reason the model could not be extended
- I've (naively) kept the transaction service as a simple REST server without validation (this is done in the account service and on the frontend): this has been done with the assumption that transaction creation will be handled via the bigger service (the (very) basic auth setup is to illustrate this). If we wanted to be able to hit the transaction service directly, then the balance on the account would have to be updated when the transaction is created (say, by firing over an event) and b) I would treat it as more of a cache than a saved property (recalculate on transation fetch, have a scheduled recalculation etc). We wouldn't want the balance and transactions to get out of sync. FOr convenience, I have currnetly left the balance saved as is as we can keep it in sync.
- As mentioned in the brief, I have used an in memory db (seperate db for both the transaction and account service) and kept a _very_ light touch on the schema design. It would instead be neater to have e.g a Postgres extenal db with foreign key constraints (e.g customerId on account referencing account table) and migration scripts, to keep it more tightly managed.
- For id, I've used UUIDs as I didn't spot if another format was specified: this does make the account list more verbose than say, numbers!
