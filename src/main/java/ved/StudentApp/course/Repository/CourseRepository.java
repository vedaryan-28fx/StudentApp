package ved.StudentApp.course.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ved.StudentApp.course.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
