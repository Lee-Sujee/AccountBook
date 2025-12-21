package com.ssafy.challenge.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ChallengeDepositEntity {

    private int id;                 // 저축 기록 ID
    private int challengeId;        // 소속 챌린지 ID
    private int amount;             // 저축 금액 (수정 가능)
    private LocalDate createdAt; // 저축 발생 시점 (불변, 로그)
}
