package com.aurionpro.EcommerceModel;

public interface IPaymentGateway {
	 boolean pay(double amount) throws PaymentFailedException;
    void refund(double amount);
    double getBalance();

}
