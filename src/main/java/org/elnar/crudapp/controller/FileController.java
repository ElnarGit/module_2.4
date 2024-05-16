package org.elnar.crudapp.controller;

import lombok.RequiredArgsConstructor;
import org.elnar.crudapp.entity.File;
import org.elnar.crudapp.service.FileService;

import java.util.List;

@RequiredArgsConstructor
public class FileController {
	private final FileService fileService;
	
	public File getFileById(Integer id){
		return fileService.getById(id);
	}
	
	public List<File> getAllFiles(){
		return fileService.getAll();
	}
	
	public File createFile(File file){
		return fileService.save(file);
	}
	
	public File updateFile(File file){
		return fileService.update(file);
	}
	
	public void deleteFileById(Integer id){
		fileService.deleteById(id);
	}
}
