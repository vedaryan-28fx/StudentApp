package ved.StudentApp.books.Services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ved.StudentApp.books.Repository.BookRepository;
import ved.StudentApp.books.dto.BookDto;
import ved.StudentApp.books.entity.Book;
import ved.StudentApp.books.exception.ResourceNotFoundException;

import java.util.List;

@Service
@AllArgsConstructor
public class BookServices {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return modelMapper.map(book, BookDto.class);
    }

    public List<BookDto> getAllBooks() {
        List<Book> bookList = bookRepository.findAll();
        return bookList.stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .toList();
    }
    @Transactional
    public BookDto createBook(BookDto bookDto) {
        Book newBook = modelMapper.map(bookDto, Book.class);
        bookRepository.save(newBook);
        return modelMapper.map(newBook, BookDto.class);

    }
    @Transactional
    public BookDto updateBook(Long id, BookDto bookDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        modelMapper.map(bookDto, book);
//        bookRepository.save(book);    cause of Transactional
        return modelMapper.map(book, BookDto.class);

    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
