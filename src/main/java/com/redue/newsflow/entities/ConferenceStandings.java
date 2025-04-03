package com.redue.newsflow.entities;

import com.redue.newsflow.dto.BasketBallTeamStanding;
import lombok.Data;

import java.util.List;

@Data
public class ConferenceStandings {
    private List<BasketBallTeamStanding> westernConference;
    private List<BasketBallTeamStanding> easternConference;
}
