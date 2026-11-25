package ved.StudentApp.dto.student;

public record StudentResponseDto(

        Long id,
        String email,
        String name,
        Integer age
) {
}
