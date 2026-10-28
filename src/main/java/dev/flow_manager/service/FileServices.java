package dev.flow_manager.service;


import dev.flow_manager.entity.Files;
import dev.flow_manager.exception.NotFoundException;
import dev.flow_manager.repository.FilesRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServices {

    private final MinioService minioService;
    private final SaveFileService saveFileService;
    private final FilesRepository filesRepository;


    @Transactional
    public UUID toConvert(MultipartFile file) throws Exception {

        String originalName = file.getOriginalFilename();

        minioService.uploadFile(
                originalName,
                file.getInputStream(),
                file.getSize()
        );

        return saveFileService.saveResponse(originalName);


    }

    public String getStatus(UUID id) {

        Files files = filesRepository.findById(id).orElseThrow(() -> new NotFoundException("Такого id не существует"));

        return files.getStatus().toString();
    }


    public Resource getFile(UUID id) throws Exception {
        Files files = filesRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Такого id не существует"));

        if (files.getConvertedObjectKey() == null) {
            throw new NotFoundException("Сконвертированный файл ещё не готов");
        }

        InputStream inputStream =
                minioService.downloadFile(files.getConvertedObjectKey());

        return new InputStreamResource(inputStream);
    }

}
