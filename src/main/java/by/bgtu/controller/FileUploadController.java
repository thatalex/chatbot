package by.bgtu.controller;

import by.bgtu.model.Answer;
import by.bgtu.model.KeyWord;
import by.bgtu.model.Subject;
import by.bgtu.service.AdminService;
import by.bgtu.service.JSONService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

/**
 * controller for importing/exporting of files
 */
@Controller
public class FileUploadController {

    private final JSONService jsonService;

    private final AdminService adminService;

    @Autowired
    public FileUploadController(JSONService jsonService, AdminService adminService) {
        this.jsonService = jsonService;
        this.adminService = adminService;
    }

    @GetMapping("/admin/export/subjects")
    @ResponseBody
    public ResponseEntity<Resource> fileExportSubjects() {
        Subject[] subjects = adminService.getSubjects().toArray(new Subject[]{});
        Resource resource;
        try {
            resource = jsonService.getResource(subjects);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header(HttpHeaders.CONTENT_DISPOSITION).build();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=subjects.txt").body(resource);
    }

    @PostMapping("/admin/import/subjects")
    public String fileImportSubjects(@RequestParam("file") MultipartFile file,
                                     RedirectAttributes redirectAttributes) {

        try {
            Subject[] subjects = jsonService.getSubjects(file);
            if (subjects != null) {
                for (Subject subject : subjects) {
                    adminService.save(subject);
                }
            }
            redirectAttributes.addFlashAttribute("viewMessage", "Файл успешно импортирован");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("viewMessage", e.toString());
        }
        return "redirect:/admin/subjects";
    }


    @GetMapping("/admin/export/keywords")
    @ResponseBody
    public ResponseEntity<Resource> fileExportKeywords() {
        KeyWord[] keyWords = adminService.getKeyWords().toArray(new KeyWord[]{});
        Resource resource;
        try {
            resource = jsonService.getResource(keyWords);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header(HttpHeaders.CONTENT_DISPOSITION).build();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=keywords.txt").body(resource);
    }

    @PostMapping("/admin/import/keywords")
    public String fileImportKeyWords(@RequestParam("file") MultipartFile file,
                                     RedirectAttributes redirectAttributes) {

        try {
            KeyWord[] keywords = jsonService.getKeyWords(file);
            if (keywords != null) {
                for (KeyWord keyWord : keywords) {
                    adminService.save(keyWord);
                }
            }
            redirectAttributes.addFlashAttribute("viewMessage", "Файл успешно импортирован");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("viewMessage", e.toString());
        }
        return "redirect:/admin/keywords";
    }


    @GetMapping("/admin/export/answers")
    @ResponseBody
    public ResponseEntity<Resource> fileExportAnswers() {
        Answer[] answers = adminService.getAnswers().toArray(new Answer[]{});
        Resource resource;
        try {
            resource = jsonService.getResource(answers);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header(HttpHeaders.CONTENT_DISPOSITION).build();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=answers.txt").body(resource);
    }


    @PostMapping("/admin/import/answers")
    public String fileImportAnswers(@RequestParam("file") MultipartFile file,
                                     RedirectAttributes redirectAttributes) {

        try {
            Answer[] answers = jsonService.getAnswers(file);
            if (answers != null) {
                for (Answer answer : answers) {
                    adminService.save(answer);
                }
            }
            redirectAttributes.addFlashAttribute("viewMessage", "Файл успешно импортирован");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("viewMessage", e.toString());
        }
        return "redirect:/admin/answers";
    }



}