package org.springframework.security.oauth2.common.exceptions;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Base exception for OAuth 2 exceptions.
 * 
 * @author Ryan Heaton
 * @author Rob Winch
 * @author Dave Syer
 */
@SuppressWarnings("serial")
@org.codehaus.jackson.map.annotate.JsonSerialize(using = OAuth2ExceptionJackson1Serializer.class)
@org.codehaus.jackson.map.annotate.JsonDeserialize(using = OAuth2ExceptionJackson1Deserializer.class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = OAuth2ExceptionJackson2Serializer.class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = OAuth2ExceptionJackson2Deserializer.class)
public class OAuth2Exception extends RuntimeException {

	public static final String ERROR = "error";
	public static final String DESCRIPTION = "error_description";
	public static final String URI = "error_uri";
	public static final String INVALID_REQUEST = "invalid_request";
	public static final String INVALID_CLIENT = "invalid_client";
	public static final String INVALID_GRANT = "invalid_grant";
	public static final String UNAUTHORIZED_CLIENT = "unauthorized_client";
	public static final String UNSUPPORTED_GRANT_TYPE = "unsupported_grant_type";
	public static final String INVALID_SCOPE = "invalid_scope";
	public static final String INSUFFICIENT_SCOPE = "insufficient_scope";
	public static final String INVALID_TOKEN = "invalid_token";
	public static final String REDIRECT_URI_MISMATCH ="redirect_uri_mismatch";
	public static final String UNSUPPORTED_RESPONSE_TYPE ="unsupported_response_type";
	public static final String ACCESS_DENIED = "access_denied";
	public static final String USERNAME_NOT_FOUND = "username_not_found";

	private Map<String, String> additionalInformation = null;

	public OAuth2Exception(String msg, Throwable t) {
		super(msg, t);
	}

	public OAuth2Exception(String msg) {
		super(msg);
	}

	/**
	 * The OAuth2 error code.
	 * 
	 * @return The OAuth2 error code.
	 */
	public String getOAuth2ErrorCode() {
		return "invalid_request";
	}

	/**
	 * The HTTP error code associated with this error.
	 * 
	 * @return The HTTP error code associated with this error.
	 */
	public int getHttpErrorCode() {
		return 400;
	}

	/**
	 * Get any additional information associated with this error.
	 * 
	 * @return Additional information, or null if none.
	 */
	public Map<String, String> getAdditionalInformation() {
		return this.additionalInformation;
	}

	/**
	 * Add some additional information with this OAuth error.
	 * 
	 * @param key The key.
	 * @param value The value.
	 */
	public void addAdditionalInformation(String key, String value) {
		if (this.additionalInformation == null) {
			this.additionalInformation = new TreeMap<String, String>();
		}

		this.additionalInformation.put(key, value);

	}

	/**
	 * Creates the appropriate subclass of OAuth2Exception given the errorCode.
	 * @param errorCode
	 * @param errorMessage
	 * @return
	 */
	public static OAuth2Exception create(String errorCode, String errorMessage) {
		if (errorMessage == null) {
			errorMessage = errorCode == null ? "OAuth Error" : errorCode;
		}
		if (INVALID_CLIENT.equals(errorCode)) {
			return new InvalidClientException(errorMessage);
		}
		else if (UNAUTHORIZED_CLIENT.equals(errorCode)) {
			return new UnauthorizedClientException(errorMessage);
		}
		else if (INVALID_GRANT.equals(errorCode)) {
			return new InvalidGrantException(errorMessage);
		}
		else if (INVALID_SCOPE.equals(errorCode)) {
			return new InvalidScopeException(errorMessage);
		}
		else if (INVALID_TOKEN.equals(errorCode)) {
			return new InvalidTokenException(errorMessage);
		}
		else if (INVALID_REQUEST.equals(errorCode)) {
			return new InvalidRequestException(errorMessage);
		}
		else if (REDIRECT_URI_MISMATCH.equals(errorCode)) {
			return new RedirectMismatchException(errorMessage);
		}
		else if (UNSUPPORTED_GRANT_TYPE.equals(errorCode)) {
			return new UnsupportedGrantTypeException(errorMessage);
		}
		else if (UNSUPPORTED_RESPONSE_TYPE.equals(errorCode)) {
			return new UnsupportedResponseTypeException(errorMessage);
		}
		else if (ACCESS_DENIED.equals(errorCode)) {
			return new UserDeniedAuthorizationException(errorMessage);
		}
		else {
			return new OAuth2Exception(errorMessage);
		}
	}
	public static OAuth2Exception create(String errorCode) {
		if (INVALID_CLIENT.equals(errorCode)) {
			return new InvalidClientException("无效的client");
		}
		else if (UNAUTHORIZED_CLIENT.equals(errorCode)) {
			return new UnauthorizedClientException("没有授权的client");
		}
		else if (INVALID_GRANT.equals(errorCode)) {
			return new InvalidGrantException("用户名或密码错误");
		}
		else if (INVALID_SCOPE.equals(errorCode)) {
			return new InvalidScopeException("无效的scope");
		}
		else if (INVALID_TOKEN.equals(errorCode)) {
			return new InvalidTokenException("token无效");
		}
		else if (INVALID_REQUEST.equals(errorCode)) {
			return new InvalidRequestException("无效的请求");
		}
		else if (REDIRECT_URI_MISMATCH.equals(errorCode)) {
			return new RedirectMismatchException("注册的跳转地址错误");
		}
		else if (UNSUPPORTED_GRANT_TYPE.equals(errorCode)) {
			return new UnsupportedGrantTypeException("不支持的GrantType");
		}
		else if (UNSUPPORTED_RESPONSE_TYPE.equals(errorCode)) {
			return new UnsupportedResponseTypeException("不支持的ResponseType");
		}
		else if (ACCESS_DENIED.equals(errorCode)) {
			return new UserDeniedAuthorizationException("用户没有权限");
		}
		else if (USERNAME_NOT_FOUND.equals(errorCode)) {
			return new UserDeniedAuthorizationException("用户名不正确");
		}
		else {
			return new OAuth2Exception("oauth2 error");
		}
	}
	/**
	 * Creates an {@link OAuth2Exception} from a Map&lt;String,String&gt;.
	 * 
	 * @param errorParams
	 * @return
	 */
	public static OAuth2Exception valueOf(Map<String, String> errorParams) {
		String errorCode = errorParams.get(ERROR);
		String errorMessage = errorParams.containsKey(DESCRIPTION) ? errorParams.get(DESCRIPTION)
				: null;
		OAuth2Exception ex = create(errorCode, errorMessage);
		Set<Map.Entry<String, String>> entries = errorParams.entrySet();
		for (Map.Entry<String, String> entry : entries) {
			String key = entry.getKey();
			if (!ERROR.equals(key) && !DESCRIPTION.equals(key)) {
				ex.addAdditionalInformation(key, entry.getValue());
			}
		}

		return ex;
	}
	
	@Override
	public String toString() {
		return getSummary();
	}

	/**
	 * @return a comma-delimited list of details (key=value pairs)
	 */
	public String getSummary() {
		
		StringBuilder builder = new StringBuilder();

		String delim = "";

		String error = this.getOAuth2ErrorCode();
		if (error != null) {
			builder.append(delim).append("error=\"").append(error).append("\"");
			delim = ", ";
		}

		String errorMessage = this.getMessage();
		if (errorMessage != null) {
			builder.append(delim).append("error_description=\"").append(errorMessage).append("\"");
			delim = ", ";
		}

		Map<String, String> additionalParams = this.getAdditionalInformation();
		if (additionalParams != null) {
			for (Map.Entry<String, String> param : additionalParams.entrySet()) {
				builder.append(delim).append(param.getKey()).append("=\"").append(param.getValue()).append("\"");
				delim = ", ";
			}
		}
		
		return builder.toString();

	}
}
