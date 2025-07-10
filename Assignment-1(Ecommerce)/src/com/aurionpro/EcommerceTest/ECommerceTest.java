package com.aurionpro.EcommerceTest;

import java.util.Scanner;

import com.aurionpro.EcommerceModel.CreditCard;
import com.aurionpro.EcommerceModel.GetInput;
import com.aurionpro.EcommerceModel.IPaymentGateway;
import com.aurionpro.EcommerceModel.InvalidAmountException;
import com.aurionpro.EcommerceModel.InvalidChoiceException;
import com.aurionpro.EcommerceModel.NetBanking;
import com.aurionpro.EcommerceModel.PaymentFailedException;
import com.aurionpro.EcommerceModel.UPI;

public class ECommerceTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GetInput getInput = new GetInput(); 
        IPaymentGateway paymentGateway = null;
   
        //Getting User Choice
        try {
            String paymentChoice = "Choose Payment Method:\n1. Credit Card\n2. UPI\n3. NetBanking";
            int choice = getInput.getChoice(scanner, paymentChoice, 3);

            switch (choice) {
                case 1:
                    paymentGateway = new CreditCard();
                    break;
                case 2:
                    paymentGateway = new UPI();
                    break;
                case 3:
                    paymentGateway = new NetBanking();
                    break;
                default:
                    throw new InvalidChoiceException("Invalid payment option selected.");
            }
            //Displaying Balance of user
            System.out.println("Your Balance is: "+paymentGateway.getBalance());
            while(true) {

            	//Payment And refund Sction
            System.out.print("Enter payment amount: ₹");
            double amount = getInput.getPositiveNumber(scanner);
            if (amount <= 0)
                throw new InvalidAmountException("Amount must be greater than zero.");

            try {
                if(paymentGateway.pay(amount)) {
                paymentGateway.refund(amount);
                break;
                }
      
            } catch (PaymentFailedException e) {
                System.out.println("Payment Error: " + e.getMessage());
            }
            }

        } catch (InvalidChoiceException | InvalidAmountException e) {
            System.out.println("Input Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
