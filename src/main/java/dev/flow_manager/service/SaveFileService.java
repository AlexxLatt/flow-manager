package dev.flow_manager.service;


import dev.flow_manager.entity.File;
import dev.flow_manager.enums.UploadStatus;
import dev.flow_manager.event.FileConversionRequestEvent;
import dev.flow_manager.event.FileConversionResultEvent;
import dev.flow_manager.repository.FilesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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

        File file = new File();

        UUID uuid = UUID.randomUUID();
        file.setCreatedAt(Instant.now());
        file.setUpdatedAt(Instant.now());
        file.setId(uuid);
        file.setOriginalFilename(originalFilename);
        file.setSourceObjectKey(originalFilename);
        file.setStatus(UploadStatus.UPLOADING);

        filesRepository.save(file);

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
        File file = filesRepository.findById(result.eventKey())
                .orElseThrow(() -> new RuntimeException("Файл не найден"));

        if ("SUCCESS".equals(result.status())) {
            file.setStatus(UploadStatus.SUCCESS);
            file.setConvertedObjectKey(result.resultFilePath());
            file.setErrorMessage(null);
        } else {
            file.setStatus(UploadStatus.FAILED);
            file.setErrorMessage(result.errorMessage());
        }

        file.setUpdatedAt(Instant.now());
        filesRepository.save(file);

        log.info("Статус файла {} обновлён на {}", result.eventKey(), file.getStatus());
    }

}
