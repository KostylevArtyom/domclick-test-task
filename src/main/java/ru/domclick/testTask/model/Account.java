package ru.domclick.testTask.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.domclick.testTask.utils.ExceptionMessages;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    @Min(value = 0, message = ExceptionMessages.INSUFFICIENT_AMOUNT)
    private BigDecimal amount;

    public void modifyAmount(BigDecimal amount) {
        this.setAmount(this.amount.add(amount));
    }
}
