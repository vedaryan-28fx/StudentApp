package ved.StudentApp.services;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ved.StudentApp.dto.book.BookRequestDto;
import ved.StudentApp.dto.book.BookResponseDto;
import ved.StudentApp.dto.student.StudentResponseDto;
import ved.StudentApp.entity.Book;
import ved.StudentApp.entity.Student;
import ved.StudentApp.exception.ResourceNotFoundException;
import ved.StudentApp.mapper.BookMapper;
import ved.StudentApp.repository.BookRepository;
import ved.StudentApp.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServicesTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private BookServices bookServices;

    @Test
    void getBooksByStudentID_WhenExists_ShouldReturnListOfBooks() {
        //Arrange
        Student student = new Student();
        student.setId(1L);
        student.setName("Ved");
        student.setEmail("ved@example.com");
        student.setAge(21);

        Book b1 = new Book();
        b1.setId(1L);
        Book b2 = new Book();
        b2.setId(2L);

        student.addBook(b1);
        student.addBook(b2);

        List<BookResponseDto> expectedBooks = List.of(
                new BookResponseDto(1L,"Test Book","Ved_Author",1L),
                new BookResponseDto(2L,"Test Book2","Ved_Author2",1L)
        );

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(bookMapper.toBookResponseDto(student.getBooks())).thenReturn(expectedBooks);

        //Act
        List<BookResponseDto> actualBooks = bookServices.getBooksByStudentID(1L);


        //Assert
        assertThat(actualBooks).hasSize(2).isEqualTo(expectedBooks);
        verify(studentRepository).findById(1L);
        verify(bookMapper).toBookResponseDto(student.getBooks());

    }

    @Test
    void getBooksByStudentID_WhenMissing_ShouldThrowResourceNotFoundException() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookServices.getBooksByStudentID(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cannot find Student with id 99");

        verifyNoInteractions(bookMapper);
    }

    @Test
    void addBook_ShouldSave_ShouldReturnBookResponseDto() {
        BookRequestDto requestDto = new BookRequestDto("Ved","Ved_Author");

        Book newBook = new Book();
        newBook.setAuthor("Ved_Author");
        newBook.setName("Ved");

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setAuthor("Ved_Author");
        savedBook.setName("Ved");
        savedBook.setId(1L);

        BookResponseDto expectedBook = new BookResponseDto(1L,"Ved","Ved_Author",1L);

        when(bookMapper.toEntity(requestDto)).thenReturn(newBook);
        when(bookRepository.save(newBook)).thenReturn(savedBook);
        when(bookMapper.toBookResponseDto(savedBook)).thenReturn(expectedBook);

        BookResponseDto actualBook = bookServices.addBook(requestDto);

        assertThat(actualBook).isEqualTo(expectedBook);
        verify(bookRepository).save(newBook);
        verify(bookMapper).toBookResponseDto(savedBook);
    }

    @Test
    void deleteBook_WhenExists_ShouldDeleteBook() {
        Student student = new Student();
        student.setId(1L);

        Book book = new Book();
        book.setId(1L);

        student.addBook(book);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookServices.deleteBook(1L);

        assertThat(student.getBooks()).isEmpty();
        assertThat(book.getStudent()).isNull();
        verify(bookRepository).delete(book);
    }
    @Test
    void deleteBook_WhenMissing_ShouldThrowAndNeverDelete() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookServices.deleteBook(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(bookRepository, never()).delete(any());
    }

    @Test
    void unassignBookToStudent_WhenBothExist_ShouldUnlinkAndReturnDto(){
        Student student = new Student();
        student.setId(1L);
        Book book = new Book();
        book.setId(1L);
        book.setName("Ved");
        book.setAuthor("Ved_Author");

        student.addBook(book);

        BookResponseDto expectedBook = new BookResponseDto(1L,"Ved","Ved_Author",1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toBookResponseDto(book)).thenReturn(expectedBook);

        BookResponseDto actualBook = bookServices.unassignBookToStudent(1L,1L);

        assertThat(actualBook).isEqualTo(expectedBook);
        assertThat(student.getBooks()).isEmpty();
        assertThat(book.getStudent()).isNull();
        verify(bookRepository, never()).delete(any()); // Option B: book stays in DB
        verify(bookRepository, never()).save(any());

    }

    @Test
    void unassignBookToStudent_WhenStudentMissing_ShouldThrowAndStopImmediately(){
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(()-> bookServices.unassignBookToStudent(99L,1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cannot find Student with id 99");

        verifyNoInteractions(bookRepository,bookMapper);
    }
    @Test
    void unassignBookToStudent_WhenBookMissing_ShouldThrowResourceNotFoundException(){
        Student student = new Student();
        student.setId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookServices.unassignBookToStudent(99L,1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Cannot find Book with id 99");

        verifyNoInteractions(bookMapper);
    }
}
