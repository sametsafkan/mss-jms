package com.sametsafkan.mssjms.listener;

import com.sametsafkan.mssjms.config.JmsConfig;
import com.sametsafkan.mssjms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class HelloMessageListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listen(@Payload HelloWorldMessage helloWorldMessage,
                       @Headers MessageHeaders headers,
                       Message message){
        log.info("I got a message : ");
        log.info(helloWorldMessage.toString());
    }

    @JmsListener(destination = JmsConfig.SEND_AND_RECEIVE_QUEUE)
    public void listenAndSend(@Payload HelloWorldMessage helloWorldMessage,
                       @Headers MessageHeaders headers,
                       Message message) throws JMSException {
        log.info("I got a message : ");
        log.info(helloWorldMessage.toString());
        HelloWorldMessage returningMessage = HelloWorldMessage.builder()
                .message("Returning Hello World")
                .id(UUID.randomUUID())
                .build();
        jmsTemplate.convertAndSend(message.getJMSReplyTo(), returningMessage);
    }
}
