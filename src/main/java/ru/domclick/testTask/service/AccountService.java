package ru.domclick.testTask.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.domclick.testTask.model.Account;
import ru.domclick.testTask.repository.AccountRepository;
import ru.domclick.testTask.utils.ExceptionMessages;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@AllArgsConstructor
@Service
public class AccountService {
    private final AccountRepository repository;
    private final ValidationService validator;

    public Account get(Long id) {
        validator.validateAccountIdIsPositive(id);
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(ExceptionMessages.NON_EXISTENT_ID));
    }

    private void modifyAmount(Long id, BigDecimal amount) {
        final Account account = this.get(id);
        account.modifyAmount(amount);
        validator.validateAmountIsNonNegative(account.getAmount());
        repository.save(account);
    }

    @Transactional
    public void replenish(Long id, BigDecimal amount) {
        validator.validateAmountIsPositive(amount);
        modifyAmount(id, amount);
    }

    @Transactional
    public void withdraw(Long id, BigDecimal amount) {
        validator.validateAmountIsPositive(amount);
        modifyAmount(id, amount.negate());
    }

    @Transactional
    public void transfer(Long sender, Long recipient, BigDecimal amount) {
        validator.validateSenderAndRecipientAreDifferent(sender, recipient);
        withdraw(sender, amount);
        replenish(recipient, amount);
    }
}
