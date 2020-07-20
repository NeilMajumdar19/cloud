package com.udacity.jwdnd.c1.cloudstorage.controller;

import com.udacity.jwdnd.c1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.c1.cloudstorage.model.Credential;
import com.udacity.jwdnd.c1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.c1.cloudstorage.model.Note;
import com.udacity.jwdnd.c1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.c1.cloudstorage.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Controller
@RequestMapping("/home")
public class HomeController {

    private NoteService noteService;
    private UserService userService;
    private CredentialService credentialService;
    private FileService fileService;
    private EncryptionService encryptionService;
    private CredentialMapper credentialMapper;

    public HomeController(NoteService noteService, CredentialService credentialService, FileService fileService, EncryptionService encryptionService, UserService userService, CredentialMapper credentialMapper) {
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.fileService = fileService;
        this.encryptionService = encryptionService;
        this.userService = userService;
        this.credentialMapper = credentialMapper;

    }

    @GetMapping
    public String getHomePage(Authentication authentication, NoteForm noteForm, CredentialForm credentialForm, Model model)
    {
        model.addAttribute("notes", noteService.getNotes(userService.getUser(authentication.getName()).getUserId()));
        model.addAttribute("credentials", credentialService.getCredentials(userService.getUser(authentication.getName()).getUserId()));
        model.addAttribute("files", fileService.getFiles(userService.getUser(authentication.getName()).getUserId()));
        return "home";
    }

    @PostMapping("/addNote")
    public String addNote(Authentication authentication, NoteForm noteForm, Model model)
    {
        boolean editError = false;
        noteForm.setUserId(userService.getUser(authentication.getName()).getUserId());
        int rowsAdded = noteService.addNote(noteForm);
        if(rowsAdded < 0)
            editError = true;
        noteForm.setNoteTitle("");
        noteForm.setNoteDescription("");
        model.addAttribute("notes", noteService.getNotes(noteForm.getUserId()));
        model.addAttribute("editError", editError);
        return "result";
    }

    @PostMapping("/deleteNote")
    public String deleteNote(Note note, Model model)
    {
        noteService.deleteNote(note.getNoteId());
        model.addAttribute("notes", noteService.getNotes(note.getUserId()));
        return "result";
    }

    @PostMapping("/editNote")
    public String editNote(Note note, Model model)
    {
        noteService.editNote(note);
        model.addAttribute("notes", noteService.getNotes(note.getUserId()));
        return "result";
    }

    @PostMapping("/addCredential")
    public String addCredential(Authentication authentication, CredentialForm credentialForm, Model model)
    {
        boolean editError = false;
        credentialForm.setUserId(userService.getUser(authentication.getName()).getUserId());
        int rowsAdded = credentialService.addCredential(credentialForm);
        if(rowsAdded < 0)
            editError = true;
        credentialForm.setUrl("");
        credentialForm.setUsername("");
        credentialForm.setPassword("");
        model.addAttribute("credentials", credentialService.getCredentials(credentialForm.getUserId()));
        model.addAttribute("editError", editError);
        return "result";
    }

    @PostMapping("/deleteCredential")
    public String deleteCredential(Credential credential, Model model)
    {
        credentialService.deleteCredential(credential.getCredentialId());
        model.addAttribute("credentials", credentialService.getCredentials(credential.getUserId()));
        return "result";
    }

    @PostMapping("/editCredential")
    public String editCredential(Credential credential, Model model)
    {
        credentialService.editCredential(credential);
        model.addAttribute("credentials", credentialService.getCredentials(credential.getUserId()));
        return "result";
    }

   @GetMapping(value = "/getDecryptedCredential")
   @ResponseBody
    public String getDecryptedCredential(@RequestParam Integer credentialId){

        Credential credential = credentialMapper.getCredential(credentialId);
        return encryptionService.decryptValue(credential.getPassword(), credential.getKey());

    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("fileUpload")MultipartFile fileUpload, Authentication authentication, Model model) throws IOException {
        boolean editError = false;
        Integer userId = userService.getUser(authentication.getName()).getUserId();
        int rowsAdded = fileService.uploadFile(fileUpload, userId);
        if(rowsAdded < 0)
            editError = true;
        model.addAttribute("files", fileService.getFiles(userId));
        model.addAttribute("editError", editError);
        return "result";
    }



}
