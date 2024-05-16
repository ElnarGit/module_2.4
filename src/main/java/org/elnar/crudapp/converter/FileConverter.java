package org.elnar.crudapp.converter;

import org.elnar.crudapp.dto.FileDTO;
import org.elnar.crudapp.entity.File;

public class FileConverter {
	public static FileDTO toDTO(File file){
		return new FileDTO(file.getId(), file.getName(), file.getFilePath());
	}
}
