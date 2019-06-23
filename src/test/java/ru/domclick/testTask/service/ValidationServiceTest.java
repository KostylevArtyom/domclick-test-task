package ru.domclick.testTask.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ValidationServiceTest {
    @Autowired
    ValidationService validator;

    @Test
    public void validateAccountIdIsPositive_Positive_Success() {
        validator.validateAccountIdIsPositive(1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateAccountIdIsPositive_NonPositive_ExceptionThrown() {
        validator.validateAccountIdIsPositive(0L);
    }

    @Test
    public void validateAmountIsPositive_Positive_Success() {
        validator.validateAmountIsPositive(BigDecimal.ONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateAmountIsPositive_NonPositive_ExceptionThrown() {
        validator.validateAmountIsPositive(BigDecimal.ZERO);
    }

    @Test
    public void validateAmountIsNonNegative_NonNegative_Success() {
        validator.validateAmountIsNonNegative(BigDecimal.ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateAmountIsNonNegative_Negative_ExceptionThrown() {
        validator.validateAmountIsNonNegative(BigDecimal.ONE.negate());
    }

    @Test
    public void validateSenderAndRecipientAreDifferent_Different_Success() {
        validator.validateSenderAndRecipientAreDifferent(1L, 2L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateSenderAndRecipientAreDifferent_Identical_ExceptionThrown() {
        validator.validateSenderAndRecipientAreDifferent(1L, 1L);
    }
}
