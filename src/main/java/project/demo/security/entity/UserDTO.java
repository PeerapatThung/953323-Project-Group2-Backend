package project.demo.security.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.demo.entity.Student;
import project.demo.entity.StudentDTO;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    StudentDTO member;
    List<AuthorityDTO> authorities;
    String id;
}
