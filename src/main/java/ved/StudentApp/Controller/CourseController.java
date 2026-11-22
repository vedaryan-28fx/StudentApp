package ved.StudentApp.Controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ved.StudentApp.services.CourseServices;
import ved.StudentApp.dto.course.CourseDto;
import ved.StudentApp.dto.course.CourseResponseDto;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class CourseController {

    private final CourseServices courseServices;

    @PostMapping("/students/{studentId}/courses/{courseID}")
    public ResponseEntity<CourseResponseDto> enrollInCourse(@PathVariable Long studentId,@PathVariable Long courseID) {
        CourseResponseDto courseResponseDto = courseServices.enrollInCourse(studentId,courseID);
        return ResponseEntity.status(HttpStatus.OK).body(courseResponseDto);
    }

    @DeleteMapping("/students/{studentId}/courses/{courseID}")
    public ResponseEntity<CourseResponseDto> withdrawFromCourse(@PathVariable Long studentId, @PathVariable Long courseID) {
        CourseResponseDto courseResponseDto = courseServices.withdrawFromCourse(studentId,courseID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(courseResponseDto);
    }

    @GetMapping("/courses/{id}")
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
    @PutMapping("courses/{id}")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id , @RequestBody CourseDto courseDto) {
        CourseDto updateCourseDto = courseServices.updateCourse(id, courseDto);
        return ResponseEntity.status(HttpStatus.OK).body(updateCourseDto);
    }
    @DeleteMapping("courses/{id}")
    public ResponseEntity<CourseDto> deleteCourse(@PathVariable Long id) {
        courseServices.deleteCourse(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
