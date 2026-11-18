package ved.StudentApp.books.Controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ved.StudentApp.books.Services.BookServices;
import ved.StudentApp.books.dto.BookDto;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/students")
public class BookController {

    private final BookServices bookServices;

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        BookDto bookDto = bookServices.getBookById(id);
        return ResponseEntity.status(HttpStatus.OK).body(bookDto);
    }
    @GetMapping()
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> bookDtoList = bookServices.getAllBooks();
        return ResponseEntity.status(HttpStatus.OK).body(bookDtoList);
    }
    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) {
        BookDto craeteBookDto = bookServices.createBook(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(craeteBookDto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id , @RequestBody BookDto bookDto) {
        BookDto updateBookDto = bookServices.updateBook(id, bookDto);
        return ResponseEntity.status(HttpStatus.OK).body(updateBookDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BookDto> deleteBook(@PathVariable Long id) {
        bookServices.deleteBook(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
