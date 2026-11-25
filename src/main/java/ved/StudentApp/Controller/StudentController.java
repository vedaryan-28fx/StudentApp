package ved.StudentApp.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ved.StudentApp.dto.student.StudentRequestDto;
import ved.StudentApp.dto.student.StudentResponseDto;
import ved.StudentApp.services.BookServices;
import ved.StudentApp.services.CourseServices;
import ved.StudentApp.services.StudentServices;
import ved.StudentApp.dto.book.BookResponseDto;

import java.util.List;

@AllArgsConstructor
@Validated

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentServices studentServices;
    private final CourseServices courseServices;
    private final BookServices bookServices;

    @GetMapping("/{studentId}/books")
    public ResponseEntity<List<BookResponseDto>> getBooksByStudentID(
            @Positive @PathVariable("studentId") Long studentId
    ) {
        List<BookResponseDto> bookList = bookServices.getBooksByStudentID(studentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDto> getStudentById(
            @Positive @PathVariable Long id
    ) {
        StudentResponseDto studentDto = studentServices.getStudentById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(studentDto);
    }
    @GetMapping()
    public ResponseEntity<List<StudentResponseDto>> getAllStudents() {
        List<StudentResponseDto> studentDtoList = studentServices.getAllStudents();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(studentDtoList);
    }
    @PostMapping
    public ResponseEntity<StudentResponseDto> createStudent(
            @Valid @RequestBody StudentRequestDto studentDto
    ) {
        StudentResponseDto createStudentDto = studentServices.createStudent(studentDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createStudentDto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDto> updateStudent(
            @Positive @PathVariable Long id,
            @Valid @RequestBody StudentRequestDto studentDto
    ) {
        StudentResponseDto updateStudentDto = studentServices.updateStudent(id,studentDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updateStudentDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<StudentResponseDto> deleteStudents(
            @Positive @PathVariable Long id
    ) {
        studentServices.deleteStudent(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }


}
