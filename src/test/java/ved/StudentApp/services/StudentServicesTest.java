package ved.StudentApp.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ved.StudentApp.dto.student.StudentRequestDto;
import ved.StudentApp.dto.student.StudentResponseDto;
import ved.StudentApp.entity.Book;
import ved.StudentApp.entity.Student;
import ved.StudentApp.exception.ResourceNotFoundException;
import ved.StudentApp.mapper.StudentMapper;
import ved.StudentApp.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServicesTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentServices studentServices;

    @Test
    void getStudentById_WhenExists_ShouldReturnDto() {
        // ARRANGE
        Student student = new Student();
        student.setId(1L);
        student.setName("Ved");
        student.setEmail("ved@example.com");
        student.setAge(21);

        StudentResponseDto expected = new StudentResponseDto(1L, "Ved", "ved@example.com", 21);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentMapper.toStudentResponseDto(student)).thenReturn(expected);

        // ACT
        StudentResponseDto actual = studentServices.getStudentById(1L);

        // ASSERT
        assertThat(actual).isEqualTo(expected);
        verify(studentRepository).findById(1L);
        verify(studentMapper).toStudentResponseDto(student);
    }

    @Test
    void getStudentById_WhenMissing_ShouldThrowResourceNotFoundException() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentServices.getStudentById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Student not found with id: 99");

        verifyNoInteractions(studentMapper);
    }

    @Test
    void getAllStudents_ShouldReturnMappedList() {
        Student s1 = new Student();
        s1.setId(1L);
        Student s2 = new Student();
        s2.setId(2L);
        List<Student> entities = List.of(s1, s2);

        List<StudentResponseDto> expected = List.of(
                new StudentResponseDto(1L, "Ved", "ved@example.com", 21),
                new StudentResponseDto(2L, "Rahul", "rahul@example.com", 22)
        );

        when(studentRepository.findAll()).thenReturn(entities);
        when(studentMapper.toStudentResponseDtos(entities)).thenReturn(expected);

        List<StudentResponseDto> actual = studentServices.getAllStudents();

        assertThat(actual).hasSize(2).isEqualTo(expected);
        verify(studentRepository).findAll();
    }

    @Test
    void createStudent_ShouldSaveAndReturnMappedSavedStudent() {
        StudentRequestDto request = new StudentRequestDto("Ved", "ved@example.com", 21);

        Student transientStudent = new Student();
        transientStudent.setName("Ved");
        transientStudent.setEmail("ved@example.com");
        transientStudent.setAge(21);

        Student savedStudent = new Student();
        savedStudent.setId(1L);
        savedStudent.setName("Ved");
        savedStudent.setEmail("ved@example.com");
        savedStudent.setAge(21);

        StudentResponseDto expected = new StudentResponseDto(1L, "Ved", "ved@example.com", 21);

        when(studentMapper.toEntity(request)).thenReturn(transientStudent);
        when(studentRepository.save(transientStudent)).thenReturn(savedStudent);
        when(studentMapper.toStudentResponseDto(savedStudent)).thenReturn(expected);

        StudentResponseDto actual = studentServices.createStudent(request);

        assertThat(actual).isEqualTo(expected);
        verify(studentRepository, times(1)).save(transientStudent);
        verify(studentMapper).toStudentResponseDto(savedStudent);
    }

    @Test
    void updateStudent_WhenExists_ShouldMutateViaMapperWithoutExplicitSave() {
        StudentRequestDto request = new StudentRequestDto("Ved Updated", "ved.new@example.com", 22);
        Student existing = new Student();
        existing.setId(1L);
        existing.setName("Ved");

        StudentResponseDto expected = new StudentResponseDto(1L, "Ved Updated", "ved.new@example.com", 22);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(studentMapper.toStudentResponseDto(existing)).thenReturn(expected);

        StudentResponseDto actual = studentServices.updateStudent(1L, request);

        assertThat(actual).isEqualTo(expected);
        verify(studentMapper).updateEntityFromRequest(request, existing);
        verify(studentRepository, never()).save(any()); // Confirms reliance on @Transactional dirty checking
    }

    @Test
    void updateStudent_WhenMissing_ShouldThrowResourceNotFoundException() {
        StudentRequestDto request = new StudentRequestDto("Ved", "ved@example.com", 21);
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentServices.updateStudent(99L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Student not found with id: 99");

        verifyNoInteractions(studentMapper);
    }

    @Test
    void deleteStudent_WhenExists_ShouldUnlinkBooksAndDeleteStudent() {
        // ARRANGE: attach a real Book to verify your unlinking loop actually clears both sides
        Student student = new Student();
        student.setId(1L);

        Book book = new Book();
        book.setName("Effective Java");
        book.setAuthor("Joshua Bloch");
        student.addBook(book);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // ACT
        studentServices.deleteStudent(1L);

        // ASSERT
        assertThat(student.getBooks()).isEmpty();
        assertThat(book.getStudent()).isNull(); // FK side cleared before deleting student
        verify(studentRepository).delete(student);
    }

    @Test
    void deleteStudent_WhenMissing_ShouldThrowAndNeverDelete() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentServices.deleteStudent(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(studentRepository, never()).delete(any());
    }
}