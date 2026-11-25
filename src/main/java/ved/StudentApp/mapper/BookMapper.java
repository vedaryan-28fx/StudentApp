package ved.StudentApp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tools.jackson.databind.type.CollectionLikeType;
import ved.StudentApp.dto.book.BookRequestDto;
import ved.StudentApp.dto.book.BookResponseDto;
import ved.StudentApp.entity.Book;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "studentId", source = "student.id")
    BookResponseDto toBookResponseDto(Book book);

    List<BookResponseDto> toBookResponseDto(Collection<Book> books);

    // Explicitly ignore DB-generated id and student association on create
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    Book toEntity(BookRequestDto request);

    // Safe in-place update for PUT /api/books/{bookId} — never touches @Id or student FK
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    void updateEntityFromRequest(BookRequestDto request, @MappingTarget Book book);
}
