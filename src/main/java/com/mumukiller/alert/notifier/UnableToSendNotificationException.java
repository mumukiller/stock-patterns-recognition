package com.mumukiller.alert.notifier;

/**
 * Created by Mumukiller on 15.07.2017.
 */
public class UnableToSendNotificationException extends RuntimeException {
    public UnableToSendNotificationException(final Throwable cause) {
        super(cause);
    }
}
