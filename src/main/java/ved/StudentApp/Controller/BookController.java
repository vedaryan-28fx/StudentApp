package ved.StudentApp.Controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ved.StudentApp.Services.BookServices;
import ved.StudentApp.dto.book.BookRequestDto;
import ved.StudentApp.dto.book.BookResponseDto;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class BookController {

    private final BookServices bookServices;

    @PostMapping("/students/{studentId}/books/{bookId}")
    public ResponseEntity<BookResponseDto> assignBookToStudent(@PathVariable Long studentId,@PathVariable Long bookId) {
        BookResponseDto bookDto = bookServices.assignBookToStudent(bookId,studentId);
        return ResponseEntity.status(HttpStatus.OK).body(bookDto);
    }

    @DeleteMapping("/students/{studentId}/books/{bookId}")
    public ResponseEntity<BookResponseDto> unassignBookToStudent(@PathVariable Long studentId,@PathVariable Long bookId) {
        BookResponseDto bookDto = bookServices.unassignBookToStudent(bookId,studentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(bookDto);
    }

    @PostMapping("/books")
    public ResponseEntity<BookResponseDto> addBook(@RequestBody BookRequestDto bookRequestDto) {
        BookResponseDto created = bookServices.addBook(bookRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

}