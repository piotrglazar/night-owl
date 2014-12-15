package com.piotrglazar.nightowl.configuration;

import com.google.common.collect.Lists;
import com.piotrglazar.nightowl.api.DatabaseFromScriptReader;
import com.piotrglazar.nightowl.model.RuntimeConfigurationRepository;
import com.piotrglazar.nightowl.model.SkyObjectVisibilitySettingsRepository;
import com.piotrglazar.nightowl.model.UserLocationRepository;
import com.piotrglazar.nightowl.model.entities.RuntimeConfiguration;
import com.piotrglazar.nightowl.model.entities.SkyObjectVisibilitySettings;
import com.piotrglazar.nightowl.model.entities.UserLocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDatabasePopulatorTest {

    @Mock
    private UserLocationRepository userLocationRepository;

    @Mock
    private RuntimeConfigurationRepository runtimeConfigurationRepository;

    @Mock
    private SkyObjectVisibilitySettingsRepository skyObjectVisibilitySettingsRepository;

    @Mock
    private DatabaseFromScriptReader databaseFromScriptReader;

    @InjectMocks
    private DefaultDatabasePopulator defaultDatabasePopulator;

    @Test
    public void shouldLoadEntitiesFromScript() {
        // when
        defaultDatabasePopulator.prepareDatabase();

        // then
        verify(databaseFromScriptReader).createDatabaseFromScript();
    }

    @Test
    public void shouldInsertUserLocationOnlyIfNoneExists() {
        // given
        given(userLocationRepository.count()).willReturn(noEntitiesExist());

        // when
        defaultDatabasePopulator.prepareDatabase();

        // then
        verify(userLocationRepository, atLeastOnce()).save(any(UserLocation.class));
    }

    @Test
    public void shouldUseAlreadyExistingUserLocationIfAnyExists() {
        // given
        given(userLocationRepository.count()).willReturn(someEntitiesExist());
        given(userLocationRepository.findAll()).willReturn(Lists.newArrayList(UserLocation.builder().build()));

        // when
        defaultDatabasePopulator.prepareDatabase();

        // then
        verify(userLocationRepository).findAll();
    }

    @Test
    public void shouldInsertSkyObjectVisibilitySettingsOnlyIfNoneExists() {
        // given
        given(skyObjectVisibilitySettingsRepository.count()).willReturn(noEntitiesExist());

        // when
        defaultDatabasePopulator.prepareDatabase();

        // then
        verify(skyObjectVisibilitySettingsRepository).saveAndFlush(any(SkyObjectVisibilitySettings.class));
    }

    @Test
    public void shouldUseAlreadyExistingSkyObjectVisibilitySettingsIfAnyExists() {
        // given
        given(skyObjectVisibilitySettingsRepository.count()).willReturn(someEntitiesExist());
        given(skyObjectVisibilitySettingsRepository.findAll()).willReturn(Lists.newArrayList(new SkyObjectVisibilitySettings()));

        // when
        defaultDatabasePopulator.prepareDatabase();

        // then
        verify(skyObjectVisibilitySettingsRepository).findAll();
    }

    @Test
    public void shouldInsertRuntimeConfigurationOnlyIfNoneExists() {
        // given
        given(runtimeConfigurationRepository.count()).willReturn(noEntitiesExist());

        // when
        defaultDatabasePopulator.prepareDatabase();

        // then
        verify(runtimeConfigurationRepository).saveAndFlush(any(RuntimeConfiguration.class));
    }

    @Test
    public void shouldDoNothingWhenRuntimeConfigurationAlreadyExists() {
        // given
        given(runtimeConfigurationRepository.count()).willReturn(someEntitiesExist());

        // when
        defaultDatabasePopulator.prepareDatabase();

        // then
        verify(runtimeConfigurationRepository, never()).saveAndFlush(any(RuntimeConfiguration.class));
    }

    @Test
    public void shouldPrepareUserLocationAndSkyObjectVisibilitySettingsBeforeRuntimeConfiguration() {
        // given
        given(userLocationRepository.count()).willReturn(someEntitiesExist());
        given(userLocationRepository.findAll()).willReturn(Lists.newArrayList(UserLocation.builder().build()));
        given(skyObjectVisibilitySettingsRepository.count()).willReturn(someEntitiesExist());
        given(skyObjectVisibilitySettingsRepository.findAll()).willReturn(Lists.newArrayList(new SkyObjectVisibilitySettings()));
        given(runtimeConfigurationRepository.count()).willReturn(noEntitiesExist());

        // when
        defaultDatabasePopulator.prepareDatabase();

        // then
        final InOrder inOrder = inOrder(userLocationRepository, skyObjectVisibilitySettingsRepository, runtimeConfigurationRepository);
        inOrder.verify(userLocationRepository).findAll();
        inOrder.verify(skyObjectVisibilitySettingsRepository).findAll();
        inOrder.verify(runtimeConfigurationRepository).saveAndFlush(any(RuntimeConfiguration.class));
    }

    private long someEntitiesExist() {
        return 1;
    }

    private long noEntitiesExist() {
        return 0;
    }
}
