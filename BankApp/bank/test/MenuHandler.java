package com.aurionpro.bank.test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import com.aurionpro.bank.exception.InvalidTransactionException;
import com.aurionpro.bank.model.Account;
import com.aurionpro.bank.model.Transaction;
import com.aurionpro.bank.service.BankFacade;
import com.aurionpro.bank.util.InputHandler;
import com.aurionpro.bank.util.ReceiptPrinter;
import com.aurionpro.bank.util.TransactionPrinter;

public class MenuHandler {
    private final BankFacade bank;

    public MenuHandler(BankFacade bank) {
        this.bank = bank;
    }

    public void start() throws SQLException {
        int choice;
        do {
            printMainMenu();
            choice = InputHandler.getInt("Choose an option: ");
            switch (choice) {
                case 1:
                    handleAdminLogin();
                    break;
                case 2:
                    handleUserLogin();
                    break;
                case 3:
                    System.out.println("✅ Thank you for using Console Bank!");
                    break;
                default:
                    System.out.println("❌ Invalid choice. Try again.");
                    break;
            }
        } while (choice != 3);
    }

    private void printMainMenu() {
        System.out.println("\n========= Console Bank =========");
        System.out.println("1. Admin Login");
        System.out.println("2. User Login");
        System.out.println("3. Exit");
        System.out.println("================================");
    }

    // 🔹 Admin Login
    private void handleAdminLogin() throws SQLException {
        String adminUser = InputHandler.getString("Enter Admin Username: ");
        String adminPass = InputHandler.getString("Enter Admin Password: ");

        if (!("admin".equals(adminUser) && "admin123".equals(adminPass))) {
            System.out.println("❌ Invalid Admin Credentials!");
            return;
        }

        int choice;
        do {
            printAdminMenu();
            choice = InputHandler.getInt("Choose an option: ");
            try {
                switch (choice) {
                    case 1:
                        handleCreateAccount();
                        break;
                    case 2:
                        handleViewAccounts();
                        break;
                    case 3:
                        handleSetAccountStatus();
                        break;
                    case 4:
                        handleViewAllTransactions();
                        break;
                    case 5:
                        System.out.println("🔙 Logging out from Admin...");
                        break;
                    default:
                        System.out.println("❌ Invalid choice.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        } while (choice != 5);
    }

    private void printAdminMenu() {
        System.out.println("\n--- Admin Menu ---");
        System.out.println("1. Create Account");
        System.out.println("2. View Accounts");
        System.out.println("3. Set Account Status (Active/Inactive)");
        System.out.println("4. View All Transactions");
        System.out.println("5. Logout");
    }

    // 🔹 User Login
    private void handleUserLogin() throws SQLException {
        int accId = InputHandler.getValidAccountId("Enter Account ID: ", bank);
        String pin = InputHandler.getString("Enter PIN: ");

        Account acc = bank.loginUser(accId, pin);
        if (acc == null) {
            System.out.println("❌ Invalid PIN or inactive account!");
            return;
        }

        int choice;
        do {
            printUserMenu();
            choice = InputHandler.getInt("Choose an option: ");
            try {
                switch (choice) {
                    case 1:
                        handleCheckBalance(acc);
                        break;
                    case 2:
                        handleDeposit(acc);
                        break;
                    case 3:
                        handleWithdraw(acc);
                        break;
                    case 4:
                        handleTransfer(acc);
                        break;
                    case 5:
                        handleUserTransactions(acc);
                        break;
                    case 6:
                        handleUpdatePin(acc);
                        break;
                    case 7:
                        System.out.println("🔙 Logging out...");
                        break;
                    default:
                        System.out.println("❌ Invalid choice.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        } while (choice != 7);
    }

    private void printUserMenu() {
        System.out.println("\n--- User Menu ---");
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer Funds");
        System.out.println("5. Transaction History");
        System.out.println("6. Update PIN");
        System.out.println("7. Logout");
    }

    // ================= Admin Actions =================
    private void handleCreateAccount() throws SQLException, InvalidTransactionException {
        String name = InputHandler.getString("Enter Customer Name: ");
        String pin = InputHandler.getPin("Set 4-digit PIN: "); // ✅ PIN validation
        BigDecimal deposit = new BigDecimal(InputHandler.getPositiveInt("Enter Initial Deposit: "));

        int id = bank.createNewAccount(name, pin, deposit);
        if (id > 0) {
            System.out.println("✅ Account created successfully! Account ID: " + id);
        } else {
            System.out.println("❌ Account creation failed.");
        }
    }

    private void handleViewAccounts() throws SQLException {
        System.out.println("\n--- View Accounts ---");
        System.out.println("1. All Accounts");
        System.out.println("2. Active Accounts");
        System.out.println("3. Inactive Accounts");
        int subChoice = InputHandler.getInt("Choose an option: ");

        String type = "ALL";
        if (subChoice == 2) type = "ACTIVE";
        else if (subChoice == 3) type = "INACTIVE";

        List<Account> accounts = bank.getAccounts(type);
        if (accounts.isEmpty()) {
            System.out.println("⚠️ No accounts found.");
            return;
        }
        for (Account acc : accounts) {
            System.out.printf("ID: %-3d | Name: %-15s | Balance: ₹%-10.2f | Status: %s\n",
                    acc.getAccountNumber(),
                    acc.getName(),
                    acc.getBalance(),
                    acc.isActive() ? "Active" : "Inactive");
        }
    }

    private void handleSetAccountStatus() throws SQLException {
        int accId = InputHandler.getValidAccountId("Enter Account ID: ", bank);
        boolean status = InputHandler.getInt("Enter 1 to Activate, 0 to Deactivate: ") == 1;
        if (bank.setAccountStatus(accId, status)) {
            System.out.println("✅ Account status updated!");
        } else {
            System.out.println("❌ Failed to update status.");
        }
    }

    private void handleViewAllTransactions() throws SQLException {
        List<Transaction> txns = bank.getAllTransactions();
        TransactionPrinter.printTransactions(txns);
    }

    // ================= User Actions =================
    private void handleCheckBalance(Account acc) {
        System.out.println("💰 Current Balance: ₹" + acc.getBalance());
    }

    private void handleDeposit(Account acc) throws SQLException, InvalidTransactionException {
        BigDecimal amt = new BigDecimal(InputHandler.getPositiveInt("Enter deposit amount (₹): "));
        String pin = InputHandler.getString("Enter PIN: ");
        if (bank.deposit(acc.getAccountNumber(), amt, pin)) {
            acc.setBalance(acc.getBalance().add(amt));
            System.out.println("✅ Deposit successful!");
            ReceiptPrinter.print(acc, "DEPOSIT", amt, acc.getBalance());
        }
    }

    private void handleWithdraw(Account acc) throws SQLException, InvalidTransactionException {
        BigDecimal amt = new BigDecimal(InputHandler.getPositiveInt("Enter withdrawal amount (₹): "));
        String pin = InputHandler.getString("Enter PIN: ");
        if (bank.withdraw(acc.getAccountNumber(), amt, pin)) {
            acc.setBalance(acc.getBalance().subtract(amt));
            System.out.println("✅ Withdrawal successful!");
            ReceiptPrinter.print(acc, "WITHDRAW", amt, acc.getBalance());
        }
    }

    private void handleTransfer(Account acc) throws SQLException, InvalidTransactionException {
        int toId = InputHandler.getValidAccountId("Enter Receiver Account ID: ", bank);
        BigDecimal amt = new BigDecimal(InputHandler.getPositiveInt("Enter transfer amount (₹): "));
        String pin = InputHandler.getString("Enter your PIN: ");
        if (bank.transfer(acc.getAccountNumber(), toId, amt, pin)) {
            acc.setBalance(acc.getBalance().subtract(amt));
            Account receiver = bank.getAccount(toId);
            System.out.println("✅ Transfer successful!");
            ReceiptPrinter.printTransferReceipt(acc, receiver, amt, acc.getBalance(), receiver.getBalance());
        }
    }

    private void handleUserTransactions(Account acc) throws SQLException {
        List<Transaction> txns = bank.getTransactionsForAccount(acc.getAccountNumber());
        TransactionPrinter.printTransactions(txns);
    }

    private void handleUpdatePin(Account acc) throws SQLException, InvalidTransactionException {
        String oldPin = InputHandler.getString("Enter Current PIN: ");
        String newPin = InputHandler.getPin("Enter New 4-digit PIN: "); // ✅ PIN validation
        if (bank.updatePin(acc.getAccountNumber(), oldPin, newPin)) {
            System.out.println("✅ PIN updated successfully!");
        } else {
            System.out.println("❌ Failed to update PIN.");
        }
    }
}
