package com.chervonnaya.quest.repository;

import com.chervonnaya.quest.model.Question;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.junit.jupiter.api.Assertions.*;

class QuestionRepositoryTest extends Mockito {
    @Test
    void constructor_Should_PutAllQuestionsToHashMap() throws NoSuchFieldException, IllegalAccessException {
        QuestionRepository questionRepository = new QuestionRepository();
        //Получаем все вопросы репозитория и складываем их в Map
        Map<Integer, Question> questionsActual = Arrays.stream(QuestionRepository.class.getDeclaredFields())
                .filter(field -> field.getType().getSimpleName().equals("Question"))
                .peek(field -> field.setAccessible(true))
                .map(field -> {
                    try {
                        return (Question) field.get(questionRepository);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(toMap(Question::getId, question -> question));

        //Получаем HashMap, в котрую вопросы сложил конструктор
        Field privateField = QuestionRepository.class.getDeclaredField("questionHashMap");
        privateField.setAccessible(true);
        HashMap<Integer, Question> questionExpected = (HashMap<Integer, Question>) privateField.get(questionRepository);

        assertEquals(questionsActual, questionExpected);
    }

    @Test
    void getAnswerById_ShouldReturnCorrectQuestionIfNotNull() throws NoSuchFieldException, IllegalAccessException {
        QuestionRepository questionRepository = new QuestionRepository();
        Field privateField = QuestionRepository.class.getDeclaredField("question1");
        privateField.setAccessible(true);
        Question questionActual = (Question) privateField.get(questionRepository);

        Question questionExpected = new QuestionRepository().getQuestionById(questionActual.getId());

        assertEquals(questionActual, questionExpected);
    }

    @Test
    void getQuestionById_Should_ThrowExceptionIfQuestionIdIsIncorrect() {
        assertThrows(NullPointerException.class, () -> new QuestionRepository().getQuestionById(22));
    }
}