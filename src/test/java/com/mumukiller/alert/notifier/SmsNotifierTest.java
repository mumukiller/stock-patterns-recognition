package com.mumukiller.alert.notifier;

import com.google.common.collect.ImmutableList;
import com.mumukiller.alert.dto.PatternDto;
import com.mumukiller.alert.recognition.pattern.PatternType;
import com.mumukiller.alert.transport.Pattern;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;


/**
 * Created by Mumukiller on 16.07.2017.
 */
public class SmsNotifierTest {

    private final static LocalDate DEFAULT_LOCAL_DATE = LocalDate.of(1,1,1);
    private final static LocalTime DEFAULT_LOCAL_TIME = LocalTime.of(1,1,1);


    @Test(expected = UnableToSendNotificationException.class)
    public void failDuringSend(){
        final SmsNotifierImpl notifier = new SmsNotifierImpl("id", "token", "sender", "receiver"){
            @Override
            protected void sendViaUnderlyingTransport(String message) { throw new RuntimeException(); }
        };
        notifier.notify(new PatternDto(PatternType.UNKNOWN, DEFAULT_LOCAL_DATE, DEFAULT_LOCAL_TIME, DEFAULT_LOCAL_DATE, DEFAULT_LOCAL_TIME));
    }

    @Test
    public void sendOk(){
        final Pattern pattern =
                new PatternDto(PatternType.UNKNOWN, DEFAULT_LOCAL_DATE, DEFAULT_LOCAL_TIME, DEFAULT_LOCAL_DATE, DEFAULT_LOCAL_TIME);

        final SmsNotifierImpl notifier = new SmsNotifierImpl("id", "token", "sender", "receiver"){
            @Override
            protected void sendViaUnderlyingTransport(String message) {  }

            @Override
            protected void send(String message) {
                assertEquals(pattern.toString(), message);
                super.send(message);
            }
        };
        notifier.notify(pattern);
    }

    @Test
    public void sendListOk(){
        final List<Pattern> patterns =
                ImmutableList.of(new PatternDto(PatternType.UNKNOWN, DEFAULT_LOCAL_DATE, DEFAULT_LOCAL_TIME, DEFAULT_LOCAL_DATE, DEFAULT_LOCAL_TIME),
                                 new PatternDto(PatternType.UNKNOWN, DEFAULT_LOCAL_DATE, DEFAULT_LOCAL_TIME, DEFAULT_LOCAL_DATE, DEFAULT_LOCAL_TIME));

        final SmsNotifierImpl notifier = new SmsNotifierImpl("id", "token", "sender", "receiver"){
            @Override
            protected void sendViaUnderlyingTransport(String message) {  }

            @Override
            protected void send(String message) {
                assertEquals(patterns.stream()
                                     .map(Object::toString)
                                     .collect(joining(" | ")) + " : [2]", message);
                super.send(message);
            }
        };
        notifier.notify(patterns);
    }

    @Test
    public void sendListLongerSizeTwoOk(){
        final List<Pattern> patterns =
                ImmutableList.of(new PatternDto(PatternType.UNKNOWN, DEFAULT_LOCAL_DATE, DEFAULT_LOCAL_TIME, DEFAULT_LOCAL_DATE, DEFAULT_LOCAL_TIME),
                                 new PatternDto(PatternType.HANGING_MAN, DEFAULT_LOCAL_DATE, DEFAULT_LOCAL_TIME, DEFAULT_LOCAL_DATE, DEFAULT_LOCAL_TIME),
                                 new PatternDto(PatternType.SHOOTING_STAR, DEFAULT_LOCAL_DATE, DEFAULT_LOCAL_TIME, DEFAULT_LOCAL_DATE, DEFAULT_LOCAL_TIME));

        final List<Pattern> patternsExpectedToBeSend =
                ImmutableList.of(new PatternDto(PatternType.UNKNOWN, DEFAULT_LOCAL_DATE, DEFAULT_LOCAL_TIME, DEFAULT_LOCAL_DATE, DEFAULT_LOCAL_TIME),
                                 new PatternDto(PatternType.HANGING_MAN, DEFAULT_LOCAL_DATE, DEFAULT_LOCAL_TIME, DEFAULT_LOCAL_DATE, DEFAULT_LOCAL_TIME));


        final SmsNotifierImpl notifier = new SmsNotifierImpl("id", "token", "sender", "receiver"){
            @Override
            protected void sendViaUnderlyingTransport(String message) {  }

            @Override
            protected void send(String message) {
                assertEquals(patternsExpectedToBeSend.stream()
                                     .map(Object::toString)
                                     .collect(joining(" | ")) + " : [3]", message);
                super.send(message);
            }
        };

        notifier.notify(patterns);
    }
}
