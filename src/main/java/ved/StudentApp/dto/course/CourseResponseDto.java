package ved.StudentApp.dto.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ved.StudentApp.entity.Student;

//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//
//public class CourseResponseDto {
//
//    private Long id;
//    private String name;
//    private String description;
//}

public record CourseResponseDto(
        Long id,
        String name,
        String description
) {
}
