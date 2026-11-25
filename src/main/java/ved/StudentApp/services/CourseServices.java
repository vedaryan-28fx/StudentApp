package ved.StudentApp.services;

import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ved.StudentApp.dto.course.CourseRequestDto;
import ved.StudentApp.mapper.CourseMapper;
import ved.StudentApp.repository.CourseRepository;
import ved.StudentApp.repository.StudentRepository;
import ved.StudentApp.dto.course.CourseResponseDto;
import ved.StudentApp.entity.Course;
import ved.StudentApp.entity.Student;
import ved.StudentApp.exception.ResourceNotFoundException;
import ved.StudentApp.notifications.policy.EnrollmentNotificationPolicy;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CourseServices {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final CourseMapper  courseMapper;
    private final EnrollmentNotificationPolicy notificationPolicy;
    private final AdminEnrollmentService adminEnrollmentService;

    @Transactional(readOnly = true)
    public CourseResponseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        return courseMapper.toCourseResponseDto(course);
    }

    @Transactional(readOnly = true)
    public List<CourseResponseDto> getAllCourses() {
        List<Course> courseList = courseRepository.findAll();
        List<CourseResponseDto> courseDtoList = new ArrayList<>();
        for (int i = 0; i < courseList.toArray().length; i++) {
            courseDtoList.add(courseMapper.toCourseResponseDto(courseList.get(i)));
        }
        return courseDtoList;
    }
    @Transactional
    public CourseResponseDto createCourse(CourseRequestDto requestDto) {
        Course newCourse = courseMapper.toEntity(requestDto);
        courseRepository.save(newCourse);
        return courseMapper.toCourseResponseDto(newCourse);

    }
    @Transactional
    public CourseResponseDto updateCourse(Long id, CourseRequestDto requestDto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        courseMapper.updateEntityFromRequest(requestDto, course);
//        courseRepository.save(Course);    cause of Transactional
        return courseMapper.toCourseResponseDto(course);

    }

    @Transactional
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    @Transactional
    public CourseResponseDto enrollInCourse(Long studentId, Long courseID) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(()-> new ResourceNotFoundException("Student not found with id: " + studentId));
        Course course = courseRepository.findById(courseID)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseID));

        student.addCourse(course);
//        Course savedCourse = courseRepository.save(course);
        notificationPolicy.notifyEnrollment(
                student.getId(), student.getName(), student.getEmail(), course.getName()
        );
        return courseMapper.toCourseResponseDto(course);
    }

    @Transactional
    public CourseResponseDto withdrawFromCourse(Long studentId, Long courseID) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(()-> new ResourceNotFoundException("Student not found with id: " + studentId));
        Course course = courseRepository.findById(courseID)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseID));

        student.dropCourse(course);
        return courseMapper.toCourseResponseDto(course);
    }
}
