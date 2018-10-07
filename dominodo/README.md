# dominodo
Domino-ui sample todo app without using MVP framework, the purpose of this sample is to show the power of domino-ui independent from the MVP frameworks, this is not the encouraged way of writing apps with domino-ui, but rather a demonostration of pure domino-ui project.

## Run the sample in dev mode
1- execute `mvn clean install`
2- in one terminal execute `mvn gwt:codeserver -pl *-client -am` to start the super dev mode.
3- in another terminal execute `mvn tomcat7:run -pl *-server -am -Denv=dev`

wait for both servers to start then access the app at `http://localhost:8080`

## Deploy to tomcat
Find the war file i the target folder of the `dominodo-server` module.
