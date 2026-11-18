package dev.atim0r.taskflow.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createListUpdateAndDeleteTask() throws Exception {
        CreateTaskRequest createRequest =
                new CreateTaskRequest("Deploy API", "Prepare the production environment", TaskPriority.HIGH);

        String created = mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Deploy API")))
                .andExpect(jsonPath("$.status", is("TODO")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        TaskResponse createdTask = objectMapper.readValue(created, TaskResponse.class);

        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        UpdateTaskRequest updateRequest =
                new UpdateTaskRequest("Deploy API", "Production checklist completed", TaskStatus.DONE, TaskPriority.HIGH);

        mockMvc.perform(put("/api/v1/tasks/{id}", createdTask.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("DONE")));

        mockMvc.perform(delete("/api/v1/tasks/{id}", createdTask.id()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/tasks/{id}", createdTask.id()))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectsBlankTitle() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest(" ", null, null);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.title").exists());
    }
}
