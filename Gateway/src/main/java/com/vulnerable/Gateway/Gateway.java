package com.vulnerable.Gateway;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@RestController
public class Gateway {	
	public static String USER_SERVICE    = "http://localhost:5000/user";
	public static String REQUEST_SERVICE = "http://localhost:5001/request";

	
	@PostMapping("/api/add-user")
	public ResponseEntity<String> AddUser(
			@RequestParam("name") String name,
			@RequestParam("role") String role
	) {		
		try {
			MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
			params.add("name", name);
			params.add("role", role);
			return PostRequest(USER_SERVICE + "/add", params);
		} catch (HttpErrorException e) {
			return ResponseEntity.status(e.status).body("ERROR : " + e.status);
		}
	}
	
	@PostMapping("/api/del-user")
	public ResponseEntity<String> DelUser(
			@RequestParam("id") int id
	) {
		try {
			GetRequest(USER_SERVICE + "/" + id);
			MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
			params.add("id", "" + id);
			return PostRequest(USER_SERVICE + "/del", params);
		} catch (HttpErrorException e) {
			return ResponseEntity.status(e.status).body("ERROR : " + e.status);
		}
	}
	
	@PostMapping("/api/add-request")
	public ResponseEntity<String> AddRequest(
			@RequestParam("user_id") int userId
	) {		
		try {			
			ResponseEntity<String> response = GetRequest(USER_SERVICE + "/" + userId + "/role");
			String role = response.getBody();
			
			if (!role.equals("USER")) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not a user");
			
			MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
			params.add("user_id", "" + userId);
			return PostRequest(REQUEST_SERVICE + "/add", params);
		} catch (HttpErrorException e) {
			return ResponseEntity.status(e.status).body("ERROR : " + e.status);
		}
	}
	
	@PostMapping("/api/del-request")
	public ResponseEntity<String> DelRequest(
			@RequestParam("id") int id
	) {
		try {
			MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
			params.add("id", "" + id);
			return PostRequest(REQUEST_SERVICE + "/del", params);
		} catch (HttpErrorException e) {
			return ResponseEntity.status(e.status).body("ERROR : " + e.status);
		}
	}
	
	@PostMapping("/api/accept-request")
	public ResponseEntity<String> AcceptRequest(
			@RequestParam("request_id") int requestId,
			@RequestParam("volunteer_id") int volunteerId
    ) {
		try {
			GetRequest(USER_SERVICE + "/" + volunteerId);
			
			ResponseEntity<String> response = GetRequest(USER_SERVICE + "/" + volunteerId + "/role");
			String role = response.getBody();
			
			if (!role.equals("VOLONTEER")) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not a volOnteer");
			
			MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
			params.add("volunteer_id", "" + volunteerId);			
			return PostRequest(REQUEST_SERVICE + "/" + requestId + "/accept", params);
		} catch (HttpErrorException e) {
			return ResponseEntity.status(e.status).body("ERROR : " + e.status);
		}
	}
	
	private ResponseEntity<String> GetRequest(String url) throws HttpErrorException {
		try {
			RestTemplate rest = new RestTemplate();
			ResponseEntity<String> response = rest.getForEntity(url , String.class);
			
			return response;
		} catch(HttpClientErrorException e) {
			int status = e.getStatusCode().value();
			throw new HttpErrorException(status);
		}
	}
	
	private ResponseEntity<String> PostRequest(String url, MultiValueMap<String, String> datas) throws HttpErrorException {		
		try {
			RestTemplate rest = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(datas, headers);
			ResponseEntity<String> response = rest.postForEntity(url, request, String.class);

			return response;
		} catch(HttpClientErrorException e) {
			int status = e.getStatusCode().value();
			throw new HttpErrorException(status);
		}
	}
}
