package com.chervonnaya.quest.controller;

import com.chervonnaya.quest.service.StatisticsUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

@WebServlet(name = "InitServlet", value = "/start")
public class InitServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Создание новой сессии
        HttpSession currentSession = request.getSession(true);
        ServletContext selvletContext = getServletContext();

        StatisticsUtil.getStatistics(request, response,"counter");
        StatisticsUtil.getStatistics(request, response,"counterWon");
        StatisticsUtil.getStatistics(request, response,"counterLost");
        if(selvletContext.getAttribute("startup") == null) {
            selvletContext.setAttribute("startup", new Date());
        }

        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        currentSession.setAttribute("IP", ipAddress);

        getServletContext().getRequestDispatcher("/play.jsp").forward(request, response);
    }

}