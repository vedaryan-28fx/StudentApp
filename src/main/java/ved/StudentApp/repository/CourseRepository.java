package ved.StudentApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ved.StudentApp.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
