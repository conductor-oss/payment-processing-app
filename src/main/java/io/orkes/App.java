package io.orkes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.client.http.MetadataClient;
import com.netflix.conductor.common.config.ObjectMapperProvider;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    private final MetadataClient metadataClient;
    private final ObjectMapper objectMapper = new ObjectMapperProvider().getObjectMapper();
    private static final TypeReference<List<TaskDef>> LIST_OF_TASK_DEF = new TypeReference<List<TaskDef>>() {};
    @PostConstruct
    public void init() throws IOException {

        InputStream is = getClass().getResourceAsStream("/failure-workflow-v1.json");
        WorkflowDef workflowDef = objectMapper.readValue(new InputStreamReader(is), WorkflowDef.class);
        metadataClient.updateWorkflowDefs(List.of(workflowDef));

        is = getClass().getResourceAsStream("/workflow-v1.json");
        workflowDef = objectMapper.readValue(new InputStreamReader(is), WorkflowDef.class);
        log.info("failure workflow is set to {}", workflowDef.getFailureWorkflow());
        metadataClient.updateWorkflowDefs(List.of(workflowDef));

        is = getClass().getResourceAsStream("/tasks.json");
        List<TaskDef> taskDefs = objectMapper.readValue(new InputStreamReader(is), LIST_OF_TASK_DEF);
        metadataClient.registerTaskDefs(taskDefs);

    }
}

