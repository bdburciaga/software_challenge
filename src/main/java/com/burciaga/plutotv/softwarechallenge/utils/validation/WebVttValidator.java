package com.burciaga.plutotv.softwarechallenge.utils.validation;

import com.burciaga.plutotv.softwarechallenge.model.CuePoint;
import com.burciaga.plutotv.softwarechallenge.utils.parser.CuePointParser;
import com.burciaga.plutotv.softwarechallenge.utils.parser.CuePointType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class WebVttValidator implements Validator {

    private final String[] PATTERNS = { "HH:mm:ss.SSS", "HH:mm:ss" };
    private final String CUEPOINT_DELIMITER = "-->";
    private final Pattern CUEPOINT_TIME_PATTERN = Pattern.compile("(\\d+:)?[0-5]\\d:[0-5]\\d\\.\\d{3}");
    private CuePointParser parser;

    public WebVttValidator(CuePointParser parser) {
        this.parser = parser;
    }

    Logger logger = LoggerFactory.getLogger(WebVttValidator.class);

    /**
     * Attempt to validate the input as webvtt.
     * @param webVtt The webvtt input.
     * @return Associated ValidationResult object.
     */
    @Override
    public ValidationResult validate(String webVtt) {
        ValidationResult validationResult = new ValidationResult();
        validationResult.setValid(true);
        StringBuilder messageBuilder;

        List<String> list = webVtt.lines().collect(Collectors.toList());

        boolean isCueStart = false;

        try {
            // Iterate through lines, as opposed to streaming, so invalid
            // line numbers can be returned to client for correction.
            for (int i = 0; i < list.size(); i++) {
                messageBuilder = new StringBuilder();
                String cuePointLine = list.get(i);

                // If current line is blank, following line must be
                // a start of the cuepoint.
                if (cuePointLine.isBlank() && !isCueStart) {
                    isCueStart = true;
                } else if (isCueStart == true) {

                    // Parse start and end cuepoints.
                    if (!cuePointLine.contains(CUEPOINT_DELIMITER)) {
                        messageBuilder.append("Line ");
                        messageBuilder.append(i + 1);
                        messageBuilder.append(": Should contain start and end times, but does not.");
                        validationResult.addSuggestion(messageBuilder.toString());

                        if (cuePointLine.isBlank()) {
                            isCueStart = true;
                        } else {
                            isCueStart = false;
                        }
                    } else {
                        try {
                            if((i + 1) == 198) {
                                boolean test = true;
                            }
                            Map<CuePointType, LocalTime> cuePointMap = parser.getCuePoints(cuePointLine);

                            // Cuepoints must not be equal, and start must be before end.
                            if (cuePointMap.get(CuePointType.START).compareTo(cuePointMap.get(CuePointType.END)) >= 0) {
                                validationResult.setValid(false);
                                messageBuilder.append("Line ");
                                messageBuilder.append(i + 1);
                                messageBuilder.append(": Cue start (");
                                messageBuilder.append(cuePointMap.get(CuePointType.START).toString());
                                messageBuilder.append(") must occur before cue end: (");
                                messageBuilder.append(cuePointMap.get(CuePointType.END).toString());
                                messageBuilder.append(")");
                                validationResult.addSuggestion(messageBuilder.toString());
                            }

                            isCueStart = false;
                        } catch (Exception ex) {

                            messageBuilder.append("Line ");
                            messageBuilder.append(i + 1);
                            messageBuilder.append(": Invalid start and endtime format.");
                            validationResult.addSuggestion(messageBuilder.toString());
                            logger.error(messageBuilder.toString());
                            logger.error(cuePointLine);
                            isCueStart = false;
                        }
                    }
                }
            }
        } catch(IllegalArgumentException ex) {
            validationResult.setMessage("Illegal Argument: " + ex.getMessage());
        }

        if (!validationResult.isValid()) {
            validationResult.setMessage("Validation issues discovered.");
        }
        logger.debug("Valid input: " + validationResult.isValid());
        logger.debug(validationResult.getMessage());

        return validationResult;
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
                logger.error("we got here: " + timeString);
                logger.debug(ex.getMessage());
            }
        }
        if (localTime == null) {
            String errorString = "Unable to parse cuepoint time from: " + timeString;
            logger.info(errorString);
            throw new IllegalArgumentException(errorString);
        }

        return localTime;
    }
}
