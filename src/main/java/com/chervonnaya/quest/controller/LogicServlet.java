package com.chervonnaya.quest.controller;


import com.chervonnaya.quest.model.Answer;
import com.chervonnaya.quest.model.ChoiceType;
import com.chervonnaya.quest.model.Question;
import com.chervonnaya.quest.repository.AnswerRepository;
import com.chervonnaya.quest.repository.QuestionRepository;
import com.chervonnaya.quest.service.StatisticsUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@WebServlet(name = "LogicServlet", value = "/game")
public class LogicServlet extends HttpServlet {

    private final AnswerRepository answerRepository = new AnswerRepository();
    private final QuestionRepository questionRepository = new QuestionRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Получаем текущую сессию
        HttpSession currentSession = request.getSession();
        request.setCharacterEncoding("UTF-8");

        if(currentSession.getAttribute("username") == null) {
            String username = request.getParameter("username");
            currentSession.setAttribute("username", username);
        }

        int questionId = 1;
        int chapterNumber = 1;

        int counter = StatisticsUtil.getStatistics(request, response,"counter");
        int counterWon = StatisticsUtil.getStatistics(request, response,"counterWon");
        int counterLost = StatisticsUtil.getStatistics(request, response,"counterLost");

        if(request.getParameter("answerid")  != null) {
            int answerId = Integer.parseInt(request.getParameter("answerid"));
            Answer answer = answerRepository.getAnswerById(answerId);
            if(answer.getChoiceType() == ChoiceType.HASNEXT) {
                try {
                    questionId = answer.getNextQuestionId();
                } catch (NullPointerException e) {
                    //у вопроса нет такого поля, логи
                }
                chapterNumber = StatisticsUtil.getStatistics(request, response,"chapterNumber");
                chapterNumber++;
            } else if (answer.getChoiceType() == ChoiceType.LOST) {
                request.setAttribute("loosingCause", answer.getLoosingCause().getText());
                StatisticsUtil.setStatistics(request, response,"counter", ++counter);
                StatisticsUtil.setStatistics(request, response,"counterLost", ++counterLost);
                getServletContext().getRequestDispatcher("/gameover.jsp").forward(request, response);
            } else if(answer.getChoiceType() == ChoiceType.WIN) {
                StatisticsUtil.setStatistics(request, response,"counter", ++counter);
                StatisticsUtil.setStatistics(request, response,"counterWon", ++counterWon);
                getServletContext().getRequestDispatcher("/youwon.jsp").forward(request, response);
            } else {
                //тут какая-то ошибка, нужно логировать
            }
        }

        Question question;
        ArrayList<Answer> answers;
        try {
            question = questionRepository.getQuestionById(questionId);
            answers = question.getAnswers();
            Collections.shuffle(answers);
            request.setAttribute("question", question);
            request.setAttribute("answers", answers);
            currentSession.setAttribute("chapterNumber", chapterNumber);
        } catch (NullPointerException e) {
            //вопроса или ответа с таким номером не существует

        }

        getServletContext().getRequestDispatcher("/play.jsp").forward(request, response);
    }

}