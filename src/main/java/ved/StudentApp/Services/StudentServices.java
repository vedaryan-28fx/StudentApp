package ved.StudentApp.Services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ved.StudentApp.Repository.StudentRepository;
import ved.StudentApp.dto.student.StudentDto;
import ved.StudentApp.entity.Student;
import ved.StudentApp.exception.ResourceNotFoundException;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentServices {

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    public StudentDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return modelMapper.map(student, StudentDto.class);
    }

    public List<StudentDto> getAllStudents() {
        List<Student> studentList = studentRepository.findAll();
        return studentList.stream()
                .map(student -> modelMapper.map(student , StudentDto.class))
                .toList();
    }
    @Transactional
    public StudentDto createStudent(StudentDto studentDto) {
        Student newStudent = modelMapper.map(studentDto, Student.class);
        studentRepository.save(newStudent);
        return modelMapper.map(newStudent, StudentDto.class);

    }
    @Transactional
    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        modelMapper.map(studentDto, student);
//        studentRepository.save(student);    cause of Transactional
        return modelMapper.map(student, StudentDto.class);

    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
