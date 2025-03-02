package com.example;

import com.example.enums.OperationType;
import com.example.enums.TransactionStatus;
import com.example.model.Transaction;
import com.example.model.Wallet;
import com.example.repository.TransactionRepository;
import com.example.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Testcontainers
public abstract class AbstractTest {

    protected static PostgreSQLContainer postgreSQLContainer;
    static {
        DockerImageName postgres = DockerImageName.parse("postgres:17.2");
        postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer(postgres)
                .withReuse(true);
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    public static void registerProperties(DynamicPropertyRegistry registry) {
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.url", () -> jdbcUrl);
    }

    @Autowired
    protected WalletRepository walletRepository;

    @Autowired
    protected TransactionRepository transactionRepository;

    @Autowired
    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper = new ObjectMapper();

    protected static final UUID WALLET_UUID_EXISTS = UUID.randomUUID();

    protected static final UUID WALLET_UUID_NOT_FOUND = UUID.fromString("00000000-0000-0000-0000-000000000404");

    protected static final BigDecimal DEPOSIT_AMOUNT = BigDecimal.valueOf(1_000);

    protected static final BigDecimal WITHDRAW_AMOUNT = BigDecimal.valueOf(100);

    protected static final BigDecimal WITHDRAW_NOT_ENOUGH_AMOUNT = BigDecimal.valueOf(10_000);

    @BeforeEach
    public void setup() {
        Wallet wallet = new Wallet(WALLET_UUID_EXISTS, DEPOSIT_AMOUNT);
        walletRepository.save(wallet);

        Transaction transaction = Transaction.builder()
                .wallet(wallet)
                .amount(DEPOSIT_AMOUNT)
                .operationType(OperationType.DEPOSIT)
                .status(TransactionStatus.APPROVED)
                .build();
        transactionRepository.save(transaction);
    }

    @AfterEach
    public void afterEach() {
        transactionRepository.deleteAll();
        walletRepository.deleteAll();
    }

}