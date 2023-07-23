package com.chervonnaya.quest.controller;

import com.chervonnaya.quest.model.Answer;
import com.chervonnaya.quest.model.ChoiceType;
import com.chervonnaya.quest.model.Question;
import com.chervonnaya.quest.repository.AnswerRepository;
import com.chervonnaya.quest.repository.QuestionRepository;
import com.chervonnaya.quest.service.StatisticsUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;


class LogicServletTest extends Mockito {
    @InjectMocks
    LogicServlet logicServletMock;
    @Mock
    HttpServletRequest requestMock;
    @Mock
    HttpServletResponse responseMock;
    @Mock
    HttpSession currentSessionMock;
    @Mock
    RequestDispatcher dispatcherMock;
    @Mock
    ServletConfig configMock;
    @Mock
    ServletContext servletContextMock;
    @Mock
    AnswerRepository answerRepositoryMock;
    @Mock
    QuestionRepository questionRepositoryMock;
    @Mock
    Answer answerMock;
    @Mock
    Question questionMock;
    AutoCloseable closeable;

    @BeforeEach
    void initServlet() throws ServletException {
        closeable = MockitoAnnotations.openMocks(this);
        Mockito.when(requestMock.getSession()).thenReturn(currentSessionMock);
        logicServletMock.init(configMock);
        Mockito.when(configMock.getServletContext()).thenReturn(servletContextMock);

        Mockito.when(currentSessionMock.getAttribute("username")).thenReturn("Елизавета");
        Mockito.when(requestMock.getParameter("answerid")).thenReturn("1");
        Mockito.when(answerRepositoryMock.getAnswerById(anyInt())).thenReturn(answerMock);
        Mockito.when(questionRepositoryMock.getQuestionById(anyInt())).thenReturn(questionMock);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void doPost_Should_SetAttributeUsernameIfNull() throws ServletException, IOException {
        String username = "Елизавета";
        Mockito.when(currentSessionMock.getAttribute("username")).thenReturn(null);
        Mockito.when(requestMock.getParameter("username")).thenReturn(username);
        Mockito.when(logicServletMock.getServletContext().getRequestDispatcher(anyString()))
                .thenReturn(dispatcherMock);

        logicServletMock.doPost(requestMock, responseMock);

        Mockito.verify(currentSessionMock).setAttribute("username", username);
    }

    @ParameterizedTest
    @ValueSource(strings = {"counter", "counterLost", "counterWon"})
    void doPost_Should_GetAllStatistic(String attribute) throws ServletException, IOException {
        Mockito.when(logicServletMock.getServletContext().getRequestDispatcher(anyString()))
                .thenReturn(dispatcherMock);
        try (MockedStatic<StatisticsUtil> statisticsUtilMockedStatic = Mockito.mockStatic(StatisticsUtil.class)){
            logicServletMock.doPost(requestMock, responseMock);
            statisticsUtilMockedStatic.verify(() -> StatisticsUtil.getStatistics(requestMock, responseMock,attribute));
        }
    }

    @Test
    void doPost_Should_SetNextQuestionIdIfHasNext() throws ServletException, IOException {
        Mockito.when(answerMock.getChoiceType()).thenReturn(ChoiceType.HASNEXT);
        Mockito.when(logicServletMock.getServletContext().getRequestDispatcher(anyString()))
                .thenReturn(dispatcherMock);

        logicServletMock.doPost(requestMock, responseMock);

        Mockito.verify(answerMock).getNextQuestionId();
    }

    @Test
    void doPost_Should_ForwardToPlayJspIfHasNext() throws ServletException, IOException {
        Mockito.when(answerMock.getChoiceType()).thenReturn(ChoiceType.HASNEXT);
        Mockito.when(servletContextMock.getRequestDispatcher("/play.jsp"))
                .thenReturn(dispatcherMock);

        logicServletMock.doPost(requestMock, responseMock);

        Mockito.verify(dispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void doPost_Should_SetLoosingCauseIfLost() throws ServletException, IOException {
        Mockito.when(answerMock.getChoiceType()).thenReturn(ChoiceType.LOST);
        Mockito.when(logicServletMock.getServletContext().getRequestDispatcher(anyString()))
                .thenReturn(dispatcherMock);

        logicServletMock.doPost(requestMock, responseMock);

        Mockito.verify(requestMock).setAttribute(eq("loosingCause"), anyString());
    }

    @Test
    void doPost_Should_ForwardToGameOverJspIfLost() throws ServletException, IOException {
        Mockito.when(answerMock.getChoiceType()).thenReturn(ChoiceType.LOST);
        Mockito.when(servletContextMock.getRequestDispatcher("/gameover.jsp"))
                .thenReturn(dispatcherMock);

        logicServletMock.doPost(requestMock, responseMock);

        Mockito.verify(dispatcherMock).forward(requestMock, responseMock);
    }


    @Test
    void doPost_Should_ForwardToPlayJspIfWin() throws ServletException, IOException {
        Mockito.when(answerMock.getChoiceType()).thenReturn(ChoiceType.WIN);
        Mockito.when(servletContextMock.getRequestDispatcher("/youwon.jsp"))
                .thenReturn(dispatcherMock);

        logicServletMock.doPost(requestMock, responseMock);

        Mockito.verify(dispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void doPost_Should_SetAttributeQuestionIfHasNext() throws ServletException, IOException {
        Mockito.when(answerMock.getChoiceType()).thenReturn(ChoiceType.HASNEXT);
        Mockito.when(logicServletMock.getServletContext().getRequestDispatcher(anyString()))
                .thenReturn(dispatcherMock);

        logicServletMock.doPost(requestMock, responseMock);

        Mockito.verify(requestMock).setAttribute("question", questionMock);
    }

    @Test
    void doPost_Should_SetAttributeAnswersIfHasNext() throws ServletException, IOException {
        Mockito.when(answerMock.getChoiceType()).thenReturn(ChoiceType.HASNEXT);
        Mockito.when(questionMock.getAnswers()).thenReturn(new ArrayList<>());
        Mockito.when(logicServletMock.getServletContext().getRequestDispatcher(anyString()))
                .thenReturn(dispatcherMock);

        logicServletMock.doPost(requestMock, responseMock);

        Mockito.verify(requestMock).setAttribute(eq("answers"), anyList());
    }


}