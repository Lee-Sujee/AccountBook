package com.ssafy.challenge.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ChallengeDepositEntity {

    private int id;               
    private int challengeId;      
    private int amount;           
    private LocalDate createdAt; 
}
