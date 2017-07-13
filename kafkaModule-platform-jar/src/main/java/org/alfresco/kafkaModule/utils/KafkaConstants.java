package org.alfresco.kafkaModule.utils;

import java.util.Properties;

public class KafkaConstants {

    protected Properties properties;

    private static KafkaConstants instance;

    private void init() {
        KafkaConstants.instance = this;
    }

    private void destroy() { KafkaConstants.instance = null; }

    public static String SEPARATOR = " | ";

    public static enum KAFKA_TOPICS {
        CREATE_NODE,
        UPDATE_NODE,
        DELETE_NODE
    }


    public static class KAFKA_SERVER {

        private static String host;
        private static String port;

        public static String getHost() {
            if(host == null) {
                host = getPropertyValue("kafka.server.host");
            }
            return host;
        }

        public static String getPort() {
            if(port == null) {
                port = getPropertyValue("kafka.server.port");
            }
            return port;
        }

        public static String getEndPointURL() {
            return getHost() + ":" + getPort();
        }
    }


    public static String getPropertyValue(String propertyName) {
        String propertyValue = instance.properties.getProperty(propertyName);
        if (propertyValue == null) {
            throw new RuntimeException("The property " + propertyName + " is not configured at alfresco-global.properties");
        }
        return propertyValue;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
