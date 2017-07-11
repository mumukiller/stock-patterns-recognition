package com.mumukiller.alert.notifier;

import com.mumukiller.alert.transport.Pattern;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * Created by Mumukiller on 20.06.2017.
 */
@Slf4j
@Service
public class SmsNotifierImpl implements Notifier {

    private final static String PATTERN_FOR_SENDING_LIST_WITH_SIZE_MORE_TAHT_TWO = "%s : [%s]";

    private final PhoneNumber phoneSender;
    private final PhoneNumber phoneReceiver;

    @Autowired
    public SmsNotifierImpl(final @Value("${twilio.account-sid}") String accountSid,
                           final @Value("${twilio.auth-token}") String authToken,
                           final @Value("${twilio.phone-from}") String phoneSender,
                           final @Value("${twilio.phone-to}") String phoneReceiver) {
        initializeUnderlyingTransport(accountSid, authToken);
        this.phoneSender = new PhoneNumber(phoneSender);
        this.phoneReceiver = new PhoneNumber(phoneReceiver);
    }

    /**
     * Wrapper method for Twilio API
     */
    private void initializeUnderlyingTransport(final String accountSid,
                                               final String authToken){
        Twilio.init(accountSid, authToken);
    }

    /**
     * Wrapper method for Twilio API
     */
    protected void sendViaUnderlyingTransport(final String message){
        log.debug("Message to send {}", message);

        Message.creator(phoneReceiver, phoneSender, message)
                .create();
    }

    @Override
    public void notify(final Pattern pattern) {
        send(pattern.toString());
    }

    @Override
    public void notify(final List<Pattern> pattern) {
        send(String.format(PATTERN_FOR_SENDING_LIST_WITH_SIZE_MORE_TAHT_TWO,
                           pattern.stream()
                                   .limit(2)
                                   .map(Object::toString)
                                   .collect(joining(" | ")),
                           pattern.size()));
    }

    protected void send(final String message){
        try {
            sendViaUnderlyingTransport(message);
        } catch (final Exception e){
            log.error(e.getMessage(), e);
            throw new UnableToSendNotificationException(e);
        }
    }
}
