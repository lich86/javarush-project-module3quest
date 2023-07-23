package com.chervonnaya.quest.repository;

import com.chervonnaya.quest.model.Answer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.junit.jupiter.api.Assertions.*;


class AnswerRepositoryTest extends Mockito {

    @Test
    void constructor_Should_PutAllQuestionsToHashMap() throws NoSuchFieldException, IllegalAccessException {
        AnswerRepository answerRepository = new AnswerRepository();
        //Получаем все ответы репозитория и складываем их в Map
        Map<Integer, Answer> answersActual = Arrays.stream(AnswerRepository.class.getDeclaredFields())
                .filter(field -> field.getType().getSimpleName().equals("Answer"))
                .peek(field -> field.setAccessible(true))
                .map(field -> {
                    try {
                        return (Answer) field.get(answerRepository);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(toMap(Answer::getId, answer -> answer));

        //Получаем HashMap, в котрую ответы сложил конструктор
        Field privateField = AnswerRepository.class.getDeclaredField("answerHashMap");
        privateField.setAccessible(true);
        HashMap<Integer, Answer> answersExpected = (HashMap<Integer, Answer>) privateField.get(answerRepository);

        assertEquals(answersActual, answersExpected);
    }

    @Test
    void getAnswerById_ShouldReturnCorrectAnswerIfNotNull() throws NoSuchFieldException, IllegalAccessException {
        AnswerRepository answerRepository = new AnswerRepository();
        Field privateField = AnswerRepository.class.getDeclaredField("answer1");
        privateField.setAccessible(true);
        Answer answerActual = (Answer) privateField.get(answerRepository);
        Answer answerExpected = new AnswerRepository().getAnswerById(answerActual.getId());

        assertEquals(answerActual, answerExpected);
    }

    @Test
    void getAnswerById_Should_ThrowExceptionIfAnswerIdIsIncorrect() {
        assertThrows(NullPointerException.class, () -> new AnswerRepository().getAnswerById(22));
    }
}