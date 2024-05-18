package org.elnar.crudapp.mapper;

import org.elnar.crudapp.dto.FileDTO;
import org.elnar.crudapp.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FileMapper {
	FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);
	
	FileDTO fileToFileDTO(File file);
	
	File fileDTOToFile(FileDTO fileDTO);
}
