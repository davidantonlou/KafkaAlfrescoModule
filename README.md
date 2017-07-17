# Kafka Alfresco Module

Alfresco repository customization for generating messages of Alfresco actions and save into Apache Kafka server.
Then Kafka connects with Logstash and Elasticsearch for indexing the message information.

This project use:
 * Alfresco 5.2 with SDK 3.0.0
 * Kafka 2.12
 * Logstash 5.5
 * Elasticsearch 5.5
 * Kibana 5.5
 
 
 ![alt text](https://github.com/davidantonlou/KafkaAlfrescoModule/src/resources/module_schema.png)



# Build and Run Alfresco

Run embedded Tomcat + H2 DB with `mvn clean install -DskipTests=true alfresco:run` or `./run.sh` 

 
# Run Kafka Server & Zookeeper

 * Start Zookeeper: `./bin/zookeeper-server-start.sh config/zookeeper.properties`
 * Start Kafka: `./bin/kafka-server-start.sh config/server.properties`
  
 
# Kafka Consumer

Run `./bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic TOPIC_TYPE` and view all generated messages in real time.
  
  
# Run Elasticsearch Stack

 * Install Kafka Input Plugin: `./logstash-5.5.0/bin/logstash-plugin install logstash-input-kafka`
 * Start Elasticsearch: `./elasticsearch-5.5.0/bin/elasticsearch`
 * Start Kibana: `./kibana-5.5.0-darwin-x86_64/bin/kibana`
 * Start Logstash: `./logstash-5.5.0/bin/logstash -f logstash-5.5.0/logstash.conf --config.reload.automatic`

 
# Alfresco Configuration

Configure Kafka host and port server from alfresco-global.properties file.

`kafka.server.host=localhost`
`kafka.server.port=9092`