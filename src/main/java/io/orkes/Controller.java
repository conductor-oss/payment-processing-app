package io.orkes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.client.http.WorkflowClient;
import com.netflix.conductor.common.metadata.workflow.StartWorkflowRequest;
import com.netflix.conductor.common.run.Workflow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@Slf4j
public class Controller {

    private final String workflowName;
    private final Integer workflowVersion;
    private final WorkflowClient workflowClient;
    private final ObjectMapper objectMapper;

    public Controller(@Value("${payment_workflow_name:payment_transfer}") String workflowName,
                      @Value("${payment_workflow_ver:1}") Integer workflowVersion,
                      WorkflowClient workflowClient,
                      ObjectMapper objectMapper) {
        this.workflowName = workflowName;
        this.workflowVersion = workflowVersion;
        this.workflowClient = workflowClient;
        this.objectMapper = objectMapper;

        log.info("Using workflow {}:{}", workflowName, workflowVersion);
    }

    @PostMapping
    public String initiatePaymentTransfer(@RequestBody PaymentTransferRequest request) {
        StartWorkflowRequest startWorkflowRequest = new StartWorkflowRequest();
        startWorkflowRequest.setName(workflowName);
        startWorkflowRequest.setVersion(workflowVersion);
        startWorkflowRequest.setInput(objectMapper.convertValue(request, Map.class));
        return workflowClient.startWorkflow(startWorkflowRequest);
    }

    @GetMapping("/{workflowId}")
    public ResponseEntity<Workflow> getTransferStatus(@PathVariable("workflowId") String workflowId) {
        var workflow = workflowClient.getWorkflow(workflowId, true);
        return ResponseEntity.ok(workflow);
    }
}
