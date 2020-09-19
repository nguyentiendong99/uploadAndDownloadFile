package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class Appcontroller {
    @Autowired
    private DocumentRepository repository;
    @GetMapping("/")
    public String Documentation(Model model){
        List<Document> listDocs = repository.findAll();
        model.addAttribute("listDoc" , listDocs);
        return "document";
    }
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("document") MultipartFile multipartFile,
                             RedirectAttributes redirectAttributes) throws IOException {
        String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        Document document = new Document();
        document.setName(filename);
        document.setContent(multipartFile.getBytes());
        document.setSize(multipartFile.getSize());
        document.setUploadTime(new Date());
        repository.save(document);
        redirectAttributes.addFlashAttribute("message" , "the file has upload successfully");
        return "redirect:/";
    }
    @GetMapping("/download")
    public void downloadFile(@RequestParam("id") Long id , HttpServletResponse response) throws Exception {
        Optional<Document> result = repository.findById(id);
        if (!result.isPresent()){
            throw new Exception("could not find document with ID : " + id);
        }
        Document document = result.get();
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment ; filename=" + document.getName();
        response.setHeader(headerKey , headerValue);
        ServletOutputStream sop = response.getOutputStream();
        sop.write(document.getContent());
        sop.close();
    }
}
