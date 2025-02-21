package com.lshwan.hof.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lshwan.hof.domain.entity.prod.Prod;
import com.lshwan.hof.service.S3Service;
import com.lshwan.hof.service.prod.ProdService;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("file")
@Log4j2
public class FileController {
    @Autowired
    private S3Service s3Service;
    @Autowired
    private ProdService prodService;

    @SuppressWarnings("null")
    @PostMapping("upload")
    public ResponseEntity<?> upload(@RequestParam("file") List<MultipartFile> files) {
        
         List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            // // S3 업로드
            // s3Service.settingFile(file,"uploads");

            String url = s3Service.settingFile(file, "prodDetail");
            urls.add(url);
            
        }
        return ResponseEntity.ok().body(urls);
    }
    @PostMapping("popup/upload")
    public ResponseEntity<String> uploadPopupImage(@RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("파일이 비어 있습니다.");
        }

        try {
            String url = s3Service.settingFile(file, "popup"); 
            return ResponseEntity.ok(url); 
        } catch (Exception e) {
            log.error("파일 업로드 실패", e);
            return ResponseEntity.internalServerError().body("파일 업로드 실패");
        }
    }
    @PostMapping("upload/{pno}")
    public ResponseEntity<?> upload(@RequestParam("file") List<MultipartFile> files,@PathVariable("pno") Long pno) {
        Prod prod = prodService.findBy(pno);
         List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            // // S3 업로드
            // s3Service.settingFile(file,"uploads");

            String url = s3Service.settingFile(file, "prodDetail",prod);
            urls.add(url);
            
        }
        return ResponseEntity.ok().body(urls);
    }
    @PostMapping("upload/thumnail")
    public ResponseEntity<?> uploadSumnail(@RequestParam("file") MultipartFile file) {
        System.out.println(file);
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("파일이 비어 있습니다.");
        }
        try {
            String url = s3Service.settingFile(file, "prod"); 
            return ResponseEntity.ok(url); 
        } catch (Exception e) {
            log.error("파일 업로드 실패", e);
            return ResponseEntity.internalServerError().body("파일 업로드 실패");
        }
    }
    
    
}
