package com.chervonnaya.quest.repository;

import com.chervonnaya.quest.model.Answer;
import com.chervonnaya.quest.model.ChoiceType;
import com.chervonnaya.quest.model.LoosingCause;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
@ToString
public class AnswerRepository {

    private final HashMap<Integer, Answer> answerHashMap = new HashMap<>();

    private Answer answer1;
    private Answer answer2;
    private Answer answer3;
    private Answer answer4;
    private Answer answer5;
    private Answer answer6;
    private Answer answer7;
    private Answer answer8;

    {
        answer1 = Answer.builder()
                .id(1)
                .text("Вперёд, навстречу приключениям!")
                .choiceType(ChoiceType.HASNEXT)
                .nextQuestionId(2)
                .build();
        answer2 = Answer.builder()
                .id(2)
                .text("Я лучше дома посижу и на диване полежу")
                .choiceType(ChoiceType.LOST)
                .loosingCause(new LoosingCause(1, "Всадники на черных лошадях врываются в твой дом, отбирают кольцо и убивают тебя."))
                .build();
        answer3 = Answer.builder()
                .id(3)
                .text("Умный горы обойдет, а мы пойдём в горы, точнее под горой")
                .choiceType(ChoiceType.HASNEXT)
                .nextQuestionId(3)
                .build();
        answer4 = Answer.builder()
                .id(4)
                .text("Нам поможет заграница, то есть белый маг Басурман")
                .choiceType(ChoiceType.LOST)
                .loosingCause(new LoosingCause(2, "Белый маг оказывается предателем. Кольцо в руках врага. Ты и твои спутники убиты орками."))
                .build();
        answer5 = Answer.builder()
                .id(5)
                .text("Эти гигантские орлы, пахнущие мертвечиной, кажутся отличнымм решением вопроса")
                .choiceType(ChoiceType.HASNEXT)
                .nextQuestionId(4)
                .build();
        answer6 = Answer.builder()
                .id(6)
                .text("Лес с мирными симпатичными живыми деревьями. Что может случиться плохого?")
                .choiceType(ChoiceType.LOST)
                .loosingCause(new LoosingCause(3, "Живые деревья оказываются людоедами. Ты и твои спутники съедены."))
                .build();
        answer7 = Answer.builder()
                .id(7)
                .text("Гори оно синим пламенем. Бросить в жерло вулкана")
                .choiceType(ChoiceType.WIN)
                .build();
        answer8 = Answer.builder()
                .id(8)
                .text("Оставить себе, оно такое блестящее, моя прелесть…")
                .choiceType(ChoiceType.LOST)
                .loosingCause(new LoosingCause(4, "Хозяин кольца подаёт на вас в суд, который затягивается на 100 лет. В результате отсуживает у вас нору под холмом и право видеться с ребенком. Ой нет, это уже из другой истории…"))
                .build();

    }

    public AnswerRepository() {
        answerHashMap.put(answer1.getId(), answer1);
        answerHashMap.put(answer2.getId(), answer2);
        answerHashMap.put(answer3.getId(), answer3);
        answerHashMap.put(answer4.getId(), answer4);
        answerHashMap.put(answer5.getId(), answer5);
        answerHashMap.put(answer6.getId(), answer6);
        answerHashMap.put(answer7.getId(), answer7);
        answerHashMap.put(answer8.getId(), answer8);
    }

    public Answer getAnswerById(int id) {
        Answer answer = answerHashMap.get(id);
        if(answer != null) {
            return answer;
        } else {
            log.error("Ответа с id [{}] не существует", id);
            throw new NullPointerException("Ответа с нужным индексом не существует");
        }
    }





}