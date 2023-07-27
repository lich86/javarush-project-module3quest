package com.chervonnaya.quest.service;

import com.chervonnaya.quest.model.Answer;

public interface QuestService {

    void getAllCounters();
    String getRedirectUrl(Answer answer);
    String handlePlayerChoice();
}
