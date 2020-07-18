package com.udacity.jwdnd.c1.cloudstorage.mapper;

import com.udacity.jwdnd.c1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid)" +
            "VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    public int insert(Note note);

    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    public List<Note> getAllNotes(Integer userId);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    public void deleteNote(Integer noteId);

    @Update("UPDATE NOTES SET notetitle = #{noteTitle}, notedescription = #{noteDescription} WHERE noteid = #{noteId}")
    public void editNote(Note note);


}
