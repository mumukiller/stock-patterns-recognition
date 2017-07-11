package com.mumukiller.alert.notifier;

import com.mumukiller.alert.transport.Pattern;

import java.util.List;

/**
 * Created by Mumukiller on 17.06.2017.
 */
public interface Notifier {
    /**
     * Send pattern using underlying transport
     * @param pattern a pattern to send
     */
    void notify(final Pattern pattern);

    /**
     * Send two first patterns because of sms length restrictions.
     * Message also contains a size of patterns which should be sent initially
     * @param patterns a list of patterns to send
     */
    void notify(final List<Pattern> patterns);
}
