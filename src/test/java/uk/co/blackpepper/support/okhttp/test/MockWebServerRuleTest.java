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
