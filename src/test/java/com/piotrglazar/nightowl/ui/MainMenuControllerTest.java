package com.piotrglazar.nightowl.ui;

import com.google.common.collect.Lists;
import com.piotrglazar.nightowl.api.MainWindow;
import com.piotrglazar.nightowl.configuration.NightOwlRuntimeConfiguration;
import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.coordinates.Longitude;
import com.piotrglazar.nightowl.model.SkyDisplayContext;
import com.piotrglazar.nightowl.model.UserLocationDto;
import com.piotrglazar.nightowl.model.UserLocationRepository;
import com.piotrglazar.nightowl.model.entities.SkyObjectVisibilitySettings;
import com.piotrglazar.nightowl.model.entities.UserLocation;
import com.piotrglazar.nightowl.util.ClockEvent;
import com.piotrglazar.nightowl.util.DoubleConverter;
import com.piotrglazar.nightowl.util.NightOwlEvent;
import com.piotrglazar.nightowl.util.StateReloadEvent;
import com.piotrglazar.nightowl.util.UiUpdateEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class MainMenuControllerTest {

    @Mock
    private MainWindow mainWindow;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private UserLocationRepository userLocationRepository;

    @Mock
    private NightOwlRuntimeConfiguration nightOwlRuntimeConfiguration;

    @Mock
    private DoubleConverter doubleConverter;

    @InjectMocks
    private MainMenuController mainMenuController;

    @Test
    public void shouldConvertUserLocationsToDto() {
        // given
        final UserLocation userLocation = userLocationWithNameLatitudeAndLongitude("Warsaw", 52.0, 21.0);
        given(userLocationRepository.findAll()).willReturn(Lists.newArrayList(userLocation));

        // when
        final List<UserLocationDto> allUserLocations = mainMenuController.getAllUserLocations();

        // then
        assertThat(allUserLocations).hasSize(1);
        final UserLocationDto userLocationDto = allUserLocations.get(0);
        assertThat(userLocationDto.getId()).isEqualTo(userLocation.getId());
        assertThat(userLocationDto.getLatitude()).isEqualTo(userLocation.getLatitude());
        assertThat(userLocationDto.getLongitude()).isEqualTo(userLocation.getLongitude());
        assertThat(userLocationDto.getName()).isEqualTo(userLocation.getName());
    }

    @Test
    public void shouldGetCurrentUserLocation() {
        // given
        final UserLocation userLocation = userLocationWithNameLatitudeAndLongitude("Warsaw", 52.0, 21.0);
        given(nightOwlRuntimeConfiguration.getUserLocation()).willReturn(userLocation);

        // when
        final UserLocationDto currentUserLocation = mainMenuController.getCurrentUserLocation();

        // then
        assertThat(currentUserLocation).isNotNull();
        verify(nightOwlRuntimeConfiguration).getUserLocation();
    }

    @Test
    public void shouldDoNothingWhenUserPressedCancel() {
        // given
        final UserLocationDto nullBecauseUserPressedCancel = null;

        // when
        mainMenuController.updateUserLocation(nullBecauseUserPressedCancel);

        // then
        verify(nightOwlRuntimeConfiguration, never()).updateUserLocation(any(UserLocation.class));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldFailWhenThereAreNoSuchUserLocation() {
        // given
        final UserLocationDto userLocationNotExistingInDb = arbitraryUserLocationDtoWithId(123);
        given(userLocationRepository.findOne(123L)).willReturn(null);

        // when
        mainMenuController.updateUserLocation(userLocationNotExistingInDb);
    }

    @Test
    public void shouldUpdateRuntimeConfiguration() {
        // given
        final UserLocation userLocation = arbitraryUserLocationWithId(123);
        final UserLocationDto userLocationDto = UserLocationDto.fromUserLocation(userLocation);
        given(userLocationRepository.findOne(123L)).willReturn(userLocation);

        // when
        mainMenuController.updateUserLocation(userLocationDto);

        // then
        verify(nightOwlRuntimeConfiguration).updateUserLocation(userLocation);
    }

    @Test
    public void shouldUpdateUserLocationAndPublishUiRepaintAndConfigurationReloadEvents() {
        final UserLocation userLocation = arbitraryUserLocationWithId(123);
        final UserLocationDto userLocationDto = UserLocationDto.fromUserLocation(userLocation);
        given(userLocationRepository.findOne(123L)).willReturn(userLocation);

        // when
        mainMenuController.updateUserLocation(userLocationDto);

        // then
        assertThatReloadsAppAndRepaintsUi();
    }

    @Test
    public void shouldAcceptNewVisibilitySettingsAndPublishUiRepaintAndConfigurationReloadEvents() {
        // given
        given(doubleConverter.twoDecimalPlaces(anyString())).willReturn(2.0);

        // when
        mainMenuController.updateStarVisibilityMagnitude("2.0");

        // then
        verify(nightOwlRuntimeConfiguration).updateStarVisibilityMagnitude(2.0);
        assertThatReloadsAppAndRepaintsUi();
    }

    @Test
    public void shouldNotUpdateApplicationStateWhenProvidedMagnitudeIsTooLow() {
        // given
        final double lowMagnitude = SkyObjectVisibilitySettings.MINIMAL_STAR_VISIBILITY_MAG - 1.0;
        given(doubleConverter.twoDecimalPlaces(anyString())).willReturn(lowMagnitude);

        // when
        mainMenuController.updateStarVisibilityMagnitude(Double.toString(lowMagnitude));

        // then
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void shouldNotUpdateApplicationStateWhenProvidedMagnitudeIsTooHigh() {
        // given
        final double highMagnitude = SkyObjectVisibilitySettings.MAXIMAL_STAR_VISIBILITY_MAG + 1.0;
        given(doubleConverter.twoDecimalPlaces(anyString())).willReturn(highMagnitude);

        // when
        mainMenuController.updateStarVisibilityMagnitude(Double.toString(highMagnitude));

        // then
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void shouldNotUpdateApplicationStateWhenProvidedMagnitudeIsNotANumber() {
        // given
        given(doubleConverter.twoDecimalPlaces(anyString())).willThrow(new NumberFormatException("test exception"));

        // when
        mainMenuController.updateStarVisibilityMagnitude("invalid number");

        // then
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    public void shouldFetchCurrentStarVisibilitySettings() {
        // given
        given(nightOwlRuntimeConfiguration.getStarVisibilityMagnitude()).willReturn(2.0);

        // when
        final double starVisibilityMagnitude = mainMenuController.currentStarVisibilityMagnitude();

        // then
        assertThat(starVisibilityMagnitude).isEqualTo(2.0);
    }

    @Test
    public void shouldFetchCurrentShowStarLabelSettings() {
        // given
        given(nightOwlRuntimeConfiguration.skyDisplayContext()).willReturn(new SkyDisplayContext(2.0, true));

        // when
        final boolean currentShowStarLabels = mainMenuController.currentShowStarLabels();

        // then
        assertThat(currentShowStarLabels).isTrue();
    }

    @Test
    public void shouldUpdateApplicationStateWhenShowStarLabelsChanged() {
        // when
        mainMenuController.updateStarLabelsVisibility(true);

        // then
        verify(nightOwlRuntimeConfiguration).updateShowStarLabels(true);
        assertThatReloadsAppAndRepaintsUi();
    }

    @Test
    public void shouldSendClockChangeEvent() {
        // when
        mainMenuController.updateClockRunningStatus(true);

        // then
        verify(eventPublisher).publishEvent(any(ClockEvent.class));
    }

    private void assertThatReloadsAppAndRepaintsUi() {
        final ArgumentCaptor<NightOwlEvent> eventsCaptor = ArgumentCaptor.forClass(NightOwlEvent.class);
        verify(eventPublisher, times(2)).publishEvent(eventsCaptor.capture());
        final List<NightOwlEvent> events = eventsCaptor.getAllValues();
        assertThat(events).hasSize(2);
        containsReloadEvent(events);
        containsMainWindowRepaintEvent(events);
    }

    private void containsMainWindowRepaintEvent(final List<NightOwlEvent> events) {
        final Optional<NightOwlEvent> event = events.stream().filter(e -> e.getClass().equals(UiUpdateEvent.class)).findFirst();
        assertThat(event.isPresent());
        ((UiUpdateEvent) event.get()).action(mainWindow);
        verify(mainWindow).repaintUi();
    }

    private void containsReloadEvent(final List<NightOwlEvent> events) {
        assertThat(events.stream().filter(e -> e.getClass().equals(StateReloadEvent.class)).count()).isEqualTo(1);
    }

    private UserLocation arbitraryUserLocationWithId(final int id) {
        final UserLocation userLocation = userLocationWithNameLatitudeAndLongitude("warsaw", 0.0, 0.0);
        userLocation.setId((long) id);

        return userLocation;
    }

    private UserLocationDto arbitraryUserLocationDtoWithId(final int id) {
        return UserLocationDto.fromUserLocation(arbitraryUserLocationWithId(id));
    }

    private UserLocation userLocationWithNameLatitudeAndLongitude(final String name, final double latitude, final double longitude) {
        return new UserLocation(new Latitude(latitude), new Longitude(longitude), name);
    }
}
