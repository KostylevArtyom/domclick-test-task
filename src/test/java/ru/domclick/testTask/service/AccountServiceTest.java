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

    @Autowired
    AccountService service;

    @Test
    public void get_ExistingId_Success() {
        Mockito.when(repository.findById(1L))
            .thenReturn(Optional.of(new Account(1L, BigDecimal.ZERO)));
        Account account = service.get(1L);
        Assert.assertEquals(account.getAmount(), BigDecimal.ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void get_NonExistingId_ExceptionThrown() {
        Mockito.when(repository.findById(1L))
            .thenReturn(Optional.empty());
        service.get(1L);
    }

    @Test
    public void replenish_ExistingIdAndPositiveAmount_Success() {
        Mockito.when(repository.findById(1L))
            .thenReturn(Optional.of(new Account(1L, BigDecimal.ZERO)));
        service.replenish(1L, BigDecimal.ONE);
        Account account = service.get(1L);
        Assert.assertEquals(account.getAmount(), BigDecimal.ONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void replenish_NonExistingIdAndPositiveAmount_ExceptionThrown() {
        Mockito.when(repository.findById(1L))
            .thenReturn(Optional.empty());
        service.replenish(1L, BigDecimal.ONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void replenish_ExistingIdAndZeroAmount_ExceptionThrown() {
        Mockito.when(repository.findById(1L))
            .thenReturn(Optional.of(new Account(1L, BigDecimal.ZERO)));
        service.replenish(1L, BigDecimal.ZERO);
    }

    @Test
    public void withdraw_ExistingIdAndPositiveAmount_Success() {
        Mockito.when(repository.findById(1L))
            .thenReturn(Optional.of(new Account(1L, BigDecimal.ONE)));
        service.withdraw(1L, BigDecimal.ONE);
        Account account = service.get(1L);
        Assert.assertEquals(account.getAmount(), BigDecimal.ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void withdraw_NonExistingIdAndPositiveAmount_ExceptionThrown() {
        Mockito.when(repository.findById(1L))
            .thenReturn(Optional.empty());
        service.withdraw(1L, BigDecimal.ONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void withdraw_ExistingIdAndZeroAmount_ExceptionThrown() {
        Mockito.when(repository.findById(1L))
            .thenReturn(Optional.of(new Account(1L, BigDecimal.ONE)));
        service.withdraw(1L, BigDecimal.ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void withdraw_ExistingIdAndExceededAmount_ExceptionThrown() {
        Mockito.when(repository.findById(1L))
            .thenReturn(Optional.of(new Account(1L, BigDecimal.ZERO)));
        service.withdraw(1L, BigDecimal.ONE);
    }

    @Test
    public void transfer_ExistingIdsAndPositiveAmount_Success() {
        Mockito.when(repository.findById(1L))
            .thenReturn(Optional.of(new Account(1L, BigDecimal.ONE)));
        Mockito.when(repository.findById(2L))
            .thenReturn(Optional.of(new Account(2L, BigDecimal.ONE)));
        service.transfer(1L, 2L, BigDecimal.ONE);
        Account first = service.get(1L);
        Account second = service.get(2L);
        Assert.assertEquals(first.getAmount(), BigDecimal.ZERO);
        Assert.assertEquals(second.getAmount(), BigDecimal.valueOf(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void transfer_NonExistingSender_ExceptionThrown() {
        Mockito.when(repository.findById(1L))
            .thenReturn(Optional.empty());
        Mockito.when(repository.findById(2L))
            .thenReturn(Optional.of(new Account(2L, BigDecimal.ONE)));
        service.transfer(1L, 2L, BigDecimal.ONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void transfer_NonExistingRecipient_ExceptionThrown() {
        Mockito.when(repository.findById(1L))
            .thenReturn(Optional.of(new Account(1L, BigDecimal.ONE)));
        Mockito.when(repository.findById(2L))
            .thenReturn(Optional.empty());
        service.transfer(1L, 2L, BigDecimal.ONE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void transfer_NonPositiveAmount_ExceptionThrown() {
        Mockito.when(repository.findById(1L))
            .thenReturn(Optional.of(new Account(1L, BigDecimal.ONE)));
        Mockito.when(repository.findById(2L))
            .thenReturn(Optional.of(new Account(2L, BigDecimal.ONE)));
        service.transfer(1L, 2L, BigDecimal.ZERO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void transfer_ExceededAmount_ExceptionThrown() {
        Mockito.when(repository.findById(1L))
            .thenReturn(Optional.of(new Account(1L, BigDecimal.ZERO)));
        Mockito.when(repository.findById(2L))
            .thenReturn(Optional.of(new Account(2L, BigDecimal.ZERO)));
        service.transfer(1L, 2L, BigDecimal.ONE);
    }
}
