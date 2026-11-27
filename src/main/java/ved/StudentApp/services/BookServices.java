package ved.StudentApp.services;

import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ved.StudentApp.mapper.BookMapper;
import ved.StudentApp.repository.BookRepository;
import ved.StudentApp.dto.book.BookRequestDto;
import ved.StudentApp.dto.book.BookResponseDto;
import ved.StudentApp.entity.Book;
import ved.StudentApp.repository.StudentRepository;
import ved.StudentApp.entity.Student;
import ved.StudentApp.exception.ResourceNotFoundException;

import java.util.List;

@Service
@AllArgsConstructor
public class BookServices {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final StudentRepository studentRepository;

    @Transactional
    public BookResponseDto addBook(BookRequestDto bookRequestDto) {
        Book newbook = bookMapper.toEntity(bookRequestDto);
        Book savedBook = bookRepository.save(newbook);
        return bookMapper.toBookResponseDto(savedBook);
    }

    @Transactional
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Book with id " + bookId));

        if (book.getStudent() != null) {
            book.getStudent().removeBook(book);
        }
        bookRepository.delete(book);
    }

    @Transactional(readOnly = true)
    public List<BookResponseDto> getBooksByStudentID(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Student with id " + studentId));

        return bookMapper.toBookResponseDto(student.getBooks());
    }

    @Transactional
    public BookResponseDto assignBookToStudent(Long studentId, Long bookId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Student with id " + studentId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Book with id " + bookId));

        student.addBook(book);
        return bookMapper.toBookResponseDto(book);

    }

    @Transactional
    public BookResponseDto unassignBookToStudent(Long bookId, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Student with id " + studentId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Book with id " + bookId));

        student.removeBook(book);
        return bookMapper.toBookResponseDto(book);

    }
}