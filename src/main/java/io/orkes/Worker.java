package io.orkes;

import com.netflix.conductor.sdk.workflow.task.WorkerTask;
import org.springframework.stereotype.Service;

@Service
public class Worker {
    @WorkerTask(value = "payment-hold")
    public String paymentHold() throws Exception {
        // if (1==2) throw new Exception("failed to execute worker");
        return "Payment Hold";
    }

    @WorkerTask(value = "debit")
    public String debit() throws Exception {
        return "Debit";
    }

    @WorkerTask(value = "credit")
    public String credit() throws Exception {
        return "Credit";
    }
}
