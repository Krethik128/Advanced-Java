package banksystemTests;
import com.gevernova.bankingsystem.exception.AccountNotFoundException;
import com.gevernova.bankingsystem.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BankTest {

    private Bank bank;
    private Account accountOne; // Renamed to accountOne
    private Account accountTwo; // Renamed to accountTwo

    @BeforeEach
    void setUp() {
        bank = new Bank();
        // Use the new constructor with AccountType
        accountOne = new Account("Alice", AccountType.SAVINGS, 1000.0);
        accountTwo = new Account("Bob", AccountType.CURRENT, 500.0); // Using CURRENT for variety
        bank.addAccount(accountOne);
        bank.addAccount(accountTwo);
    }

    @Test
    @DisplayName("Test isAccountPresent - existing account")
    void testIsAccountPresent_Existing() {
        assertTrue(bank.isAccountPresent(accountOne.getAccountNumber())); // Use dynamic account number
    }

    @Test
    @DisplayName("Test isAccountPresent - non-existing account")
    void testIsAccountPresent_NonExisting() {
        assertFalse(bank.isAccountPresent("ACC003"));
    }

    @Test
    @DisplayName("Test addAccount - successful")
    void testAddAccount_Successful() {
        Account newAccount = new Account("Charlie", AccountType.SAVINGS, 200.0);
        assertDoesNotThrow(() -> bank.addAccount(newAccount));
        assertTrue(bank.isAccountPresent(newAccount.getAccountNumber())); // Use dynamic account number
        assertEquals(newAccount, bank.getAccount(newAccount.getAccountNumber())); // Use dynamic account number
    }


    @Test
    @DisplayName("Test removeAccount - successful")
    void testRemoveAccount_Successful() {
        assertTrue(bank.isAccountPresent(accountOne.getAccountNumber()));
        assertDoesNotThrow(() -> bank.removeAccount(accountOne.getAccountNumber()));
        assertFalse(bank.isAccountPresent(accountOne.getAccountNumber()));
    }

    @Test
    @DisplayName("Test removeAccount - account does not exist")
    void testRemoveAccount_DoesNotExist() {
        assertFalse(bank.isAccountPresent("ACC003"));
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> bank.removeAccount("ACC003"));
        assertEquals("Account does not exist in Database", thrown.getMessage());
    }

    @Test
    @DisplayName("Test getAccount - successful")
    void testGetAccount_Successful() {
        Account retrievedAccount = assertDoesNotThrow(() -> bank.getAccount(accountOne.getAccountNumber()));
        assertNotNull(retrievedAccount);
        assertEquals(accountOne.getAccountNumber(), retrievedAccount.getAccountNumber());
        assertEquals("Alice", retrievedAccount.getAccountHolderName());
        assertEquals(1000.0, retrievedAccount.getBalance());
    }

    @Test
    @DisplayName("Test getAccount - account not found")
    void testGetAccount_NotFound() {
        AccountNotFoundException thrown = assertThrows(AccountNotFoundException.class, () -> bank.getAccount("ACC003"));
        assertEquals("Account does not exist in Database", thrown.getMessage());
    }

    @Test
    @DisplayName("Test getAllAccountsNumbers")
    void testGetAllAccountsNumbers() {
        List<String> accountNumbers = bank.getAllAccountsNumbers();
        assertNotNull(accountNumbers);
        assertEquals(2, accountNumbers.size());
        assertTrue(accountNumbers.contains(accountOne.getAccountNumber()));
        assertTrue(accountNumbers.contains(accountTwo.getAccountNumber()));
        assertFalse(accountNumbers.contains("ACC003"));
    }

    // --- makeTransaction Tests (require mocking System.in/out) ---

    private void provideInput(String data) {
        InputStream inputStream = System.in;
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    private String captureOutput(Runnable action) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        System.setOut(new PrintStream(byteArrayOutputStream));
        try {
            action.run();
        } finally {
            System.setOut(oldOut); // Always restore System.out
        }
        return byteArrayOutputStream.toString().trim();
    }


    @Test
    @DisplayName("Test makeTransaction - Deposit successful")
    void testMakeTransaction_DepositSuccessful() {
        String input = accountOne.getAccountNumber() + "\n1\n200\n";
        provideInput(input);

        String output = captureOutput(() -> bank.makeTransaction());

        assertTrue(output.contains("Deposited 200.0 to account " + accountOne.getAccountNumber() + ". New balance: 1200.0"));
        assertEquals(1200.0, accountOne.getBalance());
    }

    @Test
    @DisplayName("Test makeTransaction - Withdrawal successful")
    void testMakeTransaction_WithdrawalSuccessful() {
        String input = accountOne.getAccountNumber() + "\n2\n300\n";
        provideInput(input);

        String output = captureOutput(() -> bank.makeTransaction());

        assertTrue(output.contains("Withdrawn 300.0 from account " + accountOne.getAccountNumber() + ". New balance: 700.0"));
        assertEquals(700.0, accountOne.getBalance());
    }

    @Test
    @DisplayName("Test makeTransaction - Withdrawal insufficient balance")
    void testMakeTransaction_WithdrawalInsufficientBalance() {
        String input = accountOne.getAccountNumber() + "\n2\n1500\n";
        provideInput(input);

        String output = captureOutput(() -> bank.makeTransaction());

        assertTrue(output.contains("Transaction failed: Insufficient balance for withdrawal."));
        assertEquals(1000.0, accountOne.getBalance()); // Balance should remain unchanged
    }

    @Test
    @DisplayName("Test makeTransaction - Transfer successful")
    void testMakeTransaction_TransferSuccessful() {
        String input = accountOne.getAccountNumber() + "\n3\n" + accountTwo.getAccountNumber() + "\n100\n";
        provideInput(input);

        String output = captureOutput(() -> bank.makeTransaction());

        assertTrue(output.contains("Transferred 100.0 from account " + accountOne.getAccountNumber() + " to account " + accountTwo.getAccountNumber()));
        assertEquals(900.0, accountOne.getBalance());
        assertEquals(600.0, accountTwo.getBalance());
    }

    @Test
    @DisplayName("Test makeTransaction - Transfer insufficient balance")
    void testMakeTransaction_TransferInsufficientBalance() {
        String input = accountOne.getAccountNumber() + "\n3\n" + accountTwo.getAccountNumber() + "\n1500\n";
        provideInput(input);

        String output = captureOutput(() -> bank.makeTransaction());

        assertTrue(output.contains("Transaction failed: Insufficient balance for transfer from account " + accountOne.getAccountNumber()));
        assertEquals(1000.0, accountOne.getBalance());
        assertEquals(500.0, accountTwo.getBalance());
    }

    @Test
    @DisplayName("Test makeTransaction - Account not found for transaction initiation")
    void testMakeTransaction_AccountNotFoundInitiation() {
        String input = "NONEXISTENT_ACC\n";
        provideInput(input);

        String output = captureOutput(() -> bank.makeTransaction());

        assertTrue(output.contains("Account not found."));
        // Ensure no balance changes to existing accounts
        assertEquals(1000.0, accountOne.getBalance());
        assertEquals(500.0, accountTwo.getBalance());
    }

    @Test
    @DisplayName("Test makeTransaction - Recipient account not found for transfer")
    void testMakeTransaction_RecipientAccountNotFound() {
        String input = accountOne.getAccountNumber() + "\n3\nNONEXISTENT_ACC\n100\n";
        provideInput(input);

        String output = captureOutput(() -> bank.makeTransaction());

        assertTrue(output.contains("Recipient account not found."));
        // Ensure no balance changes to existing accounts
        assertEquals(1000.0, accountOne.getBalance());
        assertEquals(500.0, accountTwo.getBalance());
    }

    @Test
    @DisplayName("Test makeTransaction - Invalid transaction type")
    void testMakeTransaction_InvalidTransactionType() {
        String input = accountOne.getAccountNumber() + "\n99\n"; // 99 is an invalid type
        provideInput(input);

        String output = captureOutput(() -> bank.makeTransaction());

        assertTrue(output.contains("Invalid transaction type."));
        assertEquals(1000.0, accountOne.getBalance());
    }

    @Test
    @DisplayName("Test makeTransaction - Deposit negative amount")
    void testMakeTransaction_DepositNegativeAmount() {
        String input = accountOne.getAccountNumber() + "\n1\n-50\n";
        provideInput(input);

        String output = captureOutput(() -> bank.makeTransaction());

        assertTrue(output.contains("Transaction failed: Deposit amount must be positive."));
        assertEquals(1000.0, accountOne.getBalance());
    }

    // New tests for Account constructor
    @Test
    @DisplayName("Test Account constructor - null name")
    void testAccountConstructor_NullName() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new Account(null, AccountType.SAVINGS, 100.0));
        assertEquals("Name cannot be null or empty", thrown.getMessage());
    }

    @Test
    @DisplayName("Test Account constructor - empty name")
    void testAccountConstructor_EmptyName() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new Account("", AccountType.SAVINGS, 100.0));
        assertEquals("Name cannot be null or empty", thrown.getMessage());
    }

    @Test
    @DisplayName("Test Account constructor - null account type")
    void testAccountConstructor_NullAccountType() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new Account("Test User", null, 100.0));
        assertEquals("Account type cannot be null", thrown.getMessage());
    }

    @Test
    @DisplayName("Test Account constructor - negative balance")
    void testAccountConstructor_NegativeBalance() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> new Account("Test User", AccountType.SAVINGS, -100.0));
        assertEquals("Balance cannot be negative", thrown.getMessage());
    }

    @Test
    @DisplayName("Test Account constructor - valid parameters")
    void testAccountConstructor_Valid() {
        Account newAccount = assertDoesNotThrow(() -> new Account("Test User", AccountType.SAVINGS, 500.0));
        assertNotNull(newAccount);
        assertEquals("Test User", newAccount.getAccountHolderName());
        assertEquals(AccountType.SAVINGS, newAccount.getAccountType());
        assertEquals(500.0, newAccount.getBalance());
        assertNotNull(newAccount.getAccountNumber()); // Assuming IDGenerator creates a non-null ID
    }
}