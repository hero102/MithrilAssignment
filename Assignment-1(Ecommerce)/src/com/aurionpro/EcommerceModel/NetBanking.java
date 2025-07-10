package com.aurionpro.EcommerceModel;

public class NetBanking implements IPaymentGateway {
	private double balance = 12000;
	
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
    	if(amount>2000) {
    		refund =(amount*10)/100;
    		 System.out.printf("Congratulations You have get ₹%.0f Cashback",refund);
    	}
       
    }
}