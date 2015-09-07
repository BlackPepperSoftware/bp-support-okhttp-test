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

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Rule;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import okio.Buffer;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JsonMockWebServerRuleTest {

	private JsonMockWebServerRule rule = new JsonMockWebServerRule(8081) {
		// concrete class
	};
	
	@Rule
	public JsonMockWebServerRule getRule() {
		return rule;
	}
	
	@Test
	public void newResponseWithRootReturnsIndex() throws IOException {
		MockResponse actual = rule.newResponse(get("/"));

		assertThat(toString(actual.getBody()),
			is(fileToString("src/test/resources/uk/co/blackpepper/support/okhttp/test/index.json")));
	}
	
	@Test
	public void newResponseWithRootSetsContentType() {
		MockResponse actual = rule.newResponse(get("/"));

		assertThat(actual.getHeaders(), hasItem("Content-Type: application/json; charset=utf-8"));
	}
	
	@Test
	public void newResponseWithPathReturnsFile() throws IOException {
		MockResponse actual = rule.newResponse(get("/x"));

		assertThat(toString(actual.getBody()),
			is(fileToString("src/test/resources/uk/co/blackpepper/support/okhttp/test/x.json")));
	}
	
	@Test
	public void newResponseWithPathSetsContentType() {
		MockResponse actual = rule.newResponse(get("/x"));

		assertThat(actual.getHeaders(), hasItem("Content-Type: application/json; charset=utf-8"));
	}
	
	@Test
	public void newResponseWithUnknownPathReturnsNotFound() {
		MockResponse actual = rule.newResponse(get("/y"));
		
		assertThat(actual.getStatus(), is("HTTP/1.1 404 OK"));
	}
	
	private static RecordedRequest get(String path) {
		return new RecordedRequest(String.format("GET %s HTTP/1.1", path), null, null, 0, new byte[0], 0, null);
	}
	
	private static String toString(Buffer actual) throws IOException {
		return CharStreams.toString(new InputStreamReader(actual.inputStream(), Charsets.UTF_8));
	}
	
	private static String fileToString(String path) throws IOException {
		return Files.toString(new File(path), Charsets.UTF_8);
	}
}
