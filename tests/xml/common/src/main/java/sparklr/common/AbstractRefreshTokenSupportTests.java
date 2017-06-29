package sparklr.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author Dave Syer
 */
public abstract class AbstractRefreshTokenSupportTests extends AbstractIntegrationTests {

	/**
	 * tests a happy-day flow of the refresh token provider.
	 */
	@Test
	public void testHappyDay() throws Exception {

		OAuth2AccessToken accessToken = getAccessToken("read write", "my-trusted-client");

		// now use the refresh token to get a new access token.
		assertNotNull(accessToken.getRefreshToken());
		OAuth2AccessToken newAccessToken = refreshAccessToken(accessToken.getRefreshToken().getValue());
		assertFalse(newAccessToken.getValue().equals(accessToken.getValue()));

		verifyAccessTokens(accessToken, newAccessToken);

	}

	protected void verifyAccessTokens(OAuth2AccessToken oldAccessToken, OAuth2AccessToken newAccessToken) {
		// make sure the new access token can be used.
		verifyTokenResponse(newAccessToken.getValue(), HttpStatus.OK);
		// make sure the old access token isn't valid anymore.
		verifyTokenResponse(oldAccessToken.getValue(), HttpStatus.UNAUTHORIZED);
	}

	protected void verifyTokenResponse(String accessToken, HttpStatus status) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", String.format("%s %s", OAuth2AccessToken.BEARER_TYPE, accessToken));
		assertEquals(status, http.getStatusCode("/admin/beans", headers));
	}

	private OAuth2AccessToken refreshAccessToken(String refreshToken) {

		MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
		formData.add("grant_type", "refresh_token");
		formData.add("client_id", "my-trusted-client");
		formData.add("refresh_token", refreshToken);
		formData.add("scope", "read");
		HttpHeaders headers = getTokenHeaders("my-trusted-client");

		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> response = http.postForMap(tokenPath(), headers, formData);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue("Wrong cache control: " + response.getHeaders().getFirst("Cache-Control"), response.getHeaders()
				.getFirst("Cache-Control").contains("no-store"));
		@SuppressWarnings("unchecked")
		OAuth2AccessToken newAccessToken = DefaultOAuth2AccessToken.valueOf(response.getBody());
		return newAccessToken;

	}

	private OAuth2AccessToken getAccessToken(String scope, String clientId) throws Exception {
		MultiValueMap<String, String> formData = getTokenFormData(scope, clientId);
		HttpHeaders headers = getTokenHeaders(clientId);
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> response = http.postForMap(tokenPath(), headers, formData);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue("Wrong cache control: " + response.getHeaders().getFirst("Cache-Control"), response.getHeaders()
				.getFirst("Cache-Control").contains("no-store"));

		@SuppressWarnings("unchecked")
		OAuth2AccessToken accessToken = DefaultOAuth2AccessToken.valueOf(response.getBody());
		return accessToken;
	}

	private HttpHeaders getTokenHeaders(String clientId) {
		HttpHeaders headers = new HttpHeaders();
		if (clientId != null) {
			headers.set("Authorization", "Basic " + new String(Base64.encode((clientId + ":").getBytes())));
		}
		return headers ;
	}

	private MultiValueMap<String, String> getTokenFormData(String scope, String clientId) {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
		formData.add("grant_type", "password");
		if (clientId != null) {
			formData.add("client_id", clientId);
		}
		formData.add("scope", scope);
		formData.add("username", "user");
		formData.add("password", "password");
		return formData;
	}
}
