package uk.co.blackpepper.support.okhttp.test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.rules.ExternalResource;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

public abstract class MockWebServerRule extends ExternalResource {

	private static final Logger LOG = Logger.getLogger(MockWebServerRule.class.getName());
	
	private MockWebServer server;

	private final int port;
	
	public MockWebServerRule(int port) {
		this.port = port;
	}

	public void shutdown() throws IOException {
		server.shutdown();
		
		// MockWebServer.shutdown doesn't appear to be synchronous, so wait until it has been properly shutdown
		waitForShutdown(server);
	}

	public final String getUrl(String path) {
		return String.format("http://localhost:%d/%s", port, path);
	}

	@Override
	protected void before() throws IOException {
		server = new MockWebServer();
		
		server.setDispatcher(new Dispatcher() {
			@Override
			public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
				return newResponse(request);
			}
		});
		
		server.play(port);
	}

	@Override
	protected final void after() {
		try {
			shutdown();
		}
		catch (IOException exception) {
			LOG.log(Level.WARNING, "Error shutting down web server", exception);
		}
	}

	protected MockResponse newResponse(RecordedRequest request) {
		return new MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND);
	}

	protected final String getContent(String name) throws IOException {
		return Resources.toString(Resources.getResource(getClass(), name), Charsets.UTF_8);
	}
	
	private static void waitForShutdown(MockWebServer server) {
		while (!isPortAvailable(server.getPort())) {
			try {
				Thread.sleep(10);
			}
			catch (InterruptedException exception) {
				// ignore
			}
		}
	}

	private static boolean isPortAvailable(int port) {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			return true;
		}
		catch (IOException ioException) {
			return false;
		}
	}
}
