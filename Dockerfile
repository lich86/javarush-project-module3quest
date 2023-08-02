FROM tomcat:9-jdk21-openjdk
COPY ./target/quest-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war