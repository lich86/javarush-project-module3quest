package com.chervonnaya.quest.service;

import com.chervonnaya.quest.model.Answer;
import com.chervonnaya.quest.model.ChoiceType;
import com.chervonnaya.quest.model.Question;
import com.chervonnaya.quest.repository.AnswerRepository;
import com.chervonnaya.quest.repository.QuestionRepository;
import com.chervonnaya.quest.util.StatisticsUtil;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QuestServiceImplTest extends Mockito {
    @InjectMocks
    QuestServiceImpl service;
    @Mock
    QuestionRepository questionRepositoryMock;
    @Mock
    HttpServletRequest requestMock;
    @Mock
    HttpServletResponse responseMock;
    @Mock
    HttpSession currentSessionMock;
    @Mock
    Answer answerMock;
    @Mock
    Question questionMock;
    AutoCloseable closeable;

    @BeforeEach
    void initServlet() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @ParameterizedTest
    @ValueSource(strings = {"counter", "counterLost", "counterWon"})
    void getAllCounters_Should_GetAllStatistics(String attribute) {
        try (MockedStatic<StatisticsUtil> statisticsUtilMockedStatic = Mockito.mockStatic(StatisticsUtil.class)){
            new QuestServiceImpl(currentSessionMock,requestMock,responseMock).getAllCounters();
            statisticsUtilMockedStatic.verify(() -> StatisticsUtil.getStatistics(requestMock, responseMock,attribute));
        }
    }

    @Test
    void getAnswer_Should_ReturnAnswerIfAnswerIdNotNull() {
        Mockito.when(requestMock.getParameter("answerid")).thenReturn("1");
        Answer answerActual = new AnswerRepository().getAnswerById(1);

        Answer answersExpected = service.getAnswer();

        assertEquals(answerActual, answersExpected);
    }

    @Test
    void handleLoss_Should_SetLoosingCause() {
        service.handleLoss(answerMock);

        Mockito.verify(requestMock).setAttribute(eq("loosingCause"), anyString());
    }

    @ParameterizedTest
    @CsvSource({"HASNEXT, /play.jsp", "WIN, /youwon.jsp", "LOST, /gameover.jsp,"})
    void getRedirectUrl_Should_ReturnCorrectUrl(String choiceType, String urlActual) {
        Mockito.when(answerMock.getChoiceType()).thenReturn(ChoiceType.valueOf(choiceType));

        String urlExpected = service.getRedirectUrl(answerMock);

        assertEquals(urlActual, urlExpected);
    }

    @Test
    void getRedirectUrl_Should_ReturnErrorIfAnswerHasNoChoiceType() {
        Mockito.when(answerMock.getChoiceType()).thenReturn(null);

        String urlExpected = service.getRedirectUrl(answerMock);

        assertEquals("/error.jsp", urlExpected);
    }

    @Test
    void getRedirectUrl_Should_CallHandleLossIfLost() {
        QuestServiceImpl service = new QuestServiceImpl(currentSessionMock,requestMock,responseMock);
        QuestServiceImpl serviceSpy = Mockito.spy(service);
        Mockito.when(answerMock.getChoiceType()).thenReturn(ChoiceType.valueOf("LOST"));
        Mockito.when(requestMock.getParameter("loosingCause")).thenReturn(anyString());

        serviceSpy.getRedirectUrl(answerMock);

        Mockito.verify(serviceSpy).handleLoss(answerMock);
    }

    @Test
    void getRedirectUrl_Should_CallHandleWinIfWon() {
        QuestServiceImpl service = new QuestServiceImpl(currentSessionMock,requestMock,responseMock);
        QuestServiceImpl serviceSpy = Mockito.spy(service);
        Mockito.when(answerMock.getChoiceType()).thenReturn(ChoiceType.valueOf("WIN"));

        serviceSpy.getRedirectUrl(answerMock);

        Mockito.verify(serviceSpy).handleWin();
    }

    @Test
    void handlePlayerChoice_Should_CallGetRedirectAnswerIfAnswerNotNull() {
        QuestServiceImpl service = new QuestServiceImpl(currentSessionMock,requestMock,responseMock);
        QuestServiceImpl serviceSpy = Mockito.spy(service);
        Mockito.when(serviceSpy.getAnswer()).thenReturn(answerMock);

        serviceSpy.handlePlayerChoice();

        Mockito.verify(serviceSpy).getRedirectUrl(answerMock);
    }

    @Test
    void handlePlayerChoice_Should_GetNextQuestionIdIfHasNext() {
        QuestServiceImpl service = new QuestServiceImpl(currentSessionMock,requestMock,responseMock);
        QuestServiceImpl serviceSpy = Mockito.spy(service);
        Mockito.when(serviceSpy.getAnswer()).thenReturn(answerMock);
        Mockito.when(answerMock.getChoiceType()).thenReturn(ChoiceType.valueOf("HASNEXT"));

        serviceSpy.handlePlayerChoice();

        Mockito.verify(answerMock).getNextQuestionId();
    }

    @Test
    void handlePlayerChoice_Should_SetAttributeQuestion() {
        Mockito.when(questionRepositoryMock.getQuestionById(anyInt())).thenReturn(questionMock);

        service.handlePlayerChoice();

        Mockito.verify(requestMock).setAttribute("question", questionMock);
    }

    @Test
    void handlePlayerChoice_Should_SetAttributeAnswers() {
        ArrayList<Answer> answersMock = Mockito.mock();
        Mockito.when(questionRepositoryMock.getQuestionById(anyInt())).thenReturn(questionMock);
        Mockito.when(questionMock.getAnswers()).thenReturn(answersMock);

        service.handlePlayerChoice();

        Mockito.verify(requestMock).setAttribute("answers", answersMock);
    }

    @Test
    void handlePlayerChoice_Should_SetAttributeChapterNumber() {
        new QuestServiceImpl(currentSessionMock,requestMock,responseMock).handlePlayerChoice();

        Mockito.verify(currentSessionMock).setAttribute(eq("chapterNumber"), anyInt());
    }

}