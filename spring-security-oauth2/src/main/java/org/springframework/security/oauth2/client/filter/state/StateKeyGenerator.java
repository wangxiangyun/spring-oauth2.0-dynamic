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
package org.springframework.security.oauth2.client.filter.state;

import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

/**
 * Stategy for generating random keys for state. The state key is important protection for client apps against
 * cross-site request forgery.
 * 
 * @author Dave Syer
 * 
 */
public interface StateKeyGenerator {

	/**
	 * Generate a key.
	 * 
	 * @param resource the resource to generate the key for
	 * @return a unique key for the state.  Never null.
	 */
	String generateKey(OAuth2ProtectedResourceDetails resource);

}
