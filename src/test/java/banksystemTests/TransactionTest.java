package banksystemTests;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import com.gevernova.bankingsystem.model.*;
import com.gevernova.bankingsystem.service.*;
import com.gevernova.bankingsystem.exceptionhandling.*;

public class TransactionTest {
    private Account savingsAccount;
    private Account currentAccount;

    @BeforeEach
    public void setUp() {
        savingsAccount = new SavingsAccount("John Doe", 1000.0);
        currentAccount = new CurrentAccount("Jane Smith", 15000.0);
    }

    @Test
    @DisplayName("OCP: Deposit transaction implementation")
    public void testDepositTransaction() throws InvalidBalanceException {
        ITransaction deposit = new DepositTransaction(savingsAccount, 500.0);

        assertEquals("DEPOSIT", deposit.getTransactionType());
        assertEquals(500.0, deposit.getAmount());

        deposit.execute();
        assertEquals(1500.0, savingsAccount.getBalance());
    }

    @Test
    @DisplayName("OCP: Withdrawal transaction implementation")
    public void testWithdrawalTransaction() throws InvalidBalanceException {
        ITransaction withdrawal = new WithdrawalTransaction(currentAccount, 1000.0);

        assertEquals("WITHDRAWAL", withdrawal.getTransactionType());
        assertEquals(1000.0, withdrawal.getAmount());

        withdrawal.execute();
        assertEquals(14000.0, currentAccount.getBalance());
    }

    @Test
    @DisplayName("OCP: Transfer transaction implementation")
    public void testTransferTransaction() throws InvalidBalanceException {
        ITransaction transfer = new TransferTransaction(currentAccount, savingsAccount, 2000.0);

        assertEquals("TRANSFER", transfer.getTransactionType());
        assertEquals(2000.0, transfer.getAmount());

        transfer.execute();
        assertEquals(13000.0, currentAccount.getBalance());
        assertEquals(3000.0, savingsAccount.getBalance());
    }
}
