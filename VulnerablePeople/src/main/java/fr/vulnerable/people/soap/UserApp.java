package fr.vulnerable.people.soap;

import jakarta.xml.ws.Endpoint;

public class UserApp {
	public static final String HOST = "127.0.0.1";
	public static final int PORT = 6969;
	
	public static void main(String[] args) {
		final String url = "http://" + UserApp.HOST + ":" + UserApp.PORT + "/";
		Endpoint.publish(url, new AddNewUser());
		Endpoint.publish(url, new AddRequest());
		System.out.println("Start...");
	}
}
