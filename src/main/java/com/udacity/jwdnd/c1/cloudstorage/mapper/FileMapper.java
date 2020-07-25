package com.udacity.jwdnd.c1.cloudstorage.mapper;

import com.udacity.jwdnd.c1.cloudstorage.model.File;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileMapper {

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
            "VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    public int uploadFile(File file);

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    public List<File> getAllFiles(Integer userId);


    @Select("SELECT * FROM FILES WHERE filename = #{fileName}")
    public File getFile(String fileName);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    public int deleteFile(Integer fileId);

    @Select("SELECT * FROM FILES WHERE filename = #{fileName} AND userid = #{userId}")
    public File getFileById(String fileName, Integer userId);



}
