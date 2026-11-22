package ved.StudentApp.notifications.policy;

public interface EnrollmentNotificationPolicy {

    void notifyEnrollment(Long studentId, String studentName, String studentEmail, String courseName);

}

