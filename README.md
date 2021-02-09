## Installation

It is also straightforward to install calculator from source using Maven:

`mvn clean package`

The above step will place the calculator JAR under ./target/.
Then run the JAR file, as follows:

`java -jar target/calculator-0.0.1-SNAPSHOT.jar`

You can run the application by using 

`mvn spring-boot:run`

## API documentation:
`http://localhost:8080/swagger-ui/index.html`

### curl samples (application deployed at application context `calculator`).
> For windows use `Git Bash`

#### get Result Expression 1+2
`curl -s http://localhost:8080/rest/calculator/1+2 --user user:user`

#### find Expression By user
`curl -s http://localhost:8080/rest/calculator/findByUser/user --user admin:admin`

#### find Expression By Operation ADD
`curl -s http://localhost:8080/rest/calculator/findByOperation/ADD --user admin:admin`

#### get Between
`curl -s "http://localhost:8080/rest/calculator/filter?start=2021-02-7&end=2021-02-18" --user admin:admin`