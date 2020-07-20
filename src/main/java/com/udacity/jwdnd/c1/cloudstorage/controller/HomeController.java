package com.udacity.jwdnd.c1.cloudstorage.controller;

import com.udacity.jwdnd.c1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.c1.cloudstorage.model.*;
import com.udacity.jwdnd.c1.cloudstorage.service.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.swing.text.Document;
import java.io.IOException;

import static org.springframework.web.servlet.function.RequestPredicates.contentType;

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
        String errorMsg = "";
        noteForm.setUserId(userService.getUser(authentication.getName()).getUserId());
        int rowsAdded = noteService.addNote(noteForm);
        if(rowsAdded < 0)
        {
            editError = true;
            errorMsg = "Your changes were not saved.";
        }

        noteForm.setNoteTitle("");
        noteForm.setNoteDescription("");
        model.addAttribute("notes", noteService.getNotes(noteForm.getUserId()));
        model.addAttribute("editError", editError);
        model.addAttribute("errorMsg", errorMsg);
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
        String errorMsg = "";
        credentialForm.setUserId(userService.getUser(authentication.getName()).getUserId());
        int rowsAdded = credentialService.addCredential(credentialForm);
        if(rowsAdded < 0)
        {
            editError = true;
            errorMsg = "Your changes were not saved.";
        }
        credentialForm.setUrl("");
        credentialForm.setUsername("");
        credentialForm.setPassword("");
        model.addAttribute("credentials", credentialService.getCredentials(credentialForm.getUserId()));
        model.addAttribute("editError", editError);
        model.addAttribute("errorMsg", errorMsg);
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
        String errorMsg = "";
        Integer userId = userService.getUser(authentication.getName()).getUserId();
        if(fileUpload.isEmpty())
        {
            editError = true;
            errorMsg = "File not chosen.";
        }

        else if(fileService.fileNameExists(fileUpload.getOriginalFilename()))
        {
            editError = true;
            errorMsg = "Cannot upload more than one file with the same name.";
        }
        else
        {
            int rowsAdded = fileService.uploadFile(fileUpload, userId);
            if(rowsAdded < 0) {
                editError = true;
                errorMsg = "Your changes were not saved.";
            }
        }
        model.addAttribute("files", fileService.getFiles(userId));
        model.addAttribute("editError", editError);
        model.addAttribute("errorMsg", errorMsg);
        return "result";
    }

    @PostMapping("/deleteFile")
    public String deleteFile(File file, Model model)
    {
        fileService.deleteFile(file.getFileId());
        model.addAttribute("files", fileService.getFiles(file.getUserId()));
        model.addAttribute("fileNotChosen", false);
        return "result";
    }


    @GetMapping("/download/{filename:.+}/db")
    public ResponseEntity downloadFromDB(@PathVariable(value = "filename") String fileName) {
        File file = fileService.getFile(fileName);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(file.getFileData());
    }




}
