package ved.StudentApp.books.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class BookDto {

    private Long id;

    @NotBlank
    @Size(max = 30)
    private String name;

    @NotBlank
    @NotNull
    @Size(max = 50)
    private String author;
}
