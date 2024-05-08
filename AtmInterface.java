package Task3;

import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

class Account {
    private String name;
    private String userid;
    private String userpin;
    private double balance;
    private List<Transaction> transactionHistory;

    public Account(String name, String userid, String userpin, double initialBalance) {
        this.name = name;
        this.userid = userid;
        this.userpin = userpin;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userid;
    }

    public boolean authenticate(String enteredPin) {
        return userpin.equals(enteredPin);
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add(new Transaction("Deposit", amount, new Date()));
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        transactionHistory.add(new Transaction("Withdrawal", amount, new Date()));
        return true;
    }

    public boolean transfer(double amount, Account targetAccount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        targetAccount.deposit(amount);
        transactionHistory.add(new Transaction("Transfer", amount, new Date()));
        return true;
    }

    public void displayTransactionHistory() {
        System.out.println("Transaction History for " + name + ":");
        for (Transaction transaction : transactionHistory) {
            System.out.println(transaction);
        }
    }
}

class ATM {
    private Bank bank;

    public ATM(Bank bank) {
        this.bank = bank;
    }

    public void performOperations() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        Account account = bank.authenticate(userId, pin);
        if (account == null) {
            System.out.println("Invalid User ID or PIN. Exiting.");
            return;
        }

        do {
            System.out.println("\nATM Menu:");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    account.displayTransactionHistory();
                    break;
                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    if (account.withdraw(withdrawAmount)) {
                        System.out.println("Withdrawal successful.");
                    } else {
                        System.out.println("Insufficient balance.");
                    }
                    break;
                case 3:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    account.deposit(depositAmount);
                    System.out.println("Deposit successful.");
                    break;
                case 4:
                    System.out.print("Enter target user ID: ");
                    String targetUserId = scanner.nextLine();
                    Account targetAccount = bank.getAccount(targetUserId);
                    if (targetAccount == null) {
                        System.out.println("Invalid target user ID.");
                    } else {
                        System.out.print("Enter amount to transfer: ");
                        double transferAmount = scanner.nextDouble();
                        if (account.transfer(transferAmount, targetAccount)) {
                            System.out.println("Transfer successful.");
                        } else {
                            System.out.println("Insufficient balance.");
                        }
                    }
                    break;
                case 5:
                    System.out.println("Exiting. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }while(true);
    }
}

class Bank {
    private HashMap<String, Account> accounts;

    public Bank() {
        accounts = new HashMap<>();
    }

    public void addAccount(Account account) {
        accounts.put(account.getUserId(), account);
    }

    public Account authenticate(String userId, String pin) {
        Account account = accounts.get(userId);
        if (account != null && account.authenticate(pin)) {
            return account;
        }
        return null;
    }

    public Account getAccount(String userId) {
        return accounts.get(userId);
    }
}

class Transaction {
    private String type;
    private double amount;
    private Date date;

    public Transaction(String type, double amount, Date date) {
        this.type = type;
        this.amount = amount;
        this.date = date;
    }

    @Override
    public String toString() {
        return date + " - " + type + ": Rs" + amount;
    }
}

public class AtmInterface {
    public static void main(String[] args) {
        Bank bank = new Bank();
        // Add some accounts to the bank
        bank.addAccount(new Account("rutuja kambale", "RK1357", "1234", 1000));
        bank.addAccount(new Account("sakshi gurav", "SK2468", "5678", 2000));

        ATM atm = new ATM(bank);
        atm.performOperations();
    }
}
