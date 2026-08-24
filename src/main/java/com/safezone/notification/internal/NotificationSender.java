package com.safezone.notification.internal;

interface NotificationSender {

    void send(String recipientHint, String message);
}
