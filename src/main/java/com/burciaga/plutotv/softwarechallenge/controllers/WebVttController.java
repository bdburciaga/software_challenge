package com.burciaga.plutotv.softwarechallenge.controllers;

import com.burciaga.plutotv.softwarechallenge.edit.EditRequest;
import com.burciaga.plutotv.softwarechallenge.edit.EditResult;
import com.burciaga.plutotv.softwarechallenge.model.CuePoint;
import com.burciaga.plutotv.softwarechallenge.utils.parser.CuePointParser;
import com.burciaga.plutotv.softwarechallenge.utils.parser.CuePointType;
import com.burciaga.plutotv.softwarechallenge.utils.validation.ValidationResult;
import com.burciaga.plutotv.softwarechallenge.utils.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/webvtt")
public class WebVttController {

    @Autowired
    Validator validator;

    @Autowired
    CuePointParser parser;

    Logger logger = LoggerFactory.getLogger(WebVttController.class);

    public WebVttController(Validator validator, CuePointParser parser) {
        this.validator = validator;
        this.parser = parser;
    }

    /**
     * Validate the supplied webvtt file.
     * @param body The webvtt contents.
     * @return The validation result, including validation errors.
     */
    @PostMapping(value = "/validate", produces = { MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ValidationResult> validateWebVtt(@RequestBody String body) {
        ValidationResult validationResult = validator.validate(body);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(validationResult);
    }

    /**
     * Edit the supplied webvtt file by providing cuepoints that should be modified.
     * @param editRequest The edit request, containing webvtt contents and cue points for modification.
     * @return The modified webvtt, including validation results.
     */
    @PutMapping(value = "/edit", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<EditResult> editWebVtt(@RequestBody EditRequest editRequest) {
        EditResult editResult = new EditResult();
        StringBuilder messageBuilder = new StringBuilder();

        List<String> lines = editRequest.getWebVtt().lines().collect(Collectors.toList());

        if (editRequest.getCuePoints() != null) {

            for (CuePoint cp : editRequest.getCuePoints()) {
                int lineNumber = cp.getLineNumber();
                String cuePointLine = "";

                try {
                    cuePointLine = lines.get(lineNumber - 1);
                    Map<CuePointType, LocalTime> cuePointMap = parser.getCuePoints(cuePointLine);

                    // Only replace the cuepoint we care about, in the event of a duplicate.
                    if (cuePointLine.contains("->")) {
                        String[] splitArray = cuePointLine.split("->");
                        splitArray[0] = splitArray[0].replaceFirst(cuePointMap.get(CuePointType.START).toString(), cp.getStartTime());
                        splitArray[1] = splitArray[1].replace(cuePointMap.get(CuePointType.END).toString(), cp.getEndTime());
                        cuePointLine = String.join("->", splitArray);

                        lines.set(lineNumber - 1, cuePointLine);
                    } else {
                        messageBuilder.append("Line ");
                        messageBuilder.append(lineNumber);
                        messageBuilder.append(" is not a cuepoint line.\n");
                    }
                }
                catch(IndexOutOfBoundsException ex) {
                    messageBuilder.append("No line numbered ");
                    messageBuilder.append(lineNumber);
                    messageBuilder.append(".  ");
                    logger.info(messageBuilder.toString());
                }
                catch(Exception ex) {
                    messageBuilder.append("Unable to replace cue points in line ");
                    messageBuilder.append(lineNumber);
                    messageBuilder.append(": ");
                    messageBuilder.append(cuePointLine);
                    messageBuilder.append("\"");
                    logger.debug(messageBuilder.toString());
                }
            }

        }
        editResult.setMessage(messageBuilder.toString());
        editResult.setUpdatedContent(String.join("\n", lines));

        // Validate the modified webvtt for the client.
        editResult.setValidationResult(validator.validate(editResult.getUpdatedContent()));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(editResult);
    }
}
