package dev.flow_manager.listener;


import dev.flow_manager.event.FileConversionResultEvent;
import dev.flow_manager.service.SaveFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileResultListener {
    private static final String TOPIC_NAME = "file-conversion-result";
    private final ObjectMapper objectMapper;
    private final SaveFileService dataBaseFileService;


    @KafkaListener(
            topics = TOPIC_NAME,
            groupId = "flow-manager-group"
    )

    public void handleFileConversionResultEvent(
            FileConversionResultEvent result) throws Exception {

        dataBaseFileService.saveResult(result);
    }

}
