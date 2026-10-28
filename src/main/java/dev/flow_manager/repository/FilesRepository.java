package dev.flow_manager.repository;

import dev.flow_manager.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FilesRepository extends JpaRepository<Files, UUID> {


}
