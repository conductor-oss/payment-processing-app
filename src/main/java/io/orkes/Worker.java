package io.orkes;

import com.netflix.conductor.sdk.workflow.executor.task.NonRetryableException;
import com.netflix.conductor.sdk.workflow.task.InputParam;
import com.netflix.conductor.sdk.workflow.task.WorkerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
/**
 * Service workers used by our payment app
 * Note: These workers could be in the same app or different app and written in different languages
 * Workers typically run in a distributed env across multiple containers/machines to scale based on the workload
 */
@Slf4j
public class Worker {
    @WorkerTask(value = "validate_payment_details")
    public void validatePaymentDetails(PaymentTransferRequest request) throws Exception {
        if(request.getAmount().doubleValue() < 0) {
            throw new NonRetryableException("Invalid amount " + request.getAmount());
        }
        int random = new Random().nextInt(10);
        //Change the condition to increase/decrease failures
        if(random < 10) {
            throw new RuntimeException("Can't call the API to validate payment details");
        }

        //all OK
    }

    @WorkerTask(value = "debit_account")
    public void debit(@InputParam("accountId") String accountId, @InputParam("amount")BigDecimal amount) throws Exception {
        //todo: Implement the logic to debit money from the account
    }

    @WorkerTask(value = "credit_account")
    public void credit(@InputParam("accountId") String accountId, @InputParam("amount")BigDecimal amount) throws Exception {
        //todo: Implement the logic to credit money from the account
    }

    @WorkerTask(value = "send_notification")
    public void sendNotification(@InputParam("fromAccountId") String fromAccountId,
                                 @InputParam("toAccountId") String toAccountId,
                                 @InputParam("status") String status,
                                 @InputParam("paymentReference") String paymentReference,
                                 @InputParam("amount")BigDecimal amount) throws Exception {

        log.info("Payment of {} from account {} to {} has been {}", amount, fromAccountId, toAccountId, status);
    }

}
