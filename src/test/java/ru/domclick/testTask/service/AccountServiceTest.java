package ru.domclick.testTask.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.domclick.testTask.model.Account;
import ru.domclick.testTask.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {
    @MockBean
    private AccountRepository repository;

    @MockBean
    private ValidationService validator;

    @Autowired
    AccountService service;

    @Test
    public void get_ExistingId_Success() {
        final Long id = 1L;
        Mockito.when(repository.findById(id))
            .thenReturn(Optional.of(new Account(id, BigDecimal.ZERO)));
        Account account = service.get(id);
        Assert.assertEquals(account.getAmount(), BigDecimal.ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void get_NonExistingId_ExceptionThrown() {
        final Long id = 1L;
        Mockito.when(repository.findById(id))
            .thenReturn(Optional.empty());
        service.get(id);
    }

    @Test
    public void replenish_ExistingIdAndPositiveAmount_Success() {
        final Long id = 1L;
        Mockito.when(repository.findById(id))
            .thenReturn(Optional.of(new Account(id, BigDecimal.ZERO)));
        service.replenish(id, BigDecimal.ONE);
        Account account = service.get(id);
        Assert.assertEquals(account.getAmount(), BigDecimal.ONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void replenish_NonExistingIdAndPositiveAmount_ExceptionThrown() {
        final Long id = 1L;
        Mockito.when(repository.findById(id))
            .thenReturn(Optional.empty());
        service.replenish(id, BigDecimal.ONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void replenish_ExistingIdAndZeroAmount_ExceptionThrown() {
        final Long id = 1L;
        Mockito.doThrow(new IllegalArgumentException())
            .when(validator)
            .validateAmountIsPositive(BigDecimal.ZERO);
        Mockito.when(repository.findById(id))
            .thenReturn(Optional.of(new Account(id, BigDecimal.ZERO)));
        service.replenish(id, BigDecimal.ZERO);
    }

    @Test
    public void withdraw_ExistingIdAndPositiveAmount_Success() {
        final Long id = 1L;
        Mockito.when(repository.findById(id))
            .thenReturn(Optional.of(new Account(id, BigDecimal.ONE)));
        service.withdraw(id, BigDecimal.ONE);
        Account account = service.get(id);
        Assert.assertEquals(account.getAmount(), BigDecimal.ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void withdraw_NonExistingIdAndPositiveAmount_ExceptionThrown() {
        final Long id = 1L;
        Mockito.when(repository.findById(id))
            .thenReturn(Optional.empty());
        service.withdraw(id, BigDecimal.ONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void withdraw_ExistingIdAndZeroAmount_ExceptionThrown() {
        final Long id = 1L;
        Mockito.doThrow(new IllegalArgumentException())
            .when(validator)
            .validateAmountIsPositive(BigDecimal.ZERO);
        Mockito.when(repository.findById(id))
            .thenReturn(Optional.of(new Account(id, BigDecimal.ONE)));
        service.withdraw(id, BigDecimal.ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void withdraw_ExistingIdAndExceededAmount_ExceptionThrown() {
        final Long id = 1L;
        Mockito.doThrow(new IllegalArgumentException())
            .when(validator)
            .validateAmountIsNonNegative(BigDecimal.ONE.negate());
        Mockito.when(repository.findById(id))
            .thenReturn(Optional.of(new Account(id, BigDecimal.ZERO)));
        service.withdraw(id, BigDecimal.ONE);
    }

    @Test
    public void transfer_ExistingIdsAndPositiveAmount_Success() {
        final Long sender = 1L;
        final Long recipient = 2L;
        Mockito.when(repository.findById(sender))
            .thenReturn(Optional.of(new Account(sender, BigDecimal.ONE)));
        Mockito.when(repository.findById(recipient))
            .thenReturn(Optional.of(new Account(recipient, BigDecimal.ONE)));
        service.transfer(sender, recipient, BigDecimal.ONE);
        Account first = service.get(sender);
        Account second = service.get(recipient);
        Assert.assertEquals(first.getAmount(), BigDecimal.ZERO);
        Assert.assertEquals(second.getAmount(), BigDecimal.valueOf(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void transfer_NonExistingSender_ExceptionThrown() {
        final Long sender = 1L;
        final Long recipient = 2L;
        Mockito.when(repository.findById(sender))
            .thenReturn(Optional.empty());
        Mockito.when(repository.findById(recipient))
            .thenReturn(Optional.of(new Account(recipient, BigDecimal.ONE)));
        service.transfer(sender, recipient, BigDecimal.ONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void transfer_NonExistingRecipient_ExceptionThrown() {
        final Long sender = 1L;
        final Long recipient = 2L;
        Mockito.when(repository.findById(sender))
            .thenReturn(Optional.of(new Account(sender, BigDecimal.ONE)));
        Mockito.when(repository.findById(recipient))
            .thenReturn(Optional.empty());
        service.transfer(sender, recipient, BigDecimal.ONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void transfer_EqualSenderAndRecipient_ExceptionThrown() {
        final Long sender = 1L;
        Mockito.doThrow(new IllegalArgumentException())
            .when(validator)
            .validateSenderAndRecipientAreDifferent(sender, sender);
        Mockito.when(repository.findById(sender))
            .thenReturn(Optional.of(new Account(sender, BigDecimal.ONE)));
        service.transfer(sender, sender, BigDecimal.ONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void transfer_NonPositiveAmount_ExceptionThrown() {
        final Long sender = 1L;
        final Long recipient = 2L;
        Mockito.doThrow(new IllegalArgumentException())
            .when(validator)
            .validateAmountIsPositive(BigDecimal.ZERO);
        Mockito.when(repository.findById(sender))
            .thenReturn(Optional.of(new Account(sender, BigDecimal.ONE)));
        Mockito.when(repository.findById(recipient))
            .thenReturn(Optional.of(new Account(recipient, BigDecimal.ONE)));
        service.transfer(sender, recipient, BigDecimal.ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void transfer_ExceededAmount_ExceptionThrown() {
        final Long sender = 1L;
        final Long recipient = 2L;
        Mockito.doThrow(new IllegalArgumentException())
            .when(validator)
            .validateAmountIsNonNegative(BigDecimal.ONE.negate());
        Mockito.when(repository.findById(sender))
            .thenReturn(Optional.of(new Account(sender, BigDecimal.ZERO)));
        Mockito.when(repository.findById(recipient))
            .thenReturn(Optional.of(new Account(recipient, BigDecimal.ZERO)));
        service.transfer(sender, recipient, BigDecimal.ONE);
    }
}
