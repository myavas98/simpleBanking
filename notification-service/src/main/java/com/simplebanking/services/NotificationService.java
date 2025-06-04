package com.simplebanking.services;

import com.simplebanking.events.AccountCreatedEvent;
import com.simplebanking.events.AccountCreditedEvent;
import com.simplebanking.events.AccountDebitedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);
    private static final String ACCOUNT_EVENTS_TOPIC = "account-events";

    @KafkaListener(topics = ACCOUNT_EVENTS_TOPIC, groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void handleEvent(Object event) {
        if (event instanceof AccountCreatedEvent created) {
            LOGGER.info("Notification: Account {} created for {}", created.getAccountId(), created.getOwner());
        } else if (event instanceof AccountCreditedEvent credited) {
            LOGGER.info("Notification: Account {} credited with {}", credited.getAccountId(), credited.getAmount());
        } else if (event instanceof AccountDebitedEvent debited) {
            LOGGER.info("Notification: Account {} debited by {}", debited.getAccountId(), debited.getAmount());
        } else {
            LOGGER.warn("Unknown event type: {}", event.getClass().getName());
        }
    }
}
