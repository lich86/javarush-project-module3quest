package com.chervonnaya.quest.repository;

import com.chervonnaya.quest.model.Question;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class QuestionRepository {

    private final AnswerRepository answerRepository = new AnswerRepository();
    private final HashMap<Integer, Question> questionHashMap = new HashMap<>();

    private Question question1;
    private Question question2;
    private Question question3;
    private Question question4;


     {
         question1 = new Question(1, "Итак, пришло время сделать решительный шаг. Ты отправишься в путешествие?",
                answerRepository.getAnswerById(1), answerRepository.getAnswerById(2));
         question2 = new Question(2, "Ты и твои спутники благополучно добираетесь до обители эльфов<br>" +
                 "На совете народов принято решение выступить в путь. Куда вы отправитесь?",
                 answerRepository.getAnswerById(3), answerRepository.getAnswerById(4));
         question3 = new Question(3, "Под горой в неравной схватке с альцгеймером вы теряете мага Пендальфа<br>" +
                 "Куда дальше?",
                 answerRepository.getAnswerById(5), answerRepository.getAnswerById(6));
         question4 = new Question(4, "Гигантские орлы оказываются мирными птицами и соглашаются отнести вас к огненной горе<br>" +
                 "Что делать с кольцом?",
                 answerRepository.getAnswerById(7), answerRepository.getAnswerById(8));
    }

    public QuestionRepository() {
         questionHashMap.put(question1.getId(), question1);
         questionHashMap.put(question2.getId(), question2);
         questionHashMap.put(question3.getId(), question3);
         questionHashMap.put(question4.getId(), question4);
    }

    public Question getQuestionById(int id) {
        Question question = questionHashMap.get(id);
        if(question != null) {
            return question;
        } else {
            log.error("Вопроса с id [{}] не существует", id);
            throw new NullPointerException("Вопроса с нужным индексом не существует");
        }
    }

}
