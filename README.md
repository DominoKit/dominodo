# dominodo

### How to run 

- Build the project `mvn clean verify`

- Super Development mode :

In one terminal run  `mvn tomcat7:run -pl *-server -am -Denv=dev`

In another terminal run `mvn gwt:codeserver -pl *-client -am`

- For production get the war from the target folder and deploy it to your preferred application server
