package ved.StudentApp.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
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
    private final ModelMapper modelMapper;
    private final StudentRepository studentRepository;

    @Transactional
    public BookResponseDto addBook(BookRequestDto bookRequestDto) {
        Book newbook = modelMapper.map(bookRequestDto, Book.class);
        Book savedBook = bookRepository.save(newbook);
        return modelMapper.map(savedBook,BookResponseDto.class);
    }

    @Transactional
    public void removeBook(Long bookId, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Student with id " + studentId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Book with id " + bookId));

        if (book.getStudent() == null || !book.getStudent().getId().equals(student.getId())) {
            throw new ResourceNotFoundException("Book " + bookId + " does not belong to Student " + studentId);
        }

        student.removeBook(book);
    }

    public List<BookResponseDto> getBooksByStudentID(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Student with id " + studentId));

        return student.getBooks().stream()
                .map(book -> modelMapper.map(book, BookResponseDto.class))
                .toList();
    }

    @Transactional
    public BookResponseDto assignBookToStudent(Long studentId, Long bookId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Student with id " + studentId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Book with id " + bookId));

        student.addBook(book);
        return modelMapper.map(book, BookResponseDto.class);

    }

    public BookResponseDto unassignBookToStudent(Long bookId, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Student with id " + studentId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Book with id " + bookId));

        student.removeBook(book);
        return modelMapper.map(book, BookResponseDto.class);

    }
}