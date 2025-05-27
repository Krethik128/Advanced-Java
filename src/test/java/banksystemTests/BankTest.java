package banksystemTests;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import com.gevernova.bankingsystem.model.*;
import com.gevernova.bankingsystem.exceptionhandling.*;
import com.gevernova.bankingsystem.service.*;

public class BankTest {
    private Bank bank;
    private Account savingsAccount;
    private Account currentAccount;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out; // Store original System.out

    @BeforeEach
    public void setUp() {
        bank = new Bank();
        // Ensure initial balances meet minimums if applicable
        savingsAccount = new SavingsAccount("John Doe", 1000.0);
        currentAccount = new CurrentAccount("Jane Smith", 15000.0);

        // Capture system output for testing print methods
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        // Restore original System.out after each test
        System.setOut(originalOut);
        // Clear the captured output stream
        outputStreamCaptor.reset();
    }

    // **LSP Testing - Account Hierarchy**
    @Test
    @DisplayName("LSP: Different account types can be treated as Account")
    public void testLSP_AccountSubstitutability() {
        assertDoesNotThrow(() -> {
            bank.addAccount(savingsAccount);
            bank.addAccount(currentAccount);
        });

        assertTrue(bank.isAccountPresent(savingsAccount.getAccountNumber()));
        assertTrue(bank.isAccountPresent(currentAccount.getAccountNumber()));
    }

    @Test
    @DisplayName("LSP: Polymorphic withdrawal behavior")
    public void testLSP_PolymorphicWithdrawal() throws Exception {
        bank.addAccount(savingsAccount);
        bank.addAccount(currentAccount);

        // Both account types should work with same withdrawal method
        assertDoesNotThrow(() -> {
            bank.performWithdrawal(savingsAccount.getAccountNumber(), 200.0); // Savings: 1000 - 200 = 800
            bank.performWithdrawal(currentAccount.getAccountNumber(), 1000.0); // Current: 15000 - 1000 = 14000
        });

        // Assert that the balances reflect the polymorphic behavior
        assertEquals(800.0, savingsAccount.getBalance());
        assertEquals(14000.0, currentAccount.getBalance());

        // Test boundary conditions for SavingsAccount (LSP detail)
        assertThrows(InvalidBalanceException.class, () -> {
            // Trying to withdraw past minimum balance (800 - 400 = 400, below 500 min)
            bank.performWithdrawal(savingsAccount.getAccountNumber(), 400.0);
        });
        // Test a valid withdrawal for CurrentAccount (LSP detail)
        assertDoesNotThrow(() -> {
            bank.performWithdrawal(currentAccount.getAccountNumber(), 14000.0); // Current: 14000 - 14000 = 0
        });
        assertEquals(0.0, currentAccount.getBalance());
    }

    // **OCP Testing - Transaction Extension**
    @Test
    @DisplayName("OCP: Execute different transaction types without modification")
    public void testOCP_TransactionExtensibility() throws Exception {
        bank.addAccount(savingsAccount); // initial 1000
        bank.addAccount(currentAccount); // initial 15000

        // Test different transaction types using same executeTransaction method
        ITransaction deposit = new DepositTransaction(savingsAccount, 500.0); // Savings: 1000 + 500 = 1500
        ITransaction withdrawal = new WithdrawalTransaction(currentAccount, 1000.0); // Current: 15000 - 1000 = 14000
        ITransaction transfer = new TransferTransaction(currentAccount, savingsAccount, 2000.0); // Current: 14000 - 2000 = 12000, Savings: 1500 + 2000 = 3500

        assertDoesNotThrow(() -> {
            bank.executeTransaction(deposit);
            bank.executeTransaction(withdrawal);
            bank.executeTransaction(transfer);
        });

        assertEquals(3500.0, savingsAccount.getBalance());
        assertEquals(12000.0, currentAccount.getBalance());

        // Verify transaction history includes all types
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Transaction completed: DEPOSIT"));
        assertTrue(output.contains("Transaction completed: WITHDRAWAL"));
        assertTrue(output.contains("Transaction completed: TRANSFER"));
    }

    // **Account Management Tests**
    @Test
    @DisplayName("Add account successfully")
    public void testAddAccount_Success() {
        assertDoesNotThrow(() -> bank.addAccount(savingsAccount));
        assertTrue(bank.isAccountPresent(savingsAccount.getAccountNumber()));
        assertTrue(outputStreamCaptor.toString().contains("Account added:"));
    }

    @Test
    @DisplayName("Add duplicate account throws exception")
    public void testAddAccount_Duplicate() {
        bank.addAccount(savingsAccount);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bank.addAccount(savingsAccount);
        });
        assertEquals("Account number already exists", exception.getMessage());
    }

    @Test
    @DisplayName("Remove account successfully")
    public void testRemoveAccount_Success() throws AccountNotFoundException {
        bank.addAccount(savingsAccount);
        assertTrue(bank.isAccountPresent(savingsAccount.getAccountNumber()));

        assertDoesNotThrow(() -> bank.removeAccount(savingsAccount.getAccountNumber()));
        assertFalse(bank.isAccountPresent(savingsAccount.getAccountNumber()));
        assertTrue(outputStreamCaptor.toString().contains("Account " + savingsAccount.getAccountNumber() + " removed successfully."));
    }

    @Test
    @DisplayName("Remove non-existent account throws exception")
    public void testRemoveAccount_NonExistent() {
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            bank.removeAccount("NON_EXISTENT_ACCOUNT");
        });
        assertEquals("Account does not exist in Database", exception.getMessage());
    }

    @Test
    @DisplayName("Get account successfully")
    public void testGetAccount_Success() throws AccountNotFoundException {
        bank.addAccount(savingsAccount);

        Account retrievedAccount = bank.getAccount(savingsAccount.getAccountNumber());
        assertEquals(savingsAccount.getAccountNumber(), retrievedAccount.getAccountNumber());
        assertEquals(savingsAccount.getAccountHolderName(), retrievedAccount.getAccountHolderName());
    }

    @Test
    @DisplayName("Get non-existent account throws exception")
    public void testGetAccount_NotFound() {
        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            bank.getAccount("NON_EXISTENT_ACCOUNT");
        });
        assertEquals("Account " + "NON_EXISTENT_ACCOUNT" + " does not exist in Database", exception.getMessage());
    }

    // **Transaction Tests**
    @Test
    @DisplayName("Perform deposit successfully")
    public void testPerformDeposit_Success() throws Exception {
        bank.addAccount(savingsAccount);
        double initialBalance = savingsAccount.getBalance(); // 1000

        bank.performDeposit(savingsAccount.getAccountNumber(), 500.0);

        assertEquals(initialBalance + 500.0, savingsAccount.getBalance()); // 1500
        assertTrue(outputStreamCaptor.toString().contains("Transaction completed: DEPOSIT"));
        assertTrue(outputStreamCaptor.toString().contains("Deposited 500.0 to Savings Account"));
    }

    @Test
    @DisplayName("Perform deposit with invalid amount")
    public void testPerformDeposit_InvalidAmount() {
        bank.addAccount(savingsAccount);

        assertThrows(IllegalArgumentException.class, () -> {
            bank.performDeposit(savingsAccount.getAccountNumber(), -100.0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            bank.performDeposit(savingsAccount.getAccountNumber(), 0.0);
        });
    }

    @Test
    @DisplayName("Perform withdrawal successfully")
    public void testPerformWithdrawal_Success() throws Exception {
        bank.addAccount(currentAccount);
        double initialBalance = currentAccount.getBalance(); // 15000

        bank.performWithdrawal(currentAccount.getAccountNumber(), 1000.0);

        assertEquals(initialBalance - 1000.0, currentAccount.getBalance()); // 14000
        assertTrue(outputStreamCaptor.toString().contains("Transaction completed: WITHDRAWAL"));
        assertTrue(outputStreamCaptor.toString().contains("Withdrawn 1000.0 from Current Account"));
    }

    @Test
    @DisplayName("Perform withdrawal with insufficient funds (Savings)")
    public void testPerformWithdrawal_InsufficientFunds_Savings() {
        bank.addAccount(savingsAccount); // 1000, min 500

        InvalidBalanceException exception = assertThrows(InvalidBalanceException.class, () -> {
            bank.performWithdrawal(savingsAccount.getAccountNumber(), 600.0); // 1000 - 600 = 400 < 500 min
        });
        assertTrue(exception.getMessage().contains("Insufficient balance for withdrawal. Minimum balance of 500.0 required."));
    }

    @Test
    @DisplayName("Perform withdrawal with insufficient funds (Current)")
    public void testPerformWithdrawal_InsufficientFunds_Current() {
        bank.addAccount(currentAccount); // 15000

        InvalidBalanceException exception = assertThrows(InvalidBalanceException.class, () -> {
            bank.performWithdrawal(currentAccount.getAccountNumber(), 15001.0); // 15000 - 15001 = -1 < 0 min
        });
        assertTrue(exception.getMessage().contains("Insufficient balance for withdrawal from Current Account."));
    }

    @Test
    @DisplayName("Perform transfer with insufficient funds from sender")
    public void testPerformTransfer_InsufficientFunds() {
        bank.addAccount(savingsAccount); // 1000, min 500
        bank.addAccount(currentAccount); // 15000

        assertThrows(InvalidBalanceException.class, () -> {
            bank.performTransfer(savingsAccount.getAccountNumber(), // trying to transfer too much from savings
                    currentAccount.getAccountNumber(), 600.0); // 1000 - 600 = 400 < 500 min
        });
    }

    @Test
    @DisplayName("Perform transfer with non-existent accounts")
    public void testPerformTransfer_AccountNotFound() {
        bank.addAccount(savingsAccount);

        assertThrows(AccountNotFoundException.class, () -> {
            bank.performTransfer(savingsAccount.getAccountNumber(),
                    "NON_EXISTENT", 100.0);
        });
        assertThrows(AccountNotFoundException.class, () -> {
            bank.performTransfer("ANOTHER_NON_EXISTENT",
                    savingsAccount.getAccountNumber(), 100.0);
        });
    }

    // **Utility Methods Tests**
    @Test
    @DisplayName("Get all account numbers")
    public void testGetAllAccountNumbers() {
        bank.addAccount(savingsAccount);
        bank.addAccount(currentAccount);

        var accountNumbers = bank.getAllAccountNumbers();

        assertEquals(2, accountNumbers.size());
        assertTrue(accountNumbers.contains(savingsAccount.getAccountNumber()));
        assertTrue(accountNumbers.contains(currentAccount.getAccountNumber()));
    }

    @Test
    @DisplayName("Get all account numbers when empty")
    public void testGetAllAccountNumbers_Empty() {
        var accountNumbers = bank.getAllAccountNumbers();
        assertTrue(accountNumbers.isEmpty());
    }

    @Test
    @DisplayName("Print transaction history")
    public void testPrintTransactionHistory() throws Exception {
        bank.addAccount(savingsAccount);
        bank.performDeposit(savingsAccount.getAccountNumber(), 500.0);

        // Reset captor to only get output from printTransactionHistory
        outputStreamCaptor.reset();
        bank.printTransactionHistory();

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("=== Transaction History ==="));
        assertTrue(output.contains("Deposit of 500.0 to account " + savingsAccount.getAccountNumber()));
    }

    @Test
    @DisplayName("Print all accounts")
    public void testPrintAllAccounts() {
        bank.addAccount(savingsAccount);
        bank.addAccount(currentAccount);

        // Reset captor to only get output from printAllAccounts
        outputStreamCaptor.reset();
        bank.printAllAccounts();

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("=== All Accounts ==="));
        assertTrue(output.contains("Name=John Doe, Type=SAVINGS, Balance=1000.0"));
        assertTrue(output.contains("Name=Jane Smith, Type=CURRENT, Balance=15000.0"));
    }

    // **Edge Cases and Boundary Tests**
    @Test
    @DisplayName("Multiple transactions on same account")
    public void testMultipleTransactions() throws Exception {
        bank.addAccount(savingsAccount); // Initial 1000

        bank.performDeposit(savingsAccount.getAccountNumber(), 500.0);  // 1000 + 500 = 1500
        bank.performWithdrawal(savingsAccount.getAccountNumber(), 200.0); // 1500 - 200 = 1300
        bank.performDeposit(savingsAccount.getAccountNumber(), 300.0);  // 1300 + 300 = 1600

        assertEquals(1600.0, savingsAccount.getBalance());
    }

    @Test
    @DisplayName("Account presence check")
    public void testIsAccountPresent() throws AccountNotFoundException {
        assertFalse(bank.isAccountPresent("NON_EXISTENT"));

        bank.addAccount(savingsAccount);
        assertTrue(bank.isAccountPresent(savingsAccount.getAccountNumber()));

        bank.removeAccount(savingsAccount.getAccountNumber());
        assertFalse(bank.isAccountPresent(savingsAccount.getAccountNumber()));
    }

    @Test
    @DisplayName("Initial balance below minimum for SavingsAccount throws exception")
    public void testSavingsAccount_InitialBalanceBelowMinimum() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new SavingsAccount("Invalid John", 400.0); // 400 < 500
        });
        assertTrue(exception.getMessage().contains("Initial balance for Savings Account must be at least 500.0"));
    }

    @Test
    @DisplayName("Initial balance for CurrentAccount does not enforce specific minimum (beyond non-negative)")
    public void testCurrentAccount_InitialBalanceFlexibility() {
        // Current account can start with 0 or any positive balance
        assertDoesNotThrow(() -> new CurrentAccount("Valid Jane", 0.0));
        assertDoesNotThrow(() -> new CurrentAccount("Another Jane", 10.0));
    }
}