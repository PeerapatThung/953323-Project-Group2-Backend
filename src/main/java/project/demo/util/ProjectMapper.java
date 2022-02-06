package project.demo.util;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import project.demo.entity.CoinStock;

import project.demo.security.entity.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(imports = Collectors.class)
public interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);
    List<UserDTO> getUserDTO(List<User> user);
    CoinStock getStock(CoinStock stock);
    UserDTO getUserDto(User user);
//    List<AuthorityDTO> getAuthorityDTO(List<Authority> authorities);
}
