package com.sametsafkan.mssjms.sender;

import com.sametsafkan.mssjms.config.JmsConfig;
import com.sametsafkan.mssjms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelloSender {

    private final JmsTemplate jmsTemplate;

    @Scheduled(fixedRate = 2000)
    public void sendMessage(){
        log.info("I am sending a message");
        HelloWorldMessage message = HelloWorldMessage.builder()
                .message("Hello World")
                .id(UUID.randomUUID())
                .build();
        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);
    }
}
