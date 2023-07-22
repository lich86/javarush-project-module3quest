package com.chervonnaya.quest.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class Answer extends BaseEntity {
    private ChoiceType choiceType;
    private int nextQuestionId;
    private LoosingCause loosingCause;

    @Builder
    public Answer(int id, String text, ChoiceType choiceType, int nextQuestionId, LoosingCause loosingCause) {
        super(id, text);
        this.choiceType = choiceType;
        this.nextQuestionId = nextQuestionId;
        this.loosingCause = loosingCause;
        log.debug("Создан ответ id [{}]: text [{}], choiceType[{}], nextQuestionId[{}], loosingCause[{}]",
                id, text, choiceType, nextQuestionId, loosingCause);
    }



}
