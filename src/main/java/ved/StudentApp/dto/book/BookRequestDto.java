package ved.StudentApp.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.EnumAnnotationValue;

//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class BookRequestDto {
//
//    @NotBlank(message = "Book name is required")
//    @Size(min = 1, max = 30)
//    private String name;
//
//    @NotBlank(message = "Author name is required")
//    @Size(min = 1, max = 30)
//    private String author;
//
//}

public record BookRequestDto(

        @NotBlank(message = "Book name is required")
        @Size(min = 1, max = 100, message = "Book name must be between 1 and 100 characters")
        String name,

        @NotBlank(message = "Author name is required")
        @Size(min = 1, max = 30, message = "Name must be between 1 and 30 characters")
        String author
){
    public BookRequestDto{
        if(name != null) name=name.trim();
        if (author != null) author=author.trim();
    }
}