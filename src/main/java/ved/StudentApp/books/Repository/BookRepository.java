package ved.StudentApp.books.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ved.StudentApp.books.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
