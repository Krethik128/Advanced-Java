package banksystemTests;

import com.gevernova.bankingsystem.exception.InvalidBalanceException;
import com.gevernova.bankingsystem.model.Account;
import com.gevernova.bankingsystem.model.AccountType; // Import AccountType
import com.gevernova.bankingsystem.service.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    private Transaction transaction;
    private Account accountOne; // Renamed to accountOne
    private Account accountTwo; // Renamed to accountTwo

    @BeforeEach
    void setUp() {
        transaction = new Transaction();
        // Use the new constructor with AccountType
        accountOne = new Account("John Doe", AccountType.SAVINGS, 1000.0);
        accountTwo = new Account("Jane Smith", AccountType.CURRENT, 500.0); // Using CURRENT for variety
    }

    @Test
    @DisplayName("Test successful deposit")
    void testDeposit_Successful() {
        assertDoesNotThrow(() -> transaction.deposit(accountOne, 200.0));
        assertEquals(1200.0, accountOne.getBalance(), "Balance should increase after deposit");
    }

    @Test
    @DisplayName("Test deposit with zero amount")
    void testDeposit_ZeroAmount() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> transaction.deposit(accountOne, 0.0));
        assertEquals("Deposit amount must be positive.", thrown.getMessage());
        assertEquals(1000.0, accountOne.getBalance(), "Balance should remain unchanged for zero deposit");
    }

    @Test
    @DisplayName("Test deposit with negative amount")
    void testDeposit_NegativeAmount() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> transaction.deposit(accountOne, -100.0));
        assertEquals("Deposit amount must be positive.", thrown.getMessage());
        assertEquals(1000.0, accountOne.getBalance(), "Balance should remain unchanged for negative deposit");
    }

    @Test
    @DisplayName("Test deposit with null account")
    void testDeposit_NullAccount() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> transaction.deposit(null, 100.0));
        assertEquals("Account cannot be null for deposit.", thrown.getMessage());
    }

    @Test
    @DisplayName("Test successful withdrawal")
    void testWithdraw_Successful() {
        assertDoesNotThrow(() -> transaction.withdraw(accountOne, 300.0));
        assertEquals(700.0, accountOne.getBalance(), "Balance should decrease after withdrawal");
    }

    @Test
    @DisplayName("Test withdrawal with insufficient balance")
    void testWithdraw_InsufficientBalance() {
        InvalidBalanceException thrown = assertThrows(InvalidBalanceException.class, () -> transaction.withdraw(accountOne, 1500.0));
        assertEquals("Insufficient balance for withdrawal.", thrown.getMessage());
        assertEquals(1000.0, accountOne.getBalance(), "Balance should remain unchanged due to insufficient funds");
    }

    @Test
    @DisplayName("Test withdrawal with zero amount")
    void testWithdraw_ZeroAmount() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> transaction.withdraw(accountOne, 0.0));
        assertEquals("Withdrawal amount must be positive.", thrown.getMessage());
        assertEquals(1000.0, accountOne.getBalance(), "Balance should remain unchanged for zero withdrawal");
    }

    @Test
    @DisplayName("Test withdrawal with negative amount")
    void testWithdraw_NegativeAmount() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> transaction.withdraw(accountOne, -50.0));
        assertEquals("Withdrawal amount must be positive.", thrown.getMessage());
        assertEquals(1000.0, accountOne.getBalance(), "Balance should remain unchanged for negative withdrawal");
    }

    @Test
    @DisplayName("Test withdrawal with null account")
    void testWithdraw_NullAccount() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> transaction.withdraw(null, 100.0));
        assertEquals("Account cannot be null for withdrawal.", thrown.getMessage());
    }

    @Test
    @DisplayName("Test successful transfer")
    void testTransfer_Successful() {
        assertDoesNotThrow(() -> transaction.transfer(accountOne, accountTwo, 200.0));
        assertEquals(800.0, accountOne.getBalance(), "Sender balance should decrease after transfer");
        assertEquals(700.0, accountTwo.getBalance(), "Recipient balance should increase after transfer");
    }

    @Test
    @DisplayName("Test transfer with insufficient balance in sender account")
    void testTransfer_InsufficientBalance() {
        InvalidBalanceException thrown = assertThrows(InvalidBalanceException.class, () -> transaction.transfer(accountOne, accountTwo, 1500.0));
        assertEquals("Insufficient balance for transfer from account " + accountOne.getAccountNumber(), thrown.getMessage()); // Dynamic account number
        assertEquals(1000.0, accountOne.getBalance(), "Sender balance should remain unchanged due to insufficient funds");
        assertEquals(500.0, accountTwo.getBalance(), "Recipient balance should remain unchanged");
    }

    @Test
    @DisplayName("Test transfer with zero amount")
    void testTransfer_ZeroAmount() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> transaction.transfer(accountOne, accountTwo, 0.0));
        assertEquals("Transfer amount must be positive.", thrown.getMessage());
        assertEquals(1000.0, accountOne.getBalance());
        assertEquals(500.0, accountTwo.getBalance());
    }

    @Test
    @DisplayName("Test transfer with negative amount")
    void testTransfer_NegativeAmount() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> transaction.transfer(accountOne, accountTwo, -100.0));
        assertEquals("Transfer amount must be positive.", thrown.getMessage());
        assertEquals(1000.0, accountOne.getBalance());
        assertEquals(500.0, accountTwo.getBalance());
    }

    @Test
    @DisplayName("Test transfer with null sender account")
    void testTransfer_NullFromAccount() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> transaction.transfer(null, accountTwo, 100.0));
        assertEquals("Both sender and recipient accounts must not be null for transfer.", thrown.getMessage());
        assertEquals(1000.0, accountOne.getBalance());
        assertEquals(500.0, accountTwo.getBalance());
    }

    @Test
    @DisplayName("Test transfer with null recipient account")
    void testTransfer_NullToAccount() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> transaction.transfer(accountOne, null, 100.0));
        assertEquals("Both sender and recipient accounts must not be null for transfer.", thrown.getMessage());
        assertEquals(1000.0, accountOne.getBalance());
        assertEquals(500.0, accountTwo.getBalance());
    }
}