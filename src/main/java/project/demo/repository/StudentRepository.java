package project.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import project.demo.entity.Student;


public interface StudentRepository extends JpaRepository<Student, Long> {
}
