# kxtract

Download Podcasts, transcribe them to text, summarize them, store them in a database and then email them.

## Dependencies
- Java 1.8+
- AWS Credentials 
- MariaDB Relational database

## Common Commands

### Build and run tests
`$ ./gradlew clean build`

### Run Code coverage

#### Get Report
`$ ./gradlew jacocoTestReport && open build/reports/jacoco/test/html/index.html`

#### Check if Coverage OK
`$ ./gradlew jacocoTestCoverageVerification`


### Relational Database Maintenance
Setup the database
`./gradlew flywayMigrate -i`

Print the details and statis information
`./gradlew flywayInfo`

Delete the database contents so we can start-over
` ./gradlew flywayClean`
