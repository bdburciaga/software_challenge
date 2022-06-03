# Software Challenge

This application will be used to validate and modify webtvv files, with both operations available through an api endpoint.

## Architectural Considerations
For this project, I utilized a Spring Boot microservice to build out the RESTful API.  The reason for this decision was 
due to ease of creation, and Java being utilized by PlutoTV (based on my understanding).  The project structure follows 
the standard Spring Boot pattern, involving controllers.  The item that is intentionally omitted from this application,
due to defined requirements, is the persistence layer.  There are thoughts I have on this challenge related to
the requirements put forth, as well as an alternate approach in favor of a serverless design, that I would like to 
discuss when appropriate.

## Installation
The application can be executed by first performing a maven package command:
```shell script
mvn package
```

Once complete, the application can be executed with the following command: 
```shell script
java -jar target/software-challenge-0.0.1-SNAPSHOT.jar
```

## Documentation
All api endpoints are documented using swagger, 
which can be accessed from a running application at http://localhost:8080/swagger-ui.html.

##Usage
There are two api methods available to a client of the service: /validate and /edit.

### /validate
The validate method is used to validate cue blocks within a provided webvtt file.  The validate method determines validation based on the following rules:
* All cue blocks are separated by a single blank line.
* All cue blocks start with a line containing a start and end cue point time.  
* Each cuepoint should adhere to the pattern HH:mm:ss.SSS.
* The end cuepoint should occur after the start cuepoint.

#### Example Payload
```
WEBVTT
 
 00:00:05.429 --> 00:00:07.681 align:middle line:0
 ♪ ♪
 
 00:00:31.455 --> 00:00:33.457 align:middle line:0
 ♪ ♪
 
 00:00:51.224 --> 00:00:53.226 align:middle line:0
 (tires screech)
```

Upon validation, a response is returned that indicates if the webvtt is valid.  In the case that the webvtt is invalid, a list of reasons for invalidity are returned to the client.

#### Example Response
```json
{
    "message": "Validation issues discovered.",
    "suggestions": [
        "Line 12: Cue start (00:00:53.226) must occur before cue end: (00:00:53.226)",
        "Line 39: Cue start (00:01:17.501) must occur before cue end: (00:01:17.501)",
        "Line 98: Cue start (00:01:53.871) must occur before cue end: (00:01:53.871)",
        "Line 153: Cue start (00:02:32.158) must occur before cue end: (00:02:32.158)",
        "Line 177: Cue start (00:02:51.219) must occur before cue end: (00:02:51.219)",
        "Line 198: Cue start (00:08:12.123) must occur before cue end: (00:03:04.358)"
    ],
    "valid": false
}
```

### /edit
The edit method is used to modify a supplied webvtt file with a set of provided cue points, along with their associated line identifiers.  If the supplied data corresponds to a line containing cue points, those cue points will be altered.
After modification is executed, validation is performed to return appropriate information to the client prior to utilization of the modified webvtt.

### Example Payload
```json
{
    "webVtt": "<webvtt_string>",
    "cuePoints": [
        {
            "lineNumber": 3,
            "startTime": "11:22:33.444",
            "endTime": "18:33:44.876"
        }
    ]
}
```

The response to the client will contain the modified webvtt, as well as the associated validation result.

### Example Response
```json
{
    "validationResult": {
        "message": "",
        "suggestions": [],
        "valid": true
    },
    "updatedContent": "<modified_webvtt>",
    "message": ""
}
```