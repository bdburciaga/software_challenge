package com.burciaga.plutotv.softwarechallenge.utils.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CuePointParser {

    private final Pattern CUEPOINT_TIME_PATTERN = Pattern.compile("(\\d+:)?[0-5]\\d:[0-5]\\d(.\\d{3})?");
    private final String[] PATTERNS = { "HH:mm:ss.SSS", "HH:mm:ss" };
    Logger logger = LoggerFactory.getLogger(CuePointParser.class);

    /**
     * Get start and end cue points from a webvtt line.
     * @param line The line from the webvtt contents.
     * @return Associated start and end cuepoints.
     */
    public Map<CuePointType, LocalTime> getCuePoints(String line) {
        StringBuilder messageBuilder = new StringBuilder();
        Map<CuePointType, LocalTime> cuepoints = new HashMap<>();

        Matcher matcher = CUEPOINT_TIME_PATTERN.matcher(line);

        LocalTime cuePointStart;
        LocalTime cuePointEnd;

        try {
            if (matcher.find()) {
                cuePointStart = getLocalTime(matcher.group());
                cuepoints.put(CuePointType.START, cuePointStart);
            }
            if (matcher.find()) {
                cuePointEnd = getLocalTime(matcher.group());
                cuepoints.put(CuePointType.END, cuePointEnd);
            }
        } catch(Exception ex) {
            messageBuilder.append("Unable to find cue points in line: ");
            messageBuilder.append(line);
            logger.info(messageBuilder.toString());
            throw ex;
        }
        return cuepoints;
    }

    /**
     * Get a LocalTime representation of the cueblock timestamp.
     * @param timeString The cuepoint timestamp.
     * @return LocalTime representation.
     */
    private LocalTime getLocalTime(String timeString) throws IllegalArgumentException {
        LocalTime localTime = null;

        // Attempt to parse cue time, with or without millseconds included.
        for (String pattern : PATTERNS) {
            try {
                localTime = LocalTime.parse(timeString, DateTimeFormatter.ofPattern(pattern));
                break;
            } catch(Exception ex) {
                logger.debug(ex.getMessage());
            }
        }
        if (localTime == null) {
            String errorString = "Unable to parse cuepoint time from: " + timeString;
            logger.debug(errorString);
            throw new IllegalArgumentException(errorString);
        }

        return localTime;
    }
}
