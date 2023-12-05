package com.vulnerable.RequestService;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController()
public class RequestService {
	public static final HashMap<Integer, Request> m_request = new HashMap<Integer, Request>();
	public static int m_id = 0;
	
	@PostMapping("/request/add")
	public ResponseEntity<String> AddRequest(
			@RequestParam("user_id") int userId
	) {
		System.out.println("Creating new request");
		
		int id = m_id++;
		Request request = new Request(userId);
		String res = String.format("{id: %d}", id);

		m_request.put(id, request);
		
		return ResponseEntity.ok(res);
	}
	
	@PostMapping("/request/del")
	public ResponseEntity<String> DelRequest(
			@RequestParam("id") int id
	) {
		if (!IsIdValid(id)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		System.out.println("Deleting request");
		m_request.remove(id);
		return ResponseEntity.ok("Deleting request");
	}
	
	@PostMapping("/request/{request_id}/accept")
	public ResponseEntity<String> AcceptRequest(
			@PathVariable("request_id") int requestId,
			@RequestParam("volunteer_id") int volunteerId
    ) {
		if (!IsIdValid(requestId)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
		System.out.println("Accepting request");
		Request request = m_request.get(requestId);
		
		if (request.m_state != Request.WAITING) return new ResponseEntity<>(HttpStatus.CONFLICT);
		
		request.ChangeState(Request.CHOSEN);
		String res = String.format("Request : %d accepted by volunteer : %d", requestId, volunteerId);
		return ResponseEntity.ok(res);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<String> GetRequest(
			@PathVariable("id") int id
    ) {
		if (!IsIdValid(id)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		Request request = m_request.get(id);
		String res = String.format("{'user_id': %d, 'request_id': %d}", request.m_userId, request.m_volunteerId);
		return ResponseEntity.ok(res);
	}
	
	private boolean IsIdValid(int id) {
		return m_request.containsKey(id);
	}
}


/*@PostMapping("/request/{request_id}/{state}")
public ResponseEntity<String>  ChangeRequestState(
		@PathVariable("request_id") int requestId,
		@PathVariable("state") String state
) {
	if (!IsIdValid(requestId)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	Request request = m_request.get(requestId);
	if (request.m_volunteerId != Request.NO_VOLUNTEER) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	request.SetVolunteer(requestId);
	return ResponseEntity.ok("");
}*/