package ved.StudentApp.course.Controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ved.StudentApp.course.Services.CourseServices;
import ved.StudentApp.course.dto.CourseDto;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseServices courseServices;

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        CourseDto courseDto = courseServices.getCourseById(id);
        return ResponseEntity.status(HttpStatus.OK).body(courseDto);
    }
    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        List<CourseDto> courseDtoList = courseServices.getAllCourses();
        return ResponseEntity.status(HttpStatus.OK).body(courseDtoList);
    }
    @PostMapping
    public ResponseEntity<CourseDto> createCourse(@RequestBody CourseDto courseDto) {
        CourseDto craeteCourseDto = courseServices.createCourse(courseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(craeteCourseDto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id , @RequestBody CourseDto courseDto) {
        CourseDto updateCourseDto = courseServices.updateCourse(id, courseDto);
        return ResponseEntity.status(HttpStatus.OK).body(updateCourseDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<CourseDto> deleteCourse(@PathVariable Long id) {
        courseServices.deleteCourse(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
