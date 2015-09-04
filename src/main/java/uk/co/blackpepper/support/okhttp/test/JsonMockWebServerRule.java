package uk.co.blackpepper.support.okhttp.test;

import java.io.IOException;
import java.net.HttpURLConnection;

import com.google.common.io.Files;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

public abstract class JsonMockWebServerRule extends MockWebServerRule {
	
	public JsonMockWebServerRule(int port) {
		super(port);
	}
	
	@Override
	protected MockResponse newResponse(RecordedRequest request) {
		String path = request.getPath();
		
		MockResponse response;
		
		if ("/".equals(path)) {
			response = newJsonResponse("index.json");
		}
		else {
			response = newJsonResponse(path);
		}
		
		return response;
	}
	
	protected final MockResponse newJsonResponse(String path) {
		MockResponse response;
		
		try {
			response = new MockResponse()
				.addHeader("Content-Type", "application/json; charset=utf-8")
				.setBody(getContent(Files.getNameWithoutExtension(path) + ".json"));
		}
		catch (IllegalArgumentException | IOException exception) {
			response = new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
		}
		
		return response;
	}
}
