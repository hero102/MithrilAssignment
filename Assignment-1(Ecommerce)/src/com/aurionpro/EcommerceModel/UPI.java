package com.aurionpro.EcommerceModel;

public class UPI implements IPaymentGateway{
	private double balance = 10000;
	
    public boolean pay(double amount) {
    	if(amount<=balance) {
        System.out.println("Paid ₹" + amount + " using NetBanking");
        return true;
    	}
    	System.out.println("Amount should not exceeding the balance amount.");
    	return false;
    }
    public double getBalance() {
    	return balance;
    }

    public void refund(double amount) {
    	double refund;
    	if(amount>3000) {
    		refund =(amount*10)/100;
    		 System.out.printf("Congratulations You have get ₹%.0f Cashback",refund);
    	}
    }
}
