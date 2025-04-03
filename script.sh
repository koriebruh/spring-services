mvn clean install
mvn spring-boot:run

# for generating java class from proto file, before that make sure you have installed
# protoc, and add in some config and dependency in pom.xml
mvn clean compile
