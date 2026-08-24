package com.safezone.notification.internal;

import com.safezone.reporting.ReportSubmitted;
import com.safezone.sync.ConflictDetected;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class DomainEventListener {

    private final NotificationSender notificationSender;

    @ApplicationModuleListener
    void on(ReportSubmitted event) {
        notificationSender.send(
                "site:" + event.siteId(), "Report " + event.reportId() + " (" + event.reportTypeCode() + ") submitted");
    }

    @ApplicationModuleListener
    void on(ConflictDetected event) {
        notificationSender.send(
                "user:" + event.submittedBy(),
                "Sync conflict on " + event.entityType() + " " + event.entityId() + " needs your review");
    }
}
