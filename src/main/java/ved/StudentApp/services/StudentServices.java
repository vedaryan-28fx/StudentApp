package ved.StudentApp.services;

import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ved.StudentApp.dto.student.StudentRequestDto;
import ved.StudentApp.dto.student.StudentResponseDto;
import ved.StudentApp.entity.Book;
import ved.StudentApp.mapper.StudentMapper;
import ved.StudentApp.repository.StudentRepository;
import ved.StudentApp.entity.Student;
import ved.StudentApp.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentServices {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Transactional(readOnly = true)
    public StudentResponseDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return studentMapper.toStudentResponseDto(student);
    }

    @Transactional(readOnly = true)
    public List<StudentResponseDto> getAllStudents() {
        List<Student> studentList = studentRepository.findAll();
        return studentMapper.toStudentResponseDtos(studentList);
    }
    @Transactional
    public StudentResponseDto createStudent(StudentRequestDto studentDto) {
        Student newStudent = studentMapper.toEntity(studentDto);
        Student savedStudent = studentRepository.save(newStudent);
        return studentMapper.toStudentResponseDto(savedStudent);

    }
    @Transactional
    public StudentResponseDto updateStudent(Long id, StudentRequestDto studentDto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        studentMapper.updateEntityFromRequest(studentDto, student);
//        studentRepository.save(student);    cause of Transactional
        return studentMapper.toStudentResponseDto(student);

    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        // unlink books from student
        for (Book book : new ArrayList<>(student.getBooks())) {
            student.removeBook(book);
        }
        studentRepository.delete(student);
    }
}
