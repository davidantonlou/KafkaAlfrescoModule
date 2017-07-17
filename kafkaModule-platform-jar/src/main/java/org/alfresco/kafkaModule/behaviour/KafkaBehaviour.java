package org.alfresco.kafkaModule.behaviour;

import org.alfresco.kafkaModule.model.KafkaEvent;
import org.alfresco.kafkaModule.producer.Producer;
import org.alfresco.kafkaModule.utils.KafkaConstants;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.node.NodeServicePolicies;
import org.alfresco.repo.policy.Behaviour;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.policy.PolicyComponent;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by davidanton on 12/7/17.
 */
public class KafkaBehaviour implements NodeServicePolicies.OnCreateNodePolicy, NodeServicePolicies.OnUpdateNodePolicy, NodeServicePolicies.OnDeleteNodePolicy {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private NodeService nodeService;
    private PolicyComponent policyComponent;

    private Behaviour onCreateNode;
    private Behaviour onUpdateNode;
    private Behaviour onDeleteNode;

    private Producer producer;

    public void init() {
        this.onCreateNode = new JavaBehaviour(this, "onCreateNode",
                Behaviour.NotificationFrequency.TRANSACTION_COMMIT);
        this.onUpdateNode = new JavaBehaviour(this, "onUpdateNode",
                Behaviour.NotificationFrequency.TRANSACTION_COMMIT);
        this.onDeleteNode = new JavaBehaviour(this, "onDeleteNode",
                Behaviour.NotificationFrequency.TRANSACTION_COMMIT);

        this.policyComponent.bindClassBehaviour(QName.createQName(NamespaceService.ALFRESCO_URI, "onCreateNode"), ContentModel.TYPE_CONTENT, this.onCreateNode);
        this.policyComponent.bindClassBehaviour(QName.createQName(NamespaceService.ALFRESCO_URI, "onUpdateNode"), ContentModel.TYPE_CONTENT, this.onUpdateNode);
        this.policyComponent.bindClassBehaviour(QName.createQName(NamespaceService.ALFRESCO_URI, "onDeleteNode"), ContentModel.TYPE_CONTENT, this.onDeleteNode);
    }

    @Override
    public void onCreateNode(ChildAssociationRef childAssociationRef) {
        doBehaviourAction(KafkaConstants.KAFKA_TOPICS.CREATE_NODE.toString(), childAssociationRef.getChildRef());
    }

    @Override
    public void onDeleteNode(ChildAssociationRef childAssociationRef, boolean b) {
        doBehaviourAction(KafkaConstants.KAFKA_TOPICS.DELETE_NODE.toString(), childAssociationRef.getChildRef());
    }

    @Override
    public void onUpdateNode(NodeRef nodeRef) {
        doBehaviourAction(KafkaConstants.KAFKA_TOPICS.UPDATE_NODE.toString(), nodeRef);
    }

    private void doBehaviourAction(String topic, NodeRef nodeRef){
        if (logger.isDebugEnabled()) {
            logger.debug("KafkaBehaviour: start " + topic);
        }

        String message = createNodeRefInfoMessage(topic, nodeRef);
        insertKafkaMessage(topic, message);

        if (logger.isDebugEnabled()) {
            logger.debug("KafkaBehaviour: end " + topic);
        }
    }

    private String createNodeRefInfoMessage(String topic, NodeRef nodeRef){
        KafkaEvent event = new KafkaEvent();
        event.setNodeRef(nodeRef.toString());
        event.setDate(new Date());
        if (topic.equals(KafkaConstants.KAFKA_TOPICS.CREATE_NODE.toString())){
            event.setType(KafkaEvent.EventType.CREATE_NODE);
            event.setAuthor(nodeService.getProperty(nodeRef, ContentModel.PROP_CREATOR).toString());
        } else if (topic.equals(KafkaConstants.KAFKA_TOPICS.UPDATE_NODE.toString())){
            event.setType(KafkaEvent.EventType.UPDATE_NODE);
            event.setAuthor(nodeService.getProperty(nodeRef, ContentModel.PROP_MODIFIER).toString());
        } else if (topic.equals(KafkaConstants.KAFKA_TOPICS.DELETE_NODE.toString())){
            event.setType(KafkaEvent.EventType.DELETE_NODE);
            event.setAuthor(AuthenticationUtil.getFullyAuthenticatedUser().toString());
        } else {
            event.setType(KafkaEvent.EventType.OTHER);
            event.setAuthor(AuthenticationUtil.getFullyAuthenticatedUser().toString());
        }

        return returnMessage(event);
    }

    private String returnMessage(KafkaEvent event) {
        return new StringBuilder().append(event.getNodeRef()).append(KafkaConstants.SEPARATOR).
                append(event.getAuthor()).append(KafkaConstants.SEPARATOR).
                append(event.getDate()).toString();
    }

    private void insertKafkaMessage(String topic, String message) {
        producer.send(topic, message);
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public void setPolicyComponent(PolicyComponent policyComponent) {
        this.policyComponent = policyComponent;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }
}
