package com.burciaga.plutotv.softwarechallenge.utils.parser;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CuePointParserTest {

    CuePointParser parser = new CuePointParser();
    final String CUE_POINT_LINE = "00:01:01.819 --> 00:01:03.779 align:middle line:0";
    final String NON_CUE_POINT_LINE = "foobar";

    @Test
    public void isCuePoint() {
        Map<CuePointType, LocalTime> cuePoints = parser.getCuePoints(CUE_POINT_LINE);
        assertFalse(cuePoints.isEmpty());
        assertTrue(cuePoints.containsKey(CuePointType.START));
        assertTrue(cuePoints.containsKey(CuePointType.END));
    }

    @Test
    public void isNotCuePoint() {
        Map<CuePointType, LocalTime> cuePoints = parser.getCuePoints(NON_CUE_POINT_LINE);
        assertTrue(cuePoints.isEmpty());
    }

}