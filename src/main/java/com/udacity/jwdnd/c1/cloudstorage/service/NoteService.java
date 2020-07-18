package com.udacity.jwdnd.c1.cloudstorage.service;

import com.udacity.jwdnd.c1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.c1.cloudstorage.model.Note;
import com.udacity.jwdnd.c1.cloudstorage.model.NoteForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int addNote(NoteForm noteForm)
    {
        Note newNote = new Note();
        newNote.setNoteTitle(noteForm.getNoteTitle());
        newNote.setNoteDescription(noteForm.getNoteDescription());
        newNote.setUserId(noteForm.getUserId());
        return noteMapper.insert(newNote);
    }

    public void deleteNote(Integer noteId)
    {
        noteMapper.deleteNote(noteId);
    }

    public void editNote(Note note)
    {
        noteMapper.editNote(note);
    }

    public List<Note> getNotes(Integer userId)
    {
        return noteMapper.getAllNotes(userId);
    }
}
