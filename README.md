# Kafka Alfresco Module

Alfresco repository customization for generating messages of Alfresco actions and save into Apache Kafka server.


# Build and Run Alfresco

Run embedded Tomcat + H2 DB with `mvn clean install -DskipTests=true alfresco:run` or `./run.sh` 

 
# Run Kafka Server & Zookeeper

 * Start Zookeeper: `./bin/zookeeper-server-start.sh config/zookeeper.properties`
 * Start Kafka: `./bin/kafka-server-start.sh config/server.properties`
  
 
# Kafka Consumer

Run `.bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic TOPIC_TYPE` and view all generated messages in real time.
  
 
# Alfresco Configuration

Configure Kafka host and port server from alfresco-global.properties file.

`kafka.server.host=localhost
kafka.server.port=9092`