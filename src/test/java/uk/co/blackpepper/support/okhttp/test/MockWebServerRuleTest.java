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
import java.net.URL;

import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MockWebServerRuleTest {

	private MockWebServerRule rule = new MockWebServerRule(8081) {
		// concrete class
	};
	
	@Rule
	public MockWebServerRule getRule() {
		return rule;
	}
	
	@Test
	public void getReturnsNotFound() throws IOException {
		int actual = get("http://localhost:8081/x").getResponseCode();

		assertThat(actual, is(404));
	}
	
	@Test
	public void getUrlWithPathReturnsUrl() {
		String actual = rule.getUrl("x");
		
		assertThat(actual, is("http://localhost:8081/x"));
	}
	
	private static HttpURLConnection get(String url) throws IOException {
		return (HttpURLConnection) new URL(url).openConnection();
	}
}
