package com.burciaga.plutotv.softwarechallenge.utils.validation;

import com.burciaga.plutotv.softwarechallenge.utils.parser.CuePointParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebVttValidatorTest {

    private final String WEBVTT_CONTENTS = "WEBVTT\n" +
            "\n" +
            "00:00:05.429 --> 00:00:07.681 align:middle line:0\n" +
            "♪ ♪\n" +
            "\n" +
            "00:00:31.455 --> 00:00:33.457 align:middle line:0\n" +
            "♪ ♪\n" +
            "\n" +
            "00:00:51.224 --> 00:00:53.226 align:middle line:0\n" +
            "(tires screech)\n" +
            "\n" +
            "00:00:53.226 --> 00:00:53.226 align:middle line:0\n" +
            "-(cat meows)\n" +
            "-Todd, Ollie...\n" +
            "\n" +
            "00:00:58.148 --> 00:01:01.819 align:middle line:0\n" +
            "Take a look\n" +
            "at our temporary summer home.\n" +
            "\n" +
            "00:01:01.819 --> 00:01:03.779 align:middle line:0\n" +
            "The Harper House.\n" +
            "\n" +
            "00:01:03.779 --> 00:01:05.280 align:middle line:0\n" +
            "Kind of...\n" +
            "\n" +
            "00:01:05.280 --> 00:01:06.907 align:middle line:0\n" +
            "okay, right?\n" +
            "\n" +
            "00:01:06.907 --> 00:01:09.493 align:middle line:0\n" +
            "Oh, my God.\n" +
            "\n" +
            "00:01:11.453 --> 00:01:15.248 align:middle line:0\n" +
            "We really own this freaking\n" +
            "incredibly awesome old place?\n" +
            "\n" +
            "00:01:15.248 --> 00:01:17.501 align:middle line:0\n" +
            "Why'd we never come here?\n" +
            "\n" +
            "00:01:17.501 --> 00:01:17.501 align:middle line:0\n" +
            "Well, the last time I was here,\n" +
            "kids, I was your age.\n" +
            "\n" +
            "00:01:20.004 --> 00:01:22.214 align:middle line:0\n" +
            "Oh, yeah, it was a big surprise\n" +
            "when Aunt Maggie died\n" +
            "\n" +
            "00:01:22.214 --> 00:01:25.133 align:middle line:0\n" +
            "a few months ago,\n" +
            "and left this house to me.\n" +
            "\n" +
            "00:01:25.133 --> 00:01:28.136 align:middle line:0\n" +
            "Plus, it's on the poor north\n" +
            "side of town, Ollie.\n" +
            "\n" +
            "00:01:28.136 --> 00:01:29.388 align:middle line:0\n" +
            "It's old. It's scary.\n" +
            "\n" +
            "00:01:29.388 --> 00:01:30.806 align:middle line:0\n" +
            "It's urban.\n" +
            "\n" +
            "00:01:30.806 --> 00:01:32.516 align:middle line:0\n" +
            "It is temporary!\n" +
            "\n" +
            "00:01:32.516 --> 00:01:34.184 align:middle line:0\n" +
            "Ha! Like I said.\n" +
            "\n" +
            "00:01:34.184 --> 00:01:37.897 align:middle line:0\n" +
            "Mom, our real home over\n" +
            "on the good south side of town,\n" +
            "\n" +
            "00:01:37.897 --> 00:01:40.273 align:middle line:0\n" +
            "remember that?\n" +
            "\n" +
            "00:01:40.273 --> 00:01:41.107 align:middle line:0\n" +
            "Yeah, it's right down the street\n" +
            "from Grandpa's house.\n" +
            "\n" +
            "00:01:41.107 --> 00:01:43.151 align:middle line:0\n" +
            "So, I think we just stay there\n" +
            "\n" +
            "00:01:43.151 --> 00:01:45.320 align:middle line:0\n" +
            "while our real house\n" +
            "gets remodeled.\n" +
            "\n" +
            "00:01:45.320 --> 00:01:47.990 align:middle line:0\n" +
            "Uh, um... Oh.\n" +
            "\n" +
            "00:01:47.990 --> 00:01:49.950 align:middle line:0\n" +
            "You know gated community rules.\n" +
            "\n" +
            "00:01:49.950 --> 00:01:51.952 align:middle line:0\n" +
            "They don't allow too many people\n" +
            "living in one house\n" +
            "\n" +
            "00:01:51.952 --> 00:01:53.871 align:middle line:0\n" +
            "for a long time. So...\n" +
            "\n" +
            "00:01:53.871 --> 00:01:53.871 align:middle line:0\n" +
            "Right, Freddie?\n" +
            "\n" +
            "00:01:58.208 --> 00:02:00.126 align:middle line:0\n" +
            "Oh, yeah. Totally.\n" +
            "Re... remodeling.\n" +
            "\n" +
            "00:02:00.126 --> 00:02:01.461 align:middle line:0\n" +
            "Yeah, yeah, Debbie.\n" +
            "\n" +
            "00:02:01.461 --> 00:02:04.798 align:middle line:0\n" +
            "So, uh, just think of this\n" +
            "like camping, kids.\n" +
            "\n" +
            "00:02:04.798 --> 00:02:07.927 align:middle line:0\n" +
            "Camping sucked enough when\n" +
            "we had to shit against a tree.\n" +
            "\n" +
            "00:02:07.927 --> 00:02:10.136 align:middle line:0\n" +
            "Now we got to shit\n" +
            "in an Addams Family set.\n" +
            "\n" +
            "00:02:10.136 --> 00:02:11.597 align:middle line:0\n" +
            "No, Todd.\n" +
            "\n" +
            "00:02:11.597 --> 00:02:13.473 align:middle line:0\n" +
            "We get to.\n" +
            "\n" +
            "00:02:13.473 --> 00:02:15.893 align:middle line:0\n" +
            "We get to carry these boxes.\n" +
            "\n" +
            "00:02:15.893 --> 00:02:18.437 align:middle line:0\n" +
            "And we get to shit\n" +
            "in this house.\n" +
            "\n" +
            "00:02:18.437 --> 00:02:20.564 align:middle line:0\n" +
            "Hey, watch the language, kids.\n" +
            "(chuckles)\n" +
            "\n" +
            "00:02:20.564 --> 00:02:22.983 align:middle line:0\n" +
            "(grunting) Is this the one?\n" +
            "\n" +
            "00:02:22.983 --> 00:02:26.403 align:middle line:0\n" +
            "(gasps) Hi! How you doing?\n" +
            "\n" +
            "00:02:26.403 --> 00:02:27.988 align:middle line:0\n" +
            "Here we go.\n" +
            "\n" +
            "00:02:27.988 --> 00:02:29.573 align:middle line:0\n" +
            "Oh, no, no, no.\n" +
            "\n" +
            "00:02:29.573 --> 00:02:32.158 line:-2\n" +
            "Hey, let's try to seem cool\n" +
            "with being here, okay?\n" +
            "\n" +
            "00:02:32.158 --> 00:02:32.158 line:-1\n" +
            "For the neighbors' sake.\n" +
            "\n" +
            "00:02:33.535 --> 00:02:35.121 line:-1\n" +
            "Ugh. Knickknacks.\n" +
            "\n" +
            "00:02:37.372 --> 00:02:40.500 line:-2\n" +
            "Ugh. Man,\n" +
            "I thought someone cleared out\n" +
            "\n" +
            "00:02:40.500 --> 00:02:42.920 align:start line:-1\n" +
            "Aunt Maggie's sad old lady junk.\n" +
            "\n" +
            "00:02:42.920 --> 00:02:46.132 align:middle line:0\n" +
            "Eh, it's just a little dusty.\n" +
            "\n" +
            "00:02:46.132 --> 00:02:48.216 align:middle line:0\n" +
            "You know,\n" +
            "dust is 90% dead skin.\n" +
            "\n" +
            "00:02:48.216 --> 00:02:51.219 align:middle line:0\n" +
            "So, people, like,\n" +
            "never really go away.\n" +
            "\n" +
            "00:02:51.219 --> 00:02:51.219 align:middle line:0\n" +
            "Sweet.\n" +
            "\n" +
            "00:02:54.556 --> 00:02:57.309 line:-2\n" +
            "This slide from upper-middle\n" +
            "class to lower-middle class\n" +
            "\n" +
            "00:02:57.309 --> 00:02:59.019 line:-1\n" +
            "is a pain in the ass.\n" +
            "\n" +
            "00:02:59.019 --> 00:03:01.396 line:-2\n" +
            "Come on, give it a shot,\n" +
            "Todd. Please?\n" +
            "\n" +
            "00:03:01.396 --> 00:03:03.231 line:-2\n" +
            "Well, at least this weekend\n" +
            "we'll be back\n" +
            "\n" +
            "00:03:03.231 --> 00:03:04.357 line:-1\n" +
            "at the good old country club\n" +
            "\n" +
            "00:03:04.358 --> 00:03:04.358 line:-2\n" +
            "with other\n" +
            "moderately wealthy people";
    CuePointParser parser = new CuePointParser();
    Validator validator = new WebVttValidator(parser);


    @Test
    void validate() {
        validator.validate(WEBVTT_CONTENTS);
    }
}