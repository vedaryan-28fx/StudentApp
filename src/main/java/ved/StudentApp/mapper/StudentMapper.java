package ved.StudentApp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ved.StudentApp.dto.student.StudentRequestDto;
import ved.StudentApp.dto.student.StudentResponseDto;
import ved.StudentApp.entity.Student;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentResponseDto toStudentResponseDto(Student student);

    List<StudentResponseDto> toStudentResponseDtos(Collection<Student> students);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    @Mapping(target = "courses", ignore = true)
    Student toEntity(StudentRequestDto studentRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    @Mapping(target = "courses", ignore = true)
    void updateEntityFromRequest(StudentRequestDto studentRequestDto, @MappingTarget Student student);
}
