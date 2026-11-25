package ved.StudentApp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ved.StudentApp.dto.course.CourseRequestDto;
import ved.StudentApp.dto.course.CourseResponseDto;
import ved.StudentApp.entity.Course;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseResponseDto toCourseResponseDto(Course course);

    List<CourseResponseDto> toCourseResponseDto(Collection<Course> courses);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "students",  ignore = true)
    Course toEntity(CourseRequestDto course);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "students",  ignore = true)
    void updateEntityFromRequest(CourseRequestDto request, @MappingTarget Course course);
}
