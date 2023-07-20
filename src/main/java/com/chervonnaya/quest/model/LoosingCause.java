package com.chervonnaya.quest.model;

import lombok.Getter;

@Getter
public class LoosingCause extends BaseEntity{
    public LoosingCause(int id, String text) {
        super(id, text);
    }
}
