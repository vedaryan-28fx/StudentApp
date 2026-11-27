package ved.StudentApp.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import ved.StudentApp.dto.book.BookResponseDto;
import ved.StudentApp.dto.student.StudentRequestDto;
import ved.StudentApp.dto.student.StudentResponseDto;
import ved.StudentApp.exception.ResourceNotFoundException;
import ved.StudentApp.services.BookServices;
import ved.StudentApp.services.CourseServices;
import ved.StudentApp.services.StudentServices;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebMvcTest(StudentController.class)
@AutoConfigureRestTestClient
class StudentControllerTest {

    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private StudentServices studentServices;

    @MockitoBean
    private CourseServices courseServices; // Required by StudentController's @AllArgsConstructor

    @MockitoBean
    private BookServices bookServices;

    @Test
    void getStudentById_WhenExists_ShouldReturn200AndJson() {
        StudentResponseDto response = new StudentResponseDto(1L, "Ved", "ved@example.com", 21);
        when(studentServices.getStudentById(1L)).thenReturn(response);

        restTestClient.get()
                .uri("/api/v1/students/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Ved")
                .jsonPath("$.email").isEqualTo("ved@example.com");
    }

    @Test
    void getStudentById_WhenNotFound_ShouldReturn404ProblemDetail() {
        when(studentServices.getStudentById(99L))
                .thenThrow(new ResourceNotFoundException("Student not found with id: 99"));

        restTestClient.get()
                .uri("/api/v1/students/{id}", 99L)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Resource Not Found")
                .jsonPath("$.detail").isEqualTo("Student not found with id: 99");
    }

    @Test
    void getStudentById_WhenIdIsNotPositive_ShouldReturn400() {
        // Proves @Validated + @Positive rejects -5 before StudentServices is ever invoked
        restTestClient.get()
                .uri("/api/v1/students/{id}", -5L)
                .exchange()
                .expectStatus().isBadRequest();

        verifyNoInteractions(studentServices);
    }

    @Test
    void getAllStudents_ShouldReturn200AndJsonArray() {
        List<StudentResponseDto> students = List.of(
                new StudentResponseDto(1L, "Ved", "ved@example.com", 21),
                new StudentResponseDto(2L, "Rahul", "rahul@example.com", 22)
        );
        when(studentServices.getAllStudents()).thenReturn(students);

        restTestClient.get()
                .uri("/api/v1/students")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].name").isEqualTo("Ved")
                .jsonPath("$[1].name").isEqualTo("Rahul");
    }

    @Test
    void getBooksByStudentId_ShouldReturn200AndBooksList() {
        List<BookResponseDto> books = List.of(
                new BookResponseDto(10L, "Effective Java", "Joshua Bloch", 1L)
        );
        when(bookServices.getBooksByStudentID(1L)).thenReturn(books);

        restTestClient.get()
                .uri("/api/v1/students/{studentId}/books", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.length()").isEqualTo(1)
                .jsonPath("$[0].name").isEqualTo("Effective Java");
    }

    @Test
    void createStudent_WhenValid_ShouldReturn201Created() {
        StudentRequestDto request = new StudentRequestDto("Ved", "ved@example.com", 21);
        StudentResponseDto created = new StudentResponseDto(1L, "Ved", "ved@example.com", 21);

        when(studentServices.createStudent(any(StudentRequestDto.class))).thenReturn(created);

        restTestClient.post()
                .uri("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Ved");
    }

    @Test
    void createStudent_WhenInvalidBody_ShouldReturn400WithFieldErrors() {
        // Sending raw invalid JSON triggers @Valid + GlobalExceptionHandler.handleMethodArgumentNotValid
        String invalidJson = """
                {
                    "name": "",
                    "email": "not-an-email",
                    "age": -5
                }
                """;

        restTestClient.post()
                .uri("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .body(invalidJson)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Validation Failed")
                .jsonPath("$.fieldErrors").exists();

        verifyNoInteractions(studentServices);
    }

    @Test
    void updateStudent_WhenValid_ShouldReturn200Ok() {
        StudentRequestDto request = new StudentRequestDto("Ved Updated", "ved.new@example.com", 22);
        StudentResponseDto updated = new StudentResponseDto(1L, "Ved Updated", "ved.new@example.com", 22);

        when(studentServices.updateStudent(eq(1L), any(StudentRequestDto.class))).thenReturn(updated);

        restTestClient.put()
                .uri("/api/v1/students/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Ved Updated")
                .jsonPath("$.email").isEqualTo("ved.new@example.com");
    }

    @Test
    void deleteStudent_WhenExists_ShouldReturn204NoContent() {
        doNothing().when(studentServices).deleteStudent(1L);

        restTestClient.delete()
                .uri("/api/v1/students/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        verify(studentServices).deleteStudent(1L);
    }
}