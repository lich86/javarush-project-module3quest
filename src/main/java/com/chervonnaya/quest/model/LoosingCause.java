package com.chervonnaya.quest.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class LoosingCause extends BaseEntity{
    public LoosingCause(int id, String text) {
        super(id, text);
    }
}
