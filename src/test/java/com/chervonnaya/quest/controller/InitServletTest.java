package com.chervonnaya.quest.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

class InitServletTest extends Mockito {
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
    @Spy
    InitServlet initServletSpy;

    AutoCloseable closeable;

    @BeforeEach
    void initServlet() throws ServletException {
        closeable = MockitoAnnotations.openMocks(this);
        Mockito.when(requestMock.getSession(true)).thenReturn(currentSessionMock);
        initServletSpy.init(configMock);
        Mockito.when(configMock.getServletContext()).thenReturn(servletContextMock);
        Mockito.when(initServletSpy.getServletContext().getRequestDispatcher("/play.jsp"))
                .thenReturn(dispatcherMock);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void doGet_Should_SetAttributeIpAddress() throws ServletException, IOException {
        String IP = "1.2.3.4";
        Mockito.when(requestMock.getHeader("X-FORWARDED-FOR")).thenReturn(IP);
        Mockito.when(requestMock.getRemoteAddr()).thenReturn(IP);

        initServletSpy.doGet(requestMock, responseMock);

        Mockito.verify(currentSessionMock).setAttribute("IP", IP);
    }

    @Test
    void doPost_Should_ForwardToPlayJspIfLost() throws ServletException, IOException {
        Mockito.when(servletContextMock.getRequestDispatcher("/play.jsp"))
                .thenReturn(dispatcherMock);

        initServletSpy.doGet(requestMock, responseMock);

        Mockito.verify(dispatcherMock).forward(requestMock, responseMock);
    }


}