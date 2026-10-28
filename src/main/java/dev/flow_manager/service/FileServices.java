package dev.flow_manager.service;


import dev.flow_manager.dto.ResponseIdDto;
import dev.flow_manager.dto.ResponseStatusDto;
import dev.flow_manager.entity.File;
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
    public ResponseIdDto convertFile(MultipartFile file) throws Exception {

        String originalName = file.getOriginalFilename();

        minioService.uploadFile(
                originalName,
                file.getInputStream(),
                file.getSize()
        );

        return new ResponseIdDto(saveFileService.saveResponse(originalName));


    }

    public ResponseStatusDto getStatus(UUID id) {

        File file = filesRepository.findById(id).orElseThrow(() -> new NotFoundException("Такого id не существует"));

        return new ResponseStatusDto(file.getStatus().toString());
    }


    public Resource getFile(UUID id) throws Exception {
        File file = filesRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Такого id не существует"));

        if (file.getConvertedObjectKey() == null) {
            throw new NotFoundException("Сконвертированный файл ещё не готов");
        }

        InputStream inputStream =
                minioService.downloadFile(file.getConvertedObjectKey());

        return new InputStreamResource(inputStream);
    }

}
