# quartz
Clustered Quartz example with Spring Boot.

This demo was motivated by the fact that I couldn't find working example or instructions on how to 
set-up clustered Quartz system. Quartz example projects (number 13) shows how you can run clustered
Quartz, but it doesn't help you for setting it up in real environment. You cannot expect (well, you
can, but it's not smart) to have somebody manually cleaning your jobs from database in every system
update.

The idea in this demo is that Quartz jobs get recreated once per each installed version of the software.
The node that starts first (master) with the latest software version, will delete all existing job data and
create the new jobs according to the latest specification. The nodes that start later, just use the
job configuration created by the first node.


# Running instructions

This demo is based on Postgres database, but you can use most likely any other database as well. You
just have to copy the right Quartz table creation SQL from Quartz project. See Quartz distribution file
under directory _docs/dbTables_.

Create database
```bash
psql -c "create database quartz"
```

Create tables
```bash
psql -d quartz -f src/main/resources/sql/tables_postgres.sql
```

Compile
```bash
mvn install
```

Run node 1
```bash
java -Dserver.port=2001 -jar target/quartz-0.0.1-SNAPSHOT.jar
```

Run node 2
```bash
java -Dserver.port=2002 -jar target/quartz-0.0.1-SNAPSHOT.jar
```