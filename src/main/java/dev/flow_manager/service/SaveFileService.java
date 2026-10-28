package dev.flow_manager.service;


import dev.flow_manager.entity.Files;
import dev.flow_manager.enums.UploadStatus;
import dev.flow_manager.event.FileConversionRequestEvent;
import dev.flow_manager.event.FileConversionResultEvent;
import dev.flow_manager.repository.FilesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveFileService {

    private final FilesRepository filesRepository;
    private final String TOPIC_NAME = "file-conversion-requests";
    private final KafkaTemplate<String, FileConversionRequestEvent> kafkaTemplate;


    public UUID saveResponse(String originalFilename) {

        Files files = new Files();

        UUID uuid = UUID.randomUUID();
        files.setCreatedAt(Instant.now());
        files.setUpdatedAt(Instant.now());
        files.setId(uuid);
        files.setOriginalFilename(originalFilename);
        files.setSourceObjectKey(originalFilename);
        files.setStatus(UploadStatus.UPLOADING);

        filesRepository.save(files);

        FileConversionRequestEvent event = new FileConversionRequestEvent(
                uuid,
                originalFilename,
                Instant.now(),
                Instant.now(),
                Instant.now()
        );

        kafkaTemplate.send(TOPIC_NAME, event);

        return uuid;

    }


    public void saveResult(FileConversionResultEvent result) {
        Files files = filesRepository.findById(result.eventKey())
                .orElseThrow(() -> new RuntimeException("Файл не найден"));

        if ("SUCCESS".equals(result.status())) {
            files.setStatus(UploadStatus.SUCCESS);
            files.setConvertedObjectKey(result.resultFilePath());
            files.setErrorMessage(null);
        } else {
            files.setStatus(UploadStatus.FAILED);
            files.setErrorMessage(result.errorMessage());
        }

        files.setUpdatedAt(Instant.now());
        filesRepository.save(files);

        log.info("Статус файла {} обновлён на {}", result.eventKey(), files.getStatus());
    }

}
