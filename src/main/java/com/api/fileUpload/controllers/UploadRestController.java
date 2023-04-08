package com.api.fileUpload.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;

@RestController
@RequestMapping("/api/demo")
public class UploadRestController implements ServletContextAware {

    private ServletContext servletContext;

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public ResponseEntity<Void> upload(@RequestParam("files") MultipartFile[] files) {
        try {
            System.out.println("File List");
            for (MultipartFile file : files) {
                System.out.println("File Name: " + file.getOriginalFilename());
                System.out.println("File Size: " + file.getSize());
                System.out.println("File type: " + file.getContentType());
                System.out.println("------------------------------------------");
                String response = save(file);
                System.out.println(response);
            }

            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
    }

    public String save(MultipartFile file) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
            String newFileName = simpleDateFormat.format(new Date()) + file.getOriginalFilename();
            byte[] bytes = file.getBytes();
            Path path = Paths.get(this.servletContext.getRealPath("/uploads/images/" + newFileName));
            Files.write(path, bytes);
            return newFileName;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
