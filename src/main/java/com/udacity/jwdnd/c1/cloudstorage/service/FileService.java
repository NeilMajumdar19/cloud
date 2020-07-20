package com.udacity.jwdnd.c1.cloudstorage.service;

import com.udacity.jwdnd.c1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.c1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public int uploadFile(MultipartFile fileUpload, Integer userId) throws IOException {
        File newFile = new File();
        newFile.setFileName(fileUpload.getOriginalFilename());
        newFile.setContentType(fileUpload.getContentType());
        newFile.setFileSize(String.valueOf(fileUpload.getSize()));
        newFile.setUserId(userId);
        newFile.setFileData(fileUpload.getBytes());
        return fileMapper.uploadFile(newFile);
    }

    public void deleteFile(Integer fileId)
    {
        fileMapper.deleteFile(fileId);
    }

    public List<File> getFiles(Integer userId)
    {
        return fileMapper.getAllFiles(userId);
    }

    public boolean fileNameExists(String fileName)
    {
        return fileMapper.getFile(fileName) != null;
    }

    public File getFile(String filename)
    {
        return fileMapper.getFile(filename);
    }
}
