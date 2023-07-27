package com.chervonnaya.quest.controller;

import com.chervonnaya.quest.model.Answer;
import com.chervonnaya.quest.model.ChoiceType;
import com.chervonnaya.quest.model.Question;
import com.chervonnaya.quest.repository.AnswerRepository;
import com.chervonnaya.quest.repository.QuestionRepository;
import com.chervonnaya.quest.service.QuestService;
import com.chervonnaya.quest.service.QuestServiceImpl;
import com.chervonnaya.quest.util.StatisticsUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
    QuestService serviceMock;
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
    AutoCloseable closeable;

    @BeforeEach
    void initServlet() throws ServletException {
        closeable = MockitoAnnotations.openMocks(this);
        Mockito.when(requestMock.getSession()).thenReturn(currentSessionMock);
        logicServletMock.init(configMock);
        Mockito.when(configMock.getServletContext()).thenReturn(servletContextMock);
        Mockito.when(logicServletMock.getServletContext().getRequestDispatcher(anyString()))
                .thenReturn(dispatcherMock);
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

    @Test
    @Disabled
    void doPost_ShouldCallGetAllCounters() throws ServletException, IOException {
        Mockito.when(currentSessionMock.getAttribute("username")).thenReturn("Елизавета");

        logicServletMock.doPost(requestMock, responseMock);

        Mockito.verify(serviceMock).getAllCounters();
    }

    @Test
    void doPost_ShoulCallForward() throws ServletException, IOException {
        logicServletMock.doPost(requestMock, responseMock);

        Mockito.verify(dispatcherMock).forward(requestMock, responseMock);
    }

}