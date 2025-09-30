package com.aurionpro.bankapp.service;

import com.aurionpro.bankapp.entity.Customer;
import java.nio.file.AccessDeniedException;

public interface AuthService {
	public Customer registerCustomer(String username, String name, String password, String emailId, String contactNo,
			String dob, String city, String state, String pincode);

	String login(String username, String password);

	void checkAccess(Long resourceCustomerId) throws AccessDeniedException;

	void checkAccountOwnership(Long accountId) throws AccessDeniedException;

	public String getLoggedInUsername() throws AccessDeniedException;
}
