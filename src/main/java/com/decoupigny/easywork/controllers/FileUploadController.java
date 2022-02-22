package com.decoupigny.easywork.controllers;

import com.decoupigny.easywork.models.file.FileInfo;
import com.decoupigny.easywork.models.file.Response;
import com.decoupigny.easywork.services.FilesStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/file")
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    FilesStoreService storageService;

    @PostMapping("/upload")
    public ResponseEntity<Response> uploadFile(@RequestParam("file") MultipartFile file){
        String message;
        try {
            storageService.save(file);
            message = "File successfully uploaded: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new Response(message));
        } catch (Exception e) {
            logger.error(e.getMessage());
            message = "Could not upload the file: " + file.getOriginalFilename() + "!" ;
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new Response(message));
        }
    }

    @GetMapping("/getAll/{folderName}")
    public ResponseEntity<List<FileInfo>> getListFiles(@PathVariable String folderName) {
        List<FileInfo> fileInfos = storageService.loadAll(folderName).map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FileUploadController.class, "getFile", path.getFileName().toString(),path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }

    @GetMapping("/getOne/{folderName}/{filename:.+}")
    public ResponseEntity<File> getFile(@PathVariable String folderName, @PathVariable String filename) throws IOException {
        Resource file = storageService.load(folderName +'/'+ filename);
        return ResponseEntity.ok().body(file.getFile());
    }
}
