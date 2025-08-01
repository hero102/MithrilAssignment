package com.aurionpro.bank.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aurionpro.bank.model.Account;

public class ReceiptPrinter {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    // 🔹 Print receipt for Deposit/Withdraw
    public static void print(Account acc, String type, BigDecimal amount, BigDecimal balance) {
        System.out.println("\n=========== Transaction Receipt ===========");
        System.out.println("Transaction Type : " + type);
        System.out.println("Account Holder   : " + acc.getName());
        System.out.println("Account ID       : " + acc.getAccountNumber());
        System.out.println("Date & Time      : " + sdf.format(new Date()));
        System.out.printf("Amount           : ₹%.2f\n", amount);
        System.out.printf("Updated Balance  : ₹%.2f\n", balance);
        System.out.println("===========================================");
    }

    // 🔹 Print receipt for Transfer
    public static void printTransferReceipt(Account sender, Account receiver, BigDecimal amount,
                                            BigDecimal senderBalance, BigDecimal receiverBalance) {
        System.out.println("\n=========== Transfer Receipt ===========");
        System.out.println("Transaction Type : TRANSFER");
        System.out.println("Date & Time      : " + sdf.format(new Date()));
        System.out.println("-----------------------------------------");
        System.out.println("Sender:");
        System.out.println("   Account Holder : " + sender.getName());
        System.out.println("   Account ID     : " + sender.getAccountNumber());
        System.out.printf("   Amount Sent    : ₹%.2f\n", amount);
        System.out.printf("   New Balance    : ₹%.2f\n", senderBalance);
        System.out.println("-----------------------------------------");
        System.out.println("Receiver:");
        System.out.println("   Account Holder : " + receiver.getName());
        System.out.println("   Account ID     : " + receiver.getAccountNumber());
        System.out.printf("   Amount Received: ₹%.2f\n", amount);
        System.out.printf("   New Balance    : ₹%.2f\n", receiverBalance);
        System.out.println("=========================================");
    }
}
