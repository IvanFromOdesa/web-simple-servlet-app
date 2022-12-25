# web-simple-servlet-app
## Web-app on Java Servlets + JSPs

Simple web-application with form-based authentication. You can login/logout and see the list of users that have access to the app (their names and logins).
Users are retrieved from in-memory list of server. Unathorized users do not have access to any of app resources.

### Build & deployment
To build the web-app, run ```mvn package``` in terminal from project destination.

A .war file will appear at target folder after **BUILD SUCCESS**.

Start Tomcat server and deploy the .war file.

*Note: the app usses javax-servlet-api v. 3.1.0, so be advised to use web-container that does support that.
