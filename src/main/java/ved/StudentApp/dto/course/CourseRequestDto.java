package ved.StudentApp.dto.course;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//public class CourseRequestDto {
//
//    @NotBlank(message = "Name is required")
//    @Size(min = 1, max = 30)
//    private String name;
//
//    @Size(max = 500)
//    private String description;
//}

public record CourseRequestDto(

        @NotBlank(message = "Course name is required")
        @Size(min = 1, max = 50, message = "Name should be less than 50 characters")
        String name,

        @NotNull
        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description
){
    public CourseRequestDto{
        if(name != null) name=name.trim();
    }
}
