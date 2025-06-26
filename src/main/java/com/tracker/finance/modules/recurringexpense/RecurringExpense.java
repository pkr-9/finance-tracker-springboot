package com.tracker.finance.modules.recurringexpense;

import com.tracker.finance.modules.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
// import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "recurring_expenses")
@Data
@NoArgsConstructor
public class RecurringExpense {

    // @Id
    // @GeneratedValue(generator = "UUID")
    // @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    // private UUID id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Frequency frequency;

    @Min(1)
    @Max(31)
    @Column(nullable = false)
    private int dayOfPeriod; // Day of month (1-31) or day of week (1=Monday to 7=Sunday)

    @Column(nullable = false)
    private String category;

    public enum Frequency {
        WEEKLY, MONTHLY
    }
}