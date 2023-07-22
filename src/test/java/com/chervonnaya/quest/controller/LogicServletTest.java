package com.chervonnaya.quest.controller;

import com.chervonnaya.quest.model.Answer;
import com.chervonnaya.quest.model.ChoiceType;
import com.chervonnaya.quest.model.Question;
import com.chervonnaya.quest.repository.AnswerRepository;
import com.chervonnaya.quest.repository.QuestionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
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
        Mockito.when(logicServletMock.getServletContext().getRequestDispatcher(anyString()))
                .thenReturn(dispatcherMock);

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

        logicServletMock.doPost(requestMock, responseMock);

        Mockito.verify(currentSessionMock).setAttribute("username", username);
    }

    @Test
    void doPost_Should_SetNextQuestionIdIfHasNext() throws ServletException, IOException {
        Mockito.when(answerMock.getChoiceType()).thenReturn(ChoiceType.HASNEXT);

        logicServletMock.doPost(requestMock, responseMock);

        Mockito.verify(answerMock).getNextQuestionId();
    }

    @Test
    void doPost_Should_SetLoosingCauseIfLost() throws ServletException, IOException {
        Mockito.when(answerMock.getChoiceType()).thenReturn(ChoiceType.LOST);

        logicServletMock.doPost(requestMock, responseMock);

        Mockito.verify(requestMock).setAttribute(eq("loosingCause"), anyString());
    }

    @Test
    @Disabled
    void doPost_Should_ForwardToGameOverJspIfLost() throws ServletException, IOException {
        Mockito.when(answerMock.getChoiceType()).thenReturn(ChoiceType.LOST);
        Mockito.when(servletContextMock.getRequestDispatcher("/gameover1.jsp"))
                .thenReturn(dispatcherMock);

        logicServletMock.doPost(requestMock, responseMock);

        Mockito.verify(dispatcherMock).forward(requestMock, responseMock);
    }

    @Test
    void doPost_Should_SetAttributeQuestionIfHasNext() throws ServletException, IOException {
        Mockito.when(answerMock.getChoiceType()).thenReturn(ChoiceType.HASNEXT);

        logicServletMock.doPost(requestMock, responseMock);

        Mockito.verify(requestMock).setAttribute("question", questionMock);
    }

    @Test
    void doPost_Should_SetAttributeAnswersIfHasNext() throws ServletException, IOException {
        Mockito.when(answerMock.getChoiceType()).thenReturn(ChoiceType.HASNEXT);
        Mockito.when(questionMock.getAnswers()).thenReturn(new ArrayList<>());

        logicServletMock.doPost(requestMock, responseMock);

        Mockito.verify(requestMock).setAttribute(eq("answers"), anyList());
    }


}