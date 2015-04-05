package com.piotrglazar.nightowl.ui;

import com.piotrglazar.nightowl.util.messages.DatabaseStatisticsMessage;
import org.springframework.stereotype.Component;

@Component
public class MainMenuMessageFormatter {

    public String formatDatabaseStatisticsMessage(DatabaseStatisticsMessage databaseStatisticsMessage) {
        StringBuilder stringBuilder = new StringBuilder();

        addLine(stringBuilder, databaseStatisticsMessage.getLocationName(), "Current location");
        addLine(stringBuilder, Double.toString(databaseStatisticsMessage.getLocationLatitude()), "Latitude");
        addLine(stringBuilder, Double.toString(databaseStatisticsMessage.getLocationLongitude()), "Longitude");
        addLine(stringBuilder, Long.toString(databaseStatisticsMessage.getNumberOfLocations()), "Locations #");
        addLine(stringBuilder, Long.toString(databaseStatisticsMessage.getNumberOfStars()), "Stars #");
        addLine(stringBuilder, Long.toString(databaseStatisticsMessage.getNumberOfStarsAlwaysVisible()), "Stars always visible #");
        addLine(stringBuilder, Long.toString(databaseStatisticsMessage.getNumberOfStarsNeverVisible()), "Stars never visible #");
        addLine(stringBuilder, Long.toString(databaseStatisticsMessage.getNumberOfStarsVisibleNow()), "Stars visible now #");

        return stringBuilder.toString();
    }

    private void addLine(StringBuilder stringBuilder, String property, String message) {
        stringBuilder.append(message);
        stringBuilder.append(" : ");
        stringBuilder.append(property);
        stringBuilder.append("\n");
    }
}
