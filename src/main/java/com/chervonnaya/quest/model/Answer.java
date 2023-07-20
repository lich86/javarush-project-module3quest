package com.chervonnaya.quest.model;

import lombok.Builder;
import lombok.Getter;
@Getter
public class Answer extends BaseEntity {
    private ChoiceType choiceType;
    private int nextQuestionId;
    private LoosingCause loosingCause;

    @Builder
    public Answer(int id, String content, ChoiceType choiceType, int nextQuestionId, LoosingCause loosingCause) {
        super(id, content);
        this.choiceType = choiceType;
        this.nextQuestionId = nextQuestionId;
        this.loosingCause = loosingCause;
    }

}
