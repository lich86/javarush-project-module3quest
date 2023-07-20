package com.chervonnaya.quest.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
@Slf4j
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class Question extends BaseEntity{
    private final ArrayList<Answer> answers = new ArrayList<>();

    public Question(int id, String text, Answer... answers) {
        super(id, text);
        this.answers.addAll(Arrays.asList(answers));
        log.debug("Создан вопрос id [{}]: text [{}], answers[{}]", id, text, answers);
    }

}
