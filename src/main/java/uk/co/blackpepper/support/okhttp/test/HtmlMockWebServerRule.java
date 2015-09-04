package uk.co.blackpepper.support.okhttp.test;

import java.io.IOException;
import java.net.HttpURLConnection;

import com.google.common.io.Files;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

public abstract class HtmlMockWebServerRule extends MockWebServerRule {

	public HtmlMockWebServerRule(int port) {
		super(port);
	}

	@Override
	protected MockResponse newResponse(RecordedRequest request) {
		String path = request.getPath();
		
		MockResponse response;
		
		if ("/favicon.ico".equals(path)) {
			response = new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
		}
		else if ("/".equals(path)) {
			response = newHtmlResponse("index.html");
		}
		else {
			response = newHtmlResponse(path);
		}
		
		return response;
	}

	protected final MockResponse newHtmlResponse(String path) {
		MockResponse response;
		
		try {
			response = new MockResponse()
				.addHeader("Content-Type", "text/html; charset=utf-8")
				.setBody(getContent(Files.getNameWithoutExtension(path) + ".html"));
		}
		catch (IllegalArgumentException | IOException exception) {
			response = new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
		}
		
		return response;
	}
}
