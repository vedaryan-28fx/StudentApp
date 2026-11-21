package ved.StudentApp.Services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ved.StudentApp.Repository.CourseRepository;
import ved.StudentApp.Repository.StudentRepository;
import ved.StudentApp.dto.course.CourseDto;
import ved.StudentApp.dto.course.CourseResponseDto;
import ved.StudentApp.entity.Course;
import ved.StudentApp.entity.Student;
import ved.StudentApp.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CourseServices {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    public CourseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        return modelMapper.map(course, CourseDto.class);
    }

    public List<CourseDto> getAllCourses() {
        List<Course> courseList = courseRepository.findAll();
        List<CourseDto> courseDtoList = new ArrayList<>();
        for (int i = 0; i < courseList.toArray().length; i++) {
            courseDtoList.add(modelMapper.map(courseList.get(i), CourseDto.class));
        }
        return courseDtoList;
    }
    @Transactional
    public CourseDto createCourse(CourseDto courseDto) {
        Course newCourse = modelMapper.map(courseDto, Course.class);
        courseRepository.save(newCourse);
        return modelMapper.map(newCourse, CourseDto.class);

    }
    @Transactional
    public CourseDto updateCourse(Long id, CourseDto courseDto) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        modelMapper.map(courseDto, course);
//        courseRepository.save(Course);    cause of Transactional
        return modelMapper.map(course, CourseDto.class);

    }

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
        return modelMapper.map(course, CourseResponseDto.class);
    }


    @Transactional
    public CourseResponseDto withdrawFromCourse(Long studentId, Long courseID) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(()-> new ResourceNotFoundException("Student not found with id: " + studentId));
        Course course = courseRepository.findById(courseID)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseID));

        student.dropCourse(course);
        return modelMapper.map(course, CourseResponseDto.class);
    }
}
