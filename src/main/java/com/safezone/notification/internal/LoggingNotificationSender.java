package com.safezone.notification.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// Stand-in until a real email/push provider is wired in - callers only depend on
// NotificationSender, so swapping the implementation later touches nothing else.
@Component
class LoggingNotificationSender implements NotificationSender {

    private static final Logger log = LoggerFactory.getLogger(LoggingNotificationSender.class);

    @Override
    public void send(String recipientHint, String message) {
        log.info("[notification] to={} message={}", recipientHint, message);
    }
}
