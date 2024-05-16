package org.elnar.crudapp.converter;

import org.elnar.crudapp.dto.UserDTO;
import org.elnar.crudapp.entity.User;

public class UserConverter {
	public static UserDTO toDTO(User user){
		return new UserDTO(user.getId(), user.getName());
	}
}
