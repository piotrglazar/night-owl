package com.piotrglazar.nightowl.logic;

import com.piotrglazar.nightowl.converters.LatitudeConverter;
import com.piotrglazar.nightowl.converters.LocalTimeConverter;
import com.piotrglazar.nightowl.converters.StarInfoConverter;
import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.model.entities.StarCelestialPosition;
import com.piotrglazar.nightowl.model.entities.StarInfo;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.converters.ConvertParam;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@RunWith(JUnitParamsRunner.class)
public class StarPositionCalculatorTest {

    private StarPositionCalculator starPositionCalculator = new StarPositionCalculator();

    @Test(expected = IllegalArgumentException.class)
    @Parameters({"-90", "0", "90"})
    public void shouldThrowExceptionForUnsupportedLatitude(double latitude) {
        // expect
        starPositionCalculator.calculateBoundaryDeclination(latitude);
    }

    @Test
    @Parameters({
            "52.0 | 38.0",
            "23.5 | 66.5",
            "-35.2 | -54.8",
            "-52.0 | -38.0"
    })
    public void shouldCalculateDeclinationComplementaryToPole(double latitude, double expectedDeclination) {
        // when
        final double declination = starPositionCalculator.calculateBoundaryDeclination(latitude);

        // then
        assertThat(declination).isEqualTo(expectedDeclination);
    }

    @Test
    @Parameters({
            "00:00:00 | 0.0",
            "12:00:00 | 3.1415",
            "18:00:00 | 4.7123",
            "06:00:00 | 1.5707"
    })
    public void shouldConvertHourAngleToRadians(@ConvertParam(LocalTimeConverter.class) LocalTime hourAngle, double expectedAngle) {
        // when
        final double angle = starPositionCalculator.hourAngleAsArc(hourAngle);

        // then
        assertThat(angle).isEqualTo(expectedAngle, within(0.0001));
    }

    @Test
    @Parameters({
            "0.4014 | 1.0472 | 0.7679 | 0.8374", // 23 deg, 60 deg, 44 deg, 47.98 deg
            "0.9288 | 0.6138 | 0.7720 | 0.6231"  // 53.2167 deg, 35.16 deg, 44.25 deg, 35.7 deg
    })
    public void shouldCalculateZenithDistance(double latitude, double declination, double t, double expectedZenithDistance) {
        // when
        final double zenithDistance = starPositionCalculator.calculateZenithDistance(latitude, declination, t);

        // then
        assertThat(zenithDistance).isEqualTo(expectedZenithDistance, within(0.0001));
    }

    @Test
    @Parameters({
            "1.0472 | 0.7679 | 0.8374 | 0.9288 | 5.0279", // 60 deg, 44 deg, 47.98 deg, 53.24 deg, 288 deg
            "0.6138 | 0.7720 | 0.6230 | 0.9288 | 4.4978",   // 35.16 deg, 44.25 deg, 35.7 deg, 53.24 deg, 257.7 deg
            "0.6138 | 0.0 | 0.3150 | 0.9288 | 3.1415",   // 35.16 deg, 0 deg, 18.05 deg, 53.24 deg, 180.0 deg
            "0.6138 | 5.511 | 0.6230 | 0.9288 | 1.7852"   // 35.16 deg, 315.75 deg, 35.7 deg, 53.24 deg, 102.3 deg
    })
    public void shouldCalculateAzimuth(double declination, double t, double zenithDistance, double latitude, double expectedAzimuth) {
        // when
        final double azimuth = starPositionCalculator.calculateAzimuth(zenithDistance, declination, t, latitude);

        // then
        assertThat(azimuth).isEqualTo(expectedAzimuth, within(0.0001));
    }

    @Test
    @Parameters({
            "52.0 | 10:08:22;11.97 | 11:56 | 45.533 | 218.343",     // regulus at 11:56 sidereal time (16:57 13.07.2014 Warsaw)
            "52.0 | 06:45:08;-16.717 | 11:56 | 95.81 | 250.16",     // sirius at 11:56 sidereal time (16:57 13.07.2014 Warsaw)
            "52.0 | 18:36:56;38.78 | 03:14 | 79.06 | 37.931",       // wega at 3:14 sidereal time (8:20 14.07.2014 Warsaw)
            "52.0 | 19:50:47;8.52 | 15:50 | 65.205 | 109.039",      // altair at 15:50 sidereal time (21:04 14.07.2014 Warsaw)
            "52.0 | 14:15:42;19.11 | 15:50 | 37.706 | 218.163",     // arktur at 15:50 sidereal time (21:04 14.07.2014 Warsaw)
            "52.0 | 14:50:42;74.09 | 15:50 | 22.931 | 349.629",     // kochab at 15:50 sidereal time (21:04 14.07.2014 Warsaw)
            "52.0 | 21:18:36;62.55 | 15:50 | 42.434 | 42.593",      // alderamin at 15:50 sidereal time (21:04 14.07.2014 Warsaw)
            "52.0 | 20:41:26;45.267 | 16:26 | 41.348 | 73.013",     // deneb at 16:26 sidereal time (21:32 14.07.2014 Warsaw)
            "-34.0 | 14:39:35;-60.833 | 03:00 | 85.074 | 177.506",  // alfa centauri at 3:00 sidereal time (17.07.2014 Sydney)
            "-34.0 | 22:57:39;-29.62 | 03:00 | 50.927 | 102.726",   // fomalhaut at 3:00 sidereal time (17.07.2014 Sydney)
            "-34.0 | 06:23:57;-52.696 | 03:00 | 40.441 | 133.451",  // canopus at 3:00 sidereal time (17.07.2014 Sydney)
            // bug fix
            "52.0 | 22:57:38;-29.62 | 21:10 | 84.963 | 156.737"     // fomalhaut at 21:10 sidereal time (05.10.2014 Warsaw)
    })
    public void shouldCalculateCelestialPosition(@ConvertParam(LatitudeConverter.class) Latitude latitude,
                                                 @ConvertParam(StarInfoConverter.class) StarInfo starInfo,
                                                 @ConvertParam(LocalTimeConverter.class) LocalTime sideRealTime,
                                                 double expectedZenithDistance, double expectedAzimuth) {
        // when
        final StarCelestialPosition starCelestialPosition =
                starPositionCalculator.calculateCelestialPosition(latitude, sideRealTime, starInfo);

        // then
        StarCelestialPositionAssert.assertThat(starCelestialPosition)
                .hasZenithDistance(expectedZenithDistance)
                .hasAzimuth(expectedAzimuth);
    }
}
