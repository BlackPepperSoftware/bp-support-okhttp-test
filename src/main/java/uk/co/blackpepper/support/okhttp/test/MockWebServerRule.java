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
