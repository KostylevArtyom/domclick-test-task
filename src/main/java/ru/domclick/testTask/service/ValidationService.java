package ru.domclick.testTask.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.domclick.testTask.utils.ExceptionMessages;

import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class ValidationService {
    public void validateAccountIdIsPositive(Long id) {
        if (id <= 0) {
            throw new IllegalArgumentException(ExceptionMessages.NON_POSITIVE_ID);
        }
    }

    public void validateAmountIsPositive(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) != 1) {
            throw new IllegalArgumentException(ExceptionMessages.NON_POSITIVE_AMOUNT);
        }
    }

    public void validateAmountIsNonNegative(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) == -1) {
            throw new IllegalArgumentException(ExceptionMessages.INSUFFICIENT_AMOUNT);
        }
    }

    public void validateSenderAndRecipientAreDifferent(Long sender, Long recipient) {
        if (sender.equals(recipient)) {
            throw new IllegalArgumentException(ExceptionMessages.SENDER_EQUALS_TO_RECIPIENT);
        }
    }
}
