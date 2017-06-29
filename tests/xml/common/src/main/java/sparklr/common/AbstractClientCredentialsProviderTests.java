package sparklr.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * @author Ryan Heaton
 * @author Dave Syer
 */
public abstract class AbstractClientCredentialsProviderTests extends AbstractIntegrationTests {

	private HttpHeaders responseHeaders;

	private HttpStatus responseStatus;

	/**
	 * tests the basic provider
	 */
	@Test
	@OAuth2ContextConfiguration(ClientCredentials.class)
	public void testPostForToken() throws Exception {
		OAuth2AccessToken token = context.getAccessToken();
		assertNull(token.getRefreshToken());
	}

	/**
	 * tests that the registered scopes are used as defaults
	 */
	@Test
	@OAuth2ContextConfiguration(NoScopeClientCredentials.class)
	public void testPostForTokenWithNoScopes() throws Exception {
		OAuth2AccessToken token = context.getAccessToken();
		assertFalse("Wrong scope: " + token.getScope(), token.getScope().isEmpty());
	}

	@Test
	@OAuth2ContextConfiguration(resource = InvalidClientCredentials.class, initialize = false)
	public void testInvalidCredentials() throws Exception {
		context.setAccessTokenProvider(new ClientCredentialsAccessTokenProvider() {
			@Override
			protected ResponseErrorHandler getResponseErrorHandler() {
				return new DefaultResponseErrorHandler() {
					public void handleError(ClientHttpResponse response) throws IOException {
						responseHeaders = response.getHeaders();
						responseStatus = response.getStatusCode();
					}
				};
			}
		});
		try {
			context.getAccessToken();
			fail("Expected ResourceAccessException");
		}
		catch (Exception e) {
			// System.err.println(responseHeaders);
			// ignore
		}
		String header = responseHeaders.getFirst("WWW-Authenticate");
		assertTrue("Wrong header: " + header, header.contains("Basic realm"));
		assertEquals(HttpStatus.UNAUTHORIZED, responseStatus);
	}

	protected static class ClientCredentials extends ClientCredentialsResourceDetails {

		public ClientCredentials(Object target) {
			setClientId("my-client-with-secret");
			setClientSecret("secret");
			setScope(Arrays.asList("read"));
			setId(getClientId());
		}
	}

	static class InvalidClientCredentials extends ClientCredentials {
		public InvalidClientCredentials(Object target) {
			super(target);
			setClientId("my-client-with-secret");
			setClientSecret("wrong");
		}
	}

	static class NoScopeClientCredentials extends ClientCredentialsResourceDetails {
		public NoScopeClientCredentials(Object target) {
			setClientId("my-client-with-secret");
			setClientSecret("secret");
			setId(getClientId());
		}
	}


}
