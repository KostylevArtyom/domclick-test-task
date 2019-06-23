package ru.domclick.testTask.repository;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;
import ru.domclick.testTask.model.Account;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {
    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Account> findById(Long id);

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Iterable<Account> findAll();

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Iterable<Account> findAllById(Iterable<Long> ids);
}
