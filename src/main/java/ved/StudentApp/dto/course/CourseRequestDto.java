package ved.StudentApp.dto.course;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequestDto {

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 30)
    private String name;

    @Size(max = 500)
    private String description;
}
