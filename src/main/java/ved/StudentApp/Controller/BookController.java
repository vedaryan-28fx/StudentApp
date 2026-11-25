package ved.StudentApp.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ved.StudentApp.services.BookServices;
import ved.StudentApp.dto.book.BookRequestDto;
import ved.StudentApp.dto.book.BookResponseDto;

@AllArgsConstructor
@Validated

@RestController
@RequestMapping("/api/v1")
public class BookController {

    private final BookServices bookServices;

    @PostMapping("/students/{studentId}/books/{bookId}")
    public ResponseEntity<BookResponseDto> assignBookToStudent(
            @Positive(message = "Student ID must be positive")
            @PathVariable Long studentId,

            @Positive(message = "Student ID must be positive")
            @PathVariable Long bookId
    ) {
        BookResponseDto bookDto = bookServices.assignBookToStudent(bookId,studentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookDto);
    }

    @DeleteMapping("/students/{studentId}/books/{bookId}")
    public ResponseEntity<BookResponseDto> unassignBookToStudent(
            @Positive(message = "Student ID must be positive")
            @PathVariable Long studentId,

            @Positive(message = "Student ID must be positive")
            @PathVariable Long bookId
    ) {
        BookResponseDto bookDto = bookServices.unassignBookToStudent(bookId,studentId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(bookDto);
    }

    @PostMapping("/books")
    public ResponseEntity<BookResponseDto> addBook(
            @Valid
            @RequestBody BookRequestDto bookRequestDto
    ) {
        BookResponseDto created = bookServices.addBook(bookRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

}