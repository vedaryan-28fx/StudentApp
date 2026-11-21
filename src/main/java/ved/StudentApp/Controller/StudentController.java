package ved.StudentApp.Controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ved.StudentApp.Services.BookServices;
import ved.StudentApp.Services.CourseServices;
import ved.StudentApp.Services.StudentServices;
import ved.StudentApp.dto.book.BookResponseDto;
import ved.StudentApp.dto.student.StudentDto;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentServices studentServices;
    private final CourseServices courseServices;
    private final BookServices bookServices;

    @RequestMapping("/{studentId}/books")
    public ResponseEntity<List<BookResponseDto>> getBooksByStudentID(@PathVariable("studentId") Long studentId) {
        List<BookResponseDto> bookList = bookServices.getBooksByStudentID(studentId);
        return ResponseEntity.status(HttpStatus.OK).body(bookList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        StudentDto studentDto = studentServices.getStudentById(id);
        return ResponseEntity.status(HttpStatus.OK).body(studentDto);
    }
    @GetMapping()
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        List<StudentDto> studentDtoList = studentServices.getAllStudents();
        return ResponseEntity.status(HttpStatus.OK).body(studentDtoList);
    }
    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@RequestBody StudentDto studentDto) {
        StudentDto craeteStudentDto = studentServices.createStudent(studentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(craeteStudentDto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id ,@RequestBody StudentDto studentDto) {
        StudentDto updateStudentDto = studentServices.updateStudent(id,studentDto);
        return ResponseEntity.status(HttpStatus.OK).body(updateStudentDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<StudentDto> deleteStudents(@PathVariable Long id) {
        studentServices.deleteStudent(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
