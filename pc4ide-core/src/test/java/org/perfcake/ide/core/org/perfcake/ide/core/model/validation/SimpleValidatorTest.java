/*
 *-----------------------------------------------------------------------------
 * pc4ide
 *
 * Copyright 2017 Jakub Knetl
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
 *-----------------------------------------------------------------------------
 */

package org.perfcake.ide.core.org.perfcake.ide.core.model.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import org.junit.Test;
import org.perfcake.ide.core.docs.DocsServiceImpl;
import org.perfcake.ide.core.model.PropertyInfo;
import org.perfcake.ide.core.model.components.GeneratorModel;
import org.perfcake.ide.core.model.properties.KeyValueImpl;
import org.perfcake.ide.core.model.properties.SimpleValue;
import org.perfcake.ide.core.model.validation.EnumValidator;
import org.perfcake.ide.core.model.validation.IntegerValidator;
import org.perfcake.ide.core.model.validation.StringValidator;
import org.perfcake.ide.core.model.validation.error.CompoundError;
import org.perfcake.ide.core.model.validation.error.ErrorType;
import org.perfcake.ide.core.model.validation.error.ValidationError;
import org.perfcake.ide.core.utils.TestUtils;

/**
 * Tests individual validators.
 * @author Jakub Knetl
 */
public class SimpleValidatorTest {

    @Test
    public void testStringValidator() {
        SimpleValue p = new SimpleValue("value");
        StringValidator validator = new StringValidator();

        assertThat(validator.validate(p, p.getValue()), nullValue());
    }

    @Test
    public void testEnumValidator() {
        SimpleValue value = new SimpleValue("hello");
        EnumValidator enumValidator = new EnumValidator("a", "b", "c");

        assertThat(enumValidator.validate(value, "a"), nullValue());
        assertThat(enumValidator.validate(value, "b"), nullValue());
        assertThat(enumValidator.validate(value, "c"), nullValue());
        assertThat(enumValidator.validate(value, "d"), not(nullValue()));

        ValidationError error = enumValidator.validate(value, "d");

        assertThat(error.getErrorType(), equalTo(ErrorType.INVALID_VALUE));
    }

    @Test
    public void testModelValidator() throws IOException {
        GeneratorModel generator = new GeneratorModel(new DocsServiceImpl(TestUtils.loadJavadocProperties()));
        assertThat(generator.isValid(), equalTo(false));
        ValidationError error = generator.getValidationError();
        assertThat(error, instanceOf(CompoundError.class));
        assertThat(error.getErrorType(), equalTo(ErrorType.COMPOUND_ERROR));

        CompoundError compoundError = (CompoundError) error;
        // 2 errors expected (missing class and missing run)
        assertThat(compoundError.getErrors().size(), equalTo(2));

        PropertyInfo threadsInfo = generator.getSupportedProperty(GeneratorModel.PropertyNames.THREADS.toString());
        PropertyInfo runInfo = generator.getSupportedProperty(GeneratorModel.PropertyNames.RUN.toString());
        PropertyInfo implInfo = generator.getSupportedProperty(GeneratorModel.PropertyNames.IMPLEMENTATION.toString());

        SimpleValue threadsProperty = new SimpleValue("10");
        generator.addProperty(threadsInfo, threadsProperty);
        generator.addProperty(runInfo, new KeyValueImpl("iterations", "2000"));
        generator.addProperty(implInfo, new SimpleValue("DefaultMessageGenerator"));

        // assert that generator is valid now
        assertThat(generator.isValid(), equalTo(true));

        // make generator invalid again by setting thread count to string
        threadsProperty.setValue("threads-count");
        assertThat(generator.isValid(), equalTo(false));


    }
}
