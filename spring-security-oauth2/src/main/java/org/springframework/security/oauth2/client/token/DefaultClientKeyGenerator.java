/*
 * Copyright 2006-2011 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.springframework.security.oauth2.client.token;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.util.OAuth2Utils;

/**
 * Basic key generator taking into account the client id, scope and username (principal name) if they exist.
 * 
 * @author Dave Syer
 * 
 */
public class DefaultClientKeyGenerator implements ClientKeyGenerator {

	private static final String CLIENT_ID = "client_id";

	private static final String SCOPE = "scope";

	private static final String USERNAME = "username";

	public String extractKey(OAuth2ProtectedResourceDetails resource, Authentication authentication) {
		Map<String, String> values = new LinkedHashMap<String, String>();
		if (authentication != null) {
			values.put(USERNAME, authentication.getName());
		}
		values.put(CLIENT_ID, resource.getClientId());
		if (resource.getScope() != null) {
			values.put(SCOPE, OAuth2Utils.formatParameterList(resource.getScope()));
		}
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
		}

		try {
			byte[] bytes = digest.digest(values.toString().getBytes("UTF-8"));
			return String.format("%032x", new BigInteger(1, bytes));
		}
		catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
		}
	}

}
