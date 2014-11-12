package com.piotrglazar.nightowl;

import com.piotrglazar.nightowl.configuration.ApplicationConfiguration;
import junitparams.JUnitParamsRunner;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;

@RunWith(JUnitParamsRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@ActiveProfiles("test")
@SuppressWarnings("all")
public abstract class AbstractContextTest {

    @Rule
    public SpringInJUnitParams spring = new SpringInJUnitParams(getClass(), this);

    private static class SpringInJUnitParams extends ExternalResource {

        private final Class<?> testClass;

        private final AbstractContextTest testInstance;

        private SpringInJUnitParams(final Class<?> testClass, final AbstractContextTest testInstance) {
            this.testClass = testClass;
            this.testInstance = testInstance;
        }

        @Override
        protected void before() throws Throwable {
            final TestContextManager testContextManager = new TestContextManager(testClass);
            testContextManager.prepareTestInstance(testInstance);
        }
    }
}
