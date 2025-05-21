package com.simplebanking.services;

import com.simplebanking.events.AccountCreditedEvent;
import com.simplebanking.events.AccountDebitedEvent;
import com.simplebanking.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerService.class);
    public static final String ACCOUNT_EVENTS_TOPIC = "account-events"; // Ensure this matches producer

    @Autowired
    private TransactionService transactionService;

    @KafkaListener(topics = ACCOUNT_EVENTS_TOPIC, groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void listenAccountEvents(Object event) {
        LOGGER.info("Received event: " + event.getClass().getSimpleName());
        if (event instanceof AccountCreditedEvent) {
            AccountCreditedEvent creditedEvent = (AccountCreditedEvent) event;
            LOGGER.info("Processing AccountCreditedEvent for account: {}, amount: {}", creditedEvent.getAccountId(), creditedEvent.getAmount());
            Transaction transaction = new Transaction("CREDIT", creditedEvent.getAmount(), creditedEvent.getAccountId());
            // In a real app, you might want an approval code or more details from the event
            transactionService.save(transaction);
        } else if (event instanceof AccountDebitedEvent) {
            AccountDebitedEvent debitedEvent = (AccountDebitedEvent) event;
            LOGGER.info("Processing AccountDebitedEvent for account: {}, amount: {}", debitedEvent.getAccountId(), debitedEvent.getAmount());
            Transaction transaction = new Transaction("DEBIT", debitedEvent.getAmount(), debitedEvent.getAccountId());
            transactionService.save(transaction);
        } else {
            LOGGER.warn("Received unknown event type: " + event.getClass().getName());
        }
        // Note: AccountCreatedEvent is not explicitly handled here to create a transaction,
        // but you could add logic if needed, e.g., an "ACCOUNT_OPENED" transaction type.
    }
}
