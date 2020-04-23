package com.sametsafkan.mssjms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sametsafkan.mssjms.config.JmsConfig;
import com.sametsafkan.mssjms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelloSender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 2000)
    public void sendMessage() {
        log.info("I am sending a message");
        HelloWorldMessage message = HelloWorldMessage.builder()
                .message("Hello World")
                .id(UUID.randomUUID())
                .build();
        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);
    }

    @Scheduled(fixedRate = 2000)
    public void sendAndReceiveMessage() throws JMSException {
        log.info("I am sending a message");
        HelloWorldMessage message = HelloWorldMessage.builder()
                .message("Hello World")
                .id(UUID.randomUUID())
                .build();
        Message returnedMessage = jmsTemplate.sendAndReceive(JmsConfig.SEND_AND_RECEIVE_QUEUE, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage helloMessage = null;
                try {
                    helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
                    helloMessage.setStringProperty("_type", "com.sametsafkan.mssjms.model.HelloWorldMessage");
                    return helloMessage;
                } catch (JsonProcessingException e) {
                    throw new JMSException(e.getMessage());
                }
            }
        });
        log.info(returnedMessage.getBody(String.class).toString());
    }
}
