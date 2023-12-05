package fr.vulnerable.people.soap;

import java.util.ArrayList;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(serviceName="add-new-user")
public class AddNewUser {
	public static final ArrayList<User> USER = new ArrayList<User>();
	
	@WebMethod(operationName="add")
	public String Add(
			@WebParam(name="name") String name,
			@WebParam(name="role") String role
	) {
		AddNewUser.USER.add(new User(name, role));
		return name;
	}
}
