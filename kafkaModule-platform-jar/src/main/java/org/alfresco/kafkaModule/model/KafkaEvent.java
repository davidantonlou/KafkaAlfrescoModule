package org.alfresco.kafkaModule.model;

import java.util.Date;

/**
 * Created by davidanton on 12/7/17.
 */
public class KafkaEvent {
    public static enum EventType {
        CREATE_NODE,
        UPDATE_NODE,
        DELETE_NODE,
        OTHER
    }

    private EventType type;
    private String nodeRef;
    private String author;
    private Date date;

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getNodeRef() {
        return nodeRef;
    }

    public void setNodeRef(String nodeRef) {
        this.nodeRef = nodeRef;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
