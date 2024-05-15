package org.elnar.crudapp.service.impl;

import lombok.RequiredArgsConstructor;
import org.elnar.crudapp.entity.File;
import org.elnar.crudapp.repository.FileRepository;
import org.elnar.crudapp.service.FileService;

import java.util.List;

@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
	private final FileRepository fileRepository;
	
	@Override
	public File getById(Integer id) {
		return fileRepository.getById(id);
	}
	
	@Override
	public List<File> getAll() {
		return fileRepository.getAll();
	}
	
	@Override
	public File save(File file) {
		return fileRepository.save(file);
	}
	
	@Override
	public File update(File file) {
		return fileRepository.update(file);
	}
	
	@Override
	public void deleteById(Integer id) {
		fileRepository.deleteById(id);
	}
}
