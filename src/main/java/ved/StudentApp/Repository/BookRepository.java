package ved.StudentApp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ved.StudentApp.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
