package ved.StudentApp.course.Services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ved.StudentApp.course.Repository.CourseRepository;
import ved.StudentApp.course.dto.CourseDto;
import ved.StudentApp.course.entity.Course;
import ved.StudentApp.course.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CourseServices {

    private final CourseRepository courseRepository;
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
}
