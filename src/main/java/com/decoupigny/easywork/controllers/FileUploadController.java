package com.decoupigny.easywork.controllers;

import com.decoupigny.easywork.models.file.FileInfo;
import com.decoupigny.easywork.models.file.Response;
import com.decoupigny.easywork.services.FilesStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class FileUploadController {

    @Autowired
    FilesStoreService storageService;

    @PostMapping("/upload")
    public ResponseEntity<Response> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("ticketId") String ticketId){
        String message = "";
        try {
            storageService.save(file,ticketId);
            message = "File successfully uploaded: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new Response(message));
        } catch (Exception e) {
            System.out.println(e);
            //System.out.println(e);
            message = "Could not upload the file: " + file.getOriginalFilename() + "!" ;
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new Response(message));
        }
    }

    @GetMapping("/getFiles/{folderName}")
    public ResponseEntity<List<FileInfo>> getListFiles(@PathVariable String folderName) {
        List<FileInfo> fileInfos = storageService.loadAll(folderName).map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FileUploadController.class, "getFile", path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
