package org.alfresco.kafkaModule.producer;

import org.alfresco.kafkaModule.utils.KafkaConstants;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Created by davidanton on 12/7/17.
 */
public class KafkaAlfrescoProducer implements Producer {
    private static final String KAFKA_SERVER = KafkaConstants.KAFKA_SERVER.getEndPointURL();
    private KafkaProducer<String, String> producer;

    public void init() {
        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer(props);
    }

    public void send(String topic, String message) {
        producer.send(new ProducerRecord<String, String>(topic, message));
    }

    public void close() {
        producer.close();
    }
}
