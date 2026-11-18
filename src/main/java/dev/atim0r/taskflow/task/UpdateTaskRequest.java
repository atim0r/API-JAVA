package dev.atim0r.taskflow.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTaskRequest(
        @NotBlank @Size(max = 120) String title,
        @Size(max = 1000) String description,
        TaskStatus status,
        TaskPriority priority
) {
}
