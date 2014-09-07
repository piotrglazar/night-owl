package com.piotrglazar.nightowl.ui;

import com.google.common.collect.Lists;
import com.piotrglazar.nightowl.configuration.NightOwlRuntimeConfiguration;
import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.coordinates.Longitude;
import com.piotrglazar.nightowl.model.UserLocation;
import com.piotrglazar.nightowl.model.UserLocationDto;
import com.piotrglazar.nightowl.model.UserLocationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MainMenuControllerTest {

    private ObjectFactory<UserLocationRepository> userLocationRepositoryObjectFactory;

    private ObjectFactory<NightOwlRuntimeConfiguration> nightOwlRuntimeConfigurationObjectFactory;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private UserLocationRepository userLocationRepository;

    @Mock
    private NightOwlRuntimeConfiguration nightOwlRuntimeConfiguration;

    private MainMenuController mainMenuController;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        userLocationRepositoryObjectFactory = mock(ObjectFactory.class);
        nightOwlRuntimeConfigurationObjectFactory = mock(ObjectFactory.class);

        given(userLocationRepositoryObjectFactory.getObject()).willReturn(userLocationRepository);
        given(nightOwlRuntimeConfigurationObjectFactory.getObject()).willReturn(nightOwlRuntimeConfiguration);

        mainMenuController = new MainMenuController(userLocationRepositoryObjectFactory, nightOwlRuntimeConfigurationObjectFactory,
                eventPublisher);
    }

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

    @Test
    public void shouldFailWhenThereAreNoSuchUserLocation() {
        // given
        final int id = 1;
        final UserLocationDto userLocationNotExistingInDb = arbitraryUserLocationDtoWithId(id);
        given(userLocationRepository.findOne((long) id)).willReturn(null);

        // when
        catchException(mainMenuController).updateUserLocation(userLocationNotExistingInDb);

        // then
        assertThat((Exception) caughtException()).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void shouldUpdateRuntimeConfiguration() {
        // given
        final int id = 1;
        final UserLocation userLocation = arbitraryUserLocationWithId(id);
        final UserLocationDto userLocationDto = UserLocationDto.fromUserLocation(userLocation);
        given(userLocationRepository.findOne(userLocation.getId())).willReturn(userLocation);

        // when
        mainMenuController.updateUserLocation(userLocationDto);

        // then
        verify(nightOwlRuntimeConfiguration).updateUserLocation(userLocation);
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
