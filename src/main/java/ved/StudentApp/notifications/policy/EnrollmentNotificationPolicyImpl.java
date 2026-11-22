package ved.StudentApp.notifications.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class EnrollmentNotificationPolicyImpl implements EnrollmentNotificationPolicy {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentNotificationPolicyImpl.class);

    @Override
    public void notifyEnrollment(Long studentId, String studentName, String studentEmail, String courseName) {
        log.info("[STANDARD NOTIFICATION] Sent confirmation to {} ({}) for enrolling in course '{}'.",
                studentName, studentEmail, courseName);
    }
}
