package com.redue.newsflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FootballTeamStanding {
    
    private String teamName;
    
    private int position;
    
    private int gamesPlayed;
    
    private int win;
    
    private int draws;
    
    private int loss;
    
    private int goalDifference;
    
    private int points;
}
