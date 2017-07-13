package org.alfresco.kafkaModule.producer;

/**
 * Created by davidanton on 12/7/17.
 */
public interface Producer {

    public void send(String topic, String message);

    public void close();
}
