package ved.StudentApp.Controller;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ved.StudentApp.dto.course.CourseRequestDto;
import ved.StudentApp.services.CourseServices;
import ved.StudentApp.dto.course.CourseResponseDto;

import java.util.List;

@AllArgsConstructor
@RestController
@Validated

@RequestMapping("/api/v1")
public class CourseController {

    private final CourseServices courseServices;

    @PostMapping("/students/{studentId}/courses/{courseID}")
    public ResponseEntity<CourseResponseDto> enrollInCourse(
            @Positive @PathVariable Long studentId,
            @Positive @PathVariable Long courseID
    ) {
        CourseResponseDto courseResponseDto = courseServices.enrollInCourse(studentId,courseID);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(courseResponseDto);
    }

    @DeleteMapping("/students/{studentId}/courses/{courseID}")
    public ResponseEntity<CourseResponseDto> withdrawFromCourse(
            @Positive @PathVariable Long studentId,
            @Positive @PathVariable Long courseID
    ) {
        CourseResponseDto courseResponseDto = courseServices.withdrawFromCourse(studentId,courseID);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(courseResponseDto);
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseResponseDto> getCourseById(
            @Positive @PathVariable Long id
    ) {
        CourseResponseDto courseDto = courseServices.getCourseById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(courseDto);
    }
    @GetMapping
    public ResponseEntity<List<CourseResponseDto>> getAllCourses() {
        List<CourseResponseDto> courseDtoList = courseServices.getAllCourses();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(courseDtoList);
    }
    @PostMapping
    public ResponseEntity<CourseResponseDto> createCourse(
            @Positive @RequestBody CourseRequestDto courseDto
    ) {
        CourseResponseDto createCourseDto = courseServices.createCourse(courseDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createCourseDto);
    }
    @PutMapping("courses/{id}")
    public ResponseEntity<CourseResponseDto> updateCourse(
            @Positive @PathVariable Long id ,
            @Positive@RequestBody CourseRequestDto courseDto
    ) {
        CourseResponseDto updateCourseDto = courseServices.updateCourse(id, courseDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updateCourseDto);
    }
    @DeleteMapping("courses/{id}")
    public ResponseEntity<CourseResponseDto> deleteCourse(
            @Positive @PathVariable Long id
    ) {
        courseServices.deleteCourse(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }


}
