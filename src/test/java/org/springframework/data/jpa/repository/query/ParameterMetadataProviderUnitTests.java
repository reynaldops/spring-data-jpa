/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.jpa.repository.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Iterator;

import javax.persistence.criteria.CriteriaBuilder;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.repository.query.Parameters;
import org.springframework.data.repository.query.parser.Part;

/**
 * Unit tests for {@link ParameterMetadataProvider}.
 *
 * @author Jens Schauder
 */
public class ParameterMetadataProviderUnitTests {

	@SuppressWarnings("unchecked")
    @Test // DATAJPA-863
	public void errorMessageMentionesParametersWhenParametersAreExhausted() {

		PersistenceProvider persistenceProvider = mock(PersistenceProvider.class);
		CriteriaBuilder builder = mock(CriteriaBuilder.class);

		Parameters<?, ?> parameters = mock(Parameters.class, RETURNS_DEEP_STUBS);
		when(parameters.getBindableParameters().iterator()).thenReturn((Iterator) Collections.emptyListIterator());

		ParameterMetadataProvider metadataProvider = new ParameterMetadataProvider(builder, parameters,
				persistenceProvider);

		try {

			metadataProvider.next(mock(Part.class));
			fail("Should have thrown an exception.");
		} catch (RuntimeException re) {
			assertThat(re.getMessage(), Matchers.containsString("parameter"));
		}
	}

}