package com.piotrglazar.nightowl.logic;

import com.google.common.collect.Lists;
import com.piotrglazar.nightowl.configuration.NightOwlRuntimeConfiguration;
import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.coordinates.Longitude;
import com.piotrglazar.nightowl.model.StarInfoRepository;
import com.piotrglazar.nightowl.model.UserLocationRepository;
import com.piotrglazar.nightowl.model.entities.UserLocation;
import com.piotrglazar.nightowl.util.TimeProvider;
import com.piotrglazar.nightowl.util.messages.DatabaseStatisticsMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseStatisticsTest {

    @Mock
    private TimeProvider timeProvider;

    @Mock
    private NightOwlRuntimeConfiguration nightOwlRuntimeConfiguration;

    @Mock
    private StarPositionProvider starPositionProvider;

    @Mock
    private UserLocationRepository userLocationRepository;

    @Mock
    private StarInfoRepository starInfoRepository;

    @InjectMocks
    private DatabaseStatistics databaseStatistics;

    @Test
    public void shouldCreateDatabaseStatisticsMessage() {
        // given
        given(starInfoRepository.count()).willReturn(42L);
        given(userLocationRepository.count()).willReturn(3L);
        given(nightOwlRuntimeConfiguration.getUserLocation()).willReturn(new UserLocation(new Latitude(8.0), new Longitude(16.0),
                "Warsaw"));
        given(starPositionProvider.getNumberOfStarsAlwaysVisible(any(UserLocation.class))).willReturn(5L);
        given(starPositionProvider.getNumberOfStarsNeverVisible(any(UserLocation.class))).willReturn(1L);
        given(starPositionProvider.getStarPositions(any(UserLocation.class), any(ZonedDateTime.class))).willReturn(Lists.newLinkedList());

        // when
        DatabaseStatisticsMessage databaseStatisticsMessage = databaseStatistics.getDatabaseStatisticsMessage();

        // then
        assertThat(databaseStatisticsMessage.getLocationLatitude()).isEqualTo(8.0);
        assertThat(databaseStatisticsMessage.getLocationLongitude()).isEqualTo(16.0);
        assertThat(databaseStatisticsMessage.getLocationName()).isEqualTo("Warsaw");
        assertThat(databaseStatisticsMessage.getNumberOfLocations()).isEqualTo(3L);
        assertThat(databaseStatisticsMessage.getNumberOfStars()).isEqualTo(42L);
        assertThat(databaseStatisticsMessage.getNumberOfStarsAlwaysVisible()).isEqualTo(5L);
        assertThat(databaseStatisticsMessage.getNumberOfStarsNeverVisible()).isEqualTo(1L);
        assertThat(databaseStatisticsMessage.getNumberOfStarsVisibleNow()).isEqualTo(0L);
    }
}
