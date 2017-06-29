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
package org.springframework.security.oauth2.config.xml;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpointHandlerMapping;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Dave Syer
 * 
 */
@RunWith(Parameterized.class)
public class AuthorizationServerBeanDefinitionParserTests {

	private static final String CHECK_TOKEN_CUSTOM_ENDPOINT_RESOURCE = "authorization-server-check-token-custom-endpoint";

	private ConfigurableApplicationContext context;

	private String resource;

	@Rule
	public ExpectedException expected = ExpectedException.none();

	@Parameters
	public static List<Object[]> parameters() {
		return Arrays.asList(new Object[] { "authorization-server-vanilla" },
				new Object[] { "authorization-server-extras" },
				new Object[] { "authorization-server-types" },
				new Object[] { "authorization-server-check-token" },
				new Object[] { "authorization-server-disable" },
				new Object[] { CHECK_TOKEN_CUSTOM_ENDPOINT_RESOURCE });
	}

	public AuthorizationServerBeanDefinitionParserTests(String resource) {
		this.resource = resource;
		this.context = new GenericXmlApplicationContext(getClass(), resource + ".xml");
	}

	@After
	public void close() {
		if (context != null) {
			context.close();
		}
	}

	@Test
	public void testDefaults() {
		assertTrue(context.containsBeanDefinition("oauth2AuthorizationEndpoint"));
	}

	@Test
	public void testCheckTokenCustomEndpoint() {
		if (!CHECK_TOKEN_CUSTOM_ENDPOINT_RESOURCE.equals(this.resource)) {
			return;
		}
		FrameworkEndpointHandlerMapping frameworkEndpointHandlerMapping = context.getBean(FrameworkEndpointHandlerMapping.class);
		assertNotNull(frameworkEndpointHandlerMapping);
		assertEquals("/custom_check_token", frameworkEndpointHandlerMapping.getPath("/oauth/check_token"));
	}
}
