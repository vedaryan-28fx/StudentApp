package ved.StudentApp.configuration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ved.StudentApp.entity.Book;
import ved.StudentApp.entity.Course;
import ved.StudentApp.entity.Student;
import ved.StudentApp.repository.BookRepository;
import ved.StudentApp.repository.CourseRepository;
import ved.StudentApp.repository.StudentRepository;

import java.util.List;

@Component
public class Populate implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Populate.class);

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final BookRepository bookRepository;

    public Populate(
            StudentRepository studentRepository,
            CourseRepository courseRepository,
            BookRepository bookRepository
    ) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.bookRepository = bookRepository;
    }

    @PostConstruct
    public void init() {
        log.info("Populate init (@PostConstruct)");
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Safety guard in case ddl-auto is ever switched back to "update"
        if (studentRepository.count() > 0 || courseRepository.count() > 0) {
            log.info("Database already seeded. Skipping default data population.");
            return;
        }

        // 1. Save Courses first (Student.courses has no cascade, so courses must exist first)
        Course springBoot = new Course();
        springBoot.setName("Spring Boot & Microservices");
        springBoot.setDescription("Backend architecture, REST APIs, and Spring Data JPA");

        Course databases = new Course();
        databases.setName("PostgreSQL Internals");
        databases.setDescription("Indexing, transactions, and query optimization");

        Course docker = new Course();
        docker.setName("Docker & Containers");
        docker.setDescription("Containerizing backend services with Docker Compose");

        courseRepository.saveAll(List.of(springBoot, databases, docker));

        // 2. Create Books to assign to Students
        Book cleanCode = new Book();
        cleanCode.setName("Clean Code");
        cleanCode.setAuthor("Robert C. Martin");

        Book ddia = new Book();
        ddia.setName("Designing Data-Intensive Applications");
        ddia.setAuthor("Martin Kleppmann");

        Book springInAction = new Book();
        springInAction.setName("Spring in Action");
        springInAction.setAuthor("Craig Walls");

        // 3. Create Students and wire relationships using your entity helper methods
        Student ved = new Student();
        ved.setName("Ved Aryan");
        ved.setEmail("ved@studentapp.dev");
        ved.setAge(21);
        ved.addCourse(springBoot);
        ved.addCourse(databases);
        ved.addBook(cleanCode);
        ved.addBook(ddia);

        Student rahul = new Student();
        rahul.setName("Rahul Verma");
        rahul.setEmail("rahul@studentapp.dev");
        rahul.setAge(22);
        rahul.addCourse(springBoot);
        rahul.addCourse(docker);
        rahul.addBook(springInAction);

        // Saves students, populates student_courses, AND cascades PERSIST to their books
        studentRepository.saveAll(List.of(ved, rahul));

        // 4. Save an unassigned book separately (student_id stays NULL)
        Book effectiveJava = new Book();
        effectiveJava.setName("Effective Java");
        effectiveJava.setAuthor("Joshua Bloch");
        bookRepository.save(effectiveJava);

        log.info("Seeded {} courses, {} students, and {} books.",
                courseRepository.count(), studentRepository.count(), bookRepository.count());
    }

    @PreDestroy
    public void destroy() {
        log.info("Logging clean resource release before the ApplicationContext closes (@PreDestroy)");
    }
}