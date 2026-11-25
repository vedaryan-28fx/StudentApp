package ved.StudentApp.dto.student;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.NumberFormat;

public record StudentRequestDto(

        @NotBlank(message = "Email is required")
        @Email(message = "Please enter a valid email")
        String email,

        @NotBlank(message = "Student name is required")
        @Size(min = 1, max = 50, message = "Name should be less than 50 characters")
        String name,

        @NotNull(message = "Age is required")
        @Min(value = 5, message = "Age must be at least 5")
        @Max(value = 120, message = "Age cannot exceed 120")
        Integer age
) {
}
