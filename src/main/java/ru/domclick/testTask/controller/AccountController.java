package ru.domclick.testTask.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.domclick.testTask.model.Account;
import ru.domclick.testTask.service.AccountService;

import java.math.BigDecimal;

@AllArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService service;

    @GetMapping("/{id}")
    public Account info(@PathVariable("id") Long id) {
        return service.get(id);
    }

    @PostMapping("/{id}/replenish")
    public void replenish(@PathVariable("id") Long id, @RequestParam("amount") BigDecimal amount) {
        service.replenish(id, amount);
    }

    @PostMapping("/{id}/withdraw")
    public void withdraw(@PathVariable("id") Long id, @RequestParam("amount") BigDecimal amount) {
        service.withdraw(id, amount);
    }

    @PostMapping("/{sender}/transfer")
    public void transfer(
        @PathVariable("sender") Long sender,
        @RequestParam("recipient") Long recipient,
        @RequestParam("amount") BigDecimal amount
    ) {
        service.transfer(sender, recipient, amount);
    }
}
