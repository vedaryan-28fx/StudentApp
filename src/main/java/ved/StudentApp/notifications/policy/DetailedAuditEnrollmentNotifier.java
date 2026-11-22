package ved.StudentApp.notifications.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service("detailedNotifier")
public class DetailedAuditEnrollmentNotifier implements EnrollmentNotificationPolicy {

    private static final Logger log = LoggerFactory.getLogger(DetailedAuditEnrollmentNotifier.class);

    @Override
    public void notifyEnrollment(Long studentId, String studentName, String studentEmail, String courseName) {
        String auditEventId = UUID.randomUUID().toString();
        Instant timestamp = Instant.now();

        log.info("""
                [DETAILED AUDIT NOTIFICATION]
                  eventId      : {}
                  timestamp    : {}
                  studentId    : {}
                  studentName  : {}
                  studentEmail : {}
                  courseName   : {}
                  status       : ENROLLED
                """, auditEventId, timestamp, studentId, studentName, studentEmail, courseName);
    }
}