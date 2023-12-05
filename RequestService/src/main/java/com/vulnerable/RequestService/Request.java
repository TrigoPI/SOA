package com.vulnerable.RequestService;

public class Request {
	public static final String WAITING   = "WAITING";
	public static final String VALIDATED = "VALIDATED";
	public static final String REJECTED  = "REJECTED";
	public static final String CHOSEN 	 = "CHOSEN";
	public static final String REALIZED  = "REALIZED";
	
	public static int NO_VOLUNTEER = -1;
	
	public int m_userId;
	public int m_volunteerId;
	public String m_state;
	
	public Request(int userId) {
		m_userId = userId;
		m_state = WAITING;
		m_volunteerId = Request.NO_VOLUNTEER;
	}
	
	public void ChangeState(String state) {
		m_state = state;
	}
	
	public void SetVolunteer(int volunteerId) {
		m_volunteerId = volunteerId;
	}
}
