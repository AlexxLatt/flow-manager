package dev.flow_manager.controller;


import dev.flow_manager.dto.ResponseIdDto;
import dev.flow_manager.dto.ResponseStatusDto;
import dev.flow_manager.service.FileServices;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileServices fileServices;


    @PostMapping(consumes = "multipart/form-data")
    public ResponseIdDto uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        return fileServices.convertFile(file);
    }


    @GetMapping("/file/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable UUID id) throws Exception {
        Resource resource = fileServices.getFile(id);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"converted-file.pdf\""
                )
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/status/{id}")
    public ResponseStatusDto getStatus(@PathVariable UUID id) {
        return fileServices.getStatus(id);

    }

}
