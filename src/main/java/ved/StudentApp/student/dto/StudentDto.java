package ved.StudentApp.student.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.*;

@Getter
@Setter

public class StudentDto {

    private Long id;

    @NotBlank
    @Size(max = 30)
    private String name;

    @NotBlank
    @NotNull
    @Email
    @Size(max = 50)
    private String email;

    @NotBlank
    @Size(max = 30)
    private int age;
}
