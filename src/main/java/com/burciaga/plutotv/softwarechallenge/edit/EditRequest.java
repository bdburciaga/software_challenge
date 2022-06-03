package com.burciaga.plutotv.softwarechallenge.edit;

import com.burciaga.plutotv.softwarechallenge.model.CuePoint;

import java.util.ArrayList;
import java.util.List;

public class EditRequest {
    private String webVtt;
    private List<CuePoint> cuePoints = new ArrayList<>();

    public String getWebVtt() {
        return webVtt;
    }

    public void setWebVtt(String webVtt) {
        this.webVtt = webVtt;
    }

    public List<CuePoint> getCuePoints() {
        return cuePoints;
    }

    public void setCuePoints(List<CuePoint> cuePoints) {
        this.cuePoints = cuePoints;
    }
}
