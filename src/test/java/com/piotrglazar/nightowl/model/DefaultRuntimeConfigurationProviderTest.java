package com.piotrglazar.nightowl.model;

import com.google.common.collect.Lists;
import com.piotrglazar.nightowl.model.entities.RuntimeConfiguration;
import com.piotrglazar.nightowl.model.entities.SkyObjectVisibilitySettings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultRuntimeConfigurationProviderTest {

    @Mock
    private SkyObjectVisibilitySettingsRepository settingsRepository;

    @Mock
    private RuntimeConfigurationRepository repository;

    @InjectMocks
    private DefaultRuntimeConfigurationProvider provider;

    @Test
    public void shouldFailWhenThereIsMoreThanOneRuntimeConfiguration() {
        // given
        given(repository.findAll()).willReturn(Lists.newArrayList(new RuntimeConfiguration(), new RuntimeConfiguration()));

        // when
        catchException(provider).getConfiguration();

        // then
        assertThat((Exception) caughtException()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void shouldUseRepositoryToUpdateConfiguration() {
        // given
        final RuntimeConfiguration runtimeConfiguration = new RuntimeConfiguration();

        // when
        provider.updateConfiguration(runtimeConfiguration);

        // then
        verify(settingsRepository).save(any(SkyObjectVisibilitySettings.class));
        verify(repository).save(runtimeConfiguration);
    }
}
