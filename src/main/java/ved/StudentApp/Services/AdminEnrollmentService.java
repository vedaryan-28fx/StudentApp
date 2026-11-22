package ved.StudentApp.Services;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ved.StudentApp.repository.CourseRepository;
import ved.StudentApp.repository.StudentRepository;
import ved.StudentApp.dto.course.CourseResponseDto;
import ved.StudentApp.entity.Course;
import ved.StudentApp.entity.Student;
import ved.StudentApp.exception.ResourceNotFoundException;
import ved.StudentApp.notifications.policy.EnrollmentNotificationPolicy;

@Service
public class AdminEnrollmentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;
    private final EnrollmentNotificationPolicy notificationPolicy;

    public AdminEnrollmentService(
            StudentRepository studentRepository, CourseRepository courseRepository, ModelMapper modelMapper, @Qualifier("detailedNotifier") EnrollmentNotificationPolicy notificationPolicy
    ) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.modelMapper = modelMapper;
        this.notificationPolicy = notificationPolicy;
    }

    @Transactional
    public CourseResponseDto AdminEnrollInCourse(Long studentId, Long courseID) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(()-> new ResourceNotFoundException("Student not found with id: " + studentId));
        Course course = courseRepository.findById(courseID)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseID));

        student.addCourse(course);
//        Course savedCourse = courseRepository.save(course);
        notificationPolicy.notifyEnrollment(
                student.getId(),
                student.getName(),
                student.getEmail(),
                course.getName()
        );
        return modelMapper.map(course, CourseResponseDto.class);
    }


}