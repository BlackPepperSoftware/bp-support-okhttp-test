/*
 * Copyright 2014 Black Pepper Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
