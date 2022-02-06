package project.demo.security.repository;


import project.demo.security.entity.Authority;
import project.demo.security.entity.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByName(AuthorityName input);
}
