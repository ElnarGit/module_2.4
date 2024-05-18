package org.elnar.crudapp.mapper;

import org.elnar.crudapp.dto.UserDTO;
import org.elnar.crudapp.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
	
	UserDTO userToUserDTO(User user);
	
	User userDTOToUser(UserDTO userDTO);

}
