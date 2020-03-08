![GWT3/J2CL compatible](https://img.shields.io/badge/GWT3/J2CL-compatible-brightgreen.svg)

# dominodo
Domino-ui sample todo app without using MVP framework, the purpose of this sample is to show the power of domino-ui independent from the MVP frameworks, this is not the encouraged way of writing apps with domino-ui, but rather a demonostration of pure domino-ui project.


## Run the sample in J2CL dev mode

1. execute `mvn clean install`

2. in one terminal execute `mvn j2cl:watc` to transpile changes as they happen in j2cl

3. in another terminal execute `mvn tomcat7:run -pl *-server -am -Denv=dev`

4. wait for both servers to start then access the app at `http://localhost:8080`

5. Change dominodo-client or dominodo-j2cl code, wait for the console to say that work is finished, and refresh the browser page

## Deploy as a production compiled app

1. execute `mvn clean install` to build the entire app. 

2. Run it in the servlet container of your choosing:

   a. This could include simply running `mvn jetty:run-war` in the dominodo-server directory
  
   b. Alternatively, copy the `domindo-server/target/dominodo-server.war` file into a servlet container like tomcat or jetty.