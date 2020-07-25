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
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.servlet.function.RequestPredicates.contentType;

@Controller
@RequestMapping("/home")
public class HomeController {

    private NoteService noteService;
    private UserService userService;
    private CredentialService credentialService;
    private FileService fileService;



    public HomeController(NoteService noteService, CredentialService credentialService, FileService fileService, UserService userService) {
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.fileService = fileService;
        this.userService = userService;


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
            errorMsg = "Your changes were not saved. Try again.";
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
        boolean editError = false;
        String errorMsg = "";
        int rowsDeleted = noteService.deleteNote(note.getNoteId());
        if(rowsDeleted < 0)
        {
            editError = true;
            errorMsg = "Your changes were not saved. Try again.";
        }
        model.addAttribute("notes", noteService.getNotes(note.getUserId()));
        model.addAttribute("editError", editError);
        model.addAttribute("errorMsg", errorMsg);
        return "result";
    }

    @PostMapping("/editNote")
    public String editNote(Note note, Model model)
    {
        boolean editError = false;
        String errorMsg = "";
        int rowsEdited = noteService.editNote(note);
        if(rowsEdited < 0)
        {
            editError = true;
            errorMsg = "Your changes were not saved. Try again.";
        }
        model.addAttribute("notes", noteService.getNotes(note.getUserId()));
        model.addAttribute("editError", editError);
        model.addAttribute("errorMsg", errorMsg);
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
            errorMsg = "Your changes were not saved. Try again";
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
        boolean editError = false;
        String errorMsg = "";
        int rowsDeleted = credentialService.deleteCredential(credential.getCredentialId());
        if(rowsDeleted < 0)
        {
            editError = true;
            errorMsg = "Your changes were not saved. Try again";
        }
        model.addAttribute("credentials", credentialService.getCredentials(credential.getUserId()));
        model.addAttribute("editError", editError);
        model.addAttribute("errorMsg", errorMsg);
        return "result";
    }

    @PostMapping("/editCredential")
    public String editCredential(Credential credential, Model model)
    {
        boolean editError = false;
        String errorMsg = "";
        int rowsEdited = credentialService.editCredential(credential);
        if(rowsEdited < 0)
        {
            editError = true;
            errorMsg = "Your changes were not saved. Try again";
        }
        model.addAttribute("credentials", credentialService.getCredentials(credential.getUserId()));
        model.addAttribute("editError", editError);
        model.addAttribute("errorMsg", errorMsg);
        return "result";
    }
    //got from Manjunath on Udacity mentor help
   @GetMapping(value = "/getDecryptedCredential", produces = MediaType.APPLICATION_JSON_VALUE)
   @ResponseBody
    public Map<String, String> getDecryptedCredential(@RequestParam Integer credentialId){

        Credential credential = credentialService.getCredential(credentialId);
        String decryptedPassword = credentialService.decryptPassword(credential);
        Map<String, String> map = new HashMap();
        map.put("encryptedPassword", credential.getPassword());
        map.put("decryptedPassword", decryptedPassword);
        return map;

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
                errorMsg = "Your changes were not saved. Try again.";
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
        boolean editError = false;
        String errorMsg = "";
        int rowsDeleted = fileService.deleteFile(file.getFileId());
        if(rowsDeleted < 0)
        {
            editError = true;
            errorMsg = "Your changes were not saved. Try again.";
        }
        model.addAttribute("files", fileService.getFiles(file.getUserId()));
        model.addAttribute("editError", editError);
        model.addAttribute("errorMsg", errorMsg);
        return "result";
    }

    //got from udacity forum from Sahil
    @GetMapping("/download/{filename:.+}/db")
    public ResponseEntity downloadFromDB(@PathVariable(value = "filename") String fileName) {
        File file = fileService.getFile(fileName);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(file.getFileData());
    }




}
