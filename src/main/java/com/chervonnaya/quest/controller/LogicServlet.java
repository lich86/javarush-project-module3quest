package com.chervonnaya.quest.controller;

import com.chervonnaya.quest.service.QuestService;
import com.chervonnaya.quest.service.QuestServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Getter
@WebServlet(name = "LogicServlet", value = "/game")
public class LogicServlet extends HttpServlet {

    private QuestService service;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession currentSession = request.getSession();
        request.setCharacterEncoding("UTF-8");

        service = new QuestServiceImpl(currentSession, request, response);

        if(currentSession.getAttribute("username") == null) {
            String username = request.getParameter("username");
            currentSession.setAttribute("username", username);
        }

        service.getAllCounters();
        String redirectUrl = service.handlePlayerChoice();

        getServletContext().getRequestDispatcher(redirectUrl).forward(request, response);
    }

}