package com.chervonnaya.quest.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;

@Getter
public class Question extends BaseEntity{
    private final ArrayList<Answer> answers = new ArrayList<>();

    public Question(int id, String text, Answer... answers) {
        super(id, text);
        this.answers.addAll(Arrays.asList(answers));
    }

}
