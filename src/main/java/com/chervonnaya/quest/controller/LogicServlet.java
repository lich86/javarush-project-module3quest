package com.chervonnaya.quest.controller;


import com.chervonnaya.quest.model.Answer;
import com.chervonnaya.quest.model.ChoiceType;
import com.chervonnaya.quest.model.Question;
import com.chervonnaya.quest.repository.QuestionRepository;
import com.chervonnaya.quest.repository.AnswerRepository;
import com.chervonnaya.quest.service.StatisticsUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@WebServlet(name = "LogicServlet", value = "/game")
public class LogicServlet extends HttpServlet {

    private AnswerRepository answerRepository = new AnswerRepository();
    private QuestionRepository questionRepository = new QuestionRepository();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                    log.error("У вопроса [{}] нет поля nextQuestion", questionId);
                    getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
                }
                chapterNumber = StatisticsUtil.getStatistics(request, response,"chapterNumber");
                chapterNumber++;
            } else if (answer.getChoiceType() == ChoiceType.LOST) {
                try {
                    request.setAttribute("loosingCause", answer.getLoosingCause().getText());
                } catch (NullPointerException e) {
                    log.error("У вопроса [{}] нет поля loosingCause", answer.getId());
                    request.setAttribute("loosingCause", "Вы проиграли!");
                }
                StatisticsUtil.setStatistics(request, response,"counter", ++counter);
                StatisticsUtil.setStatistics(request, response,"counterLost", ++counterLost);
                getServletContext().getRequestDispatcher("/gameover.jsp").forward(request, response);
                return;
            } else if(answer.getChoiceType() == ChoiceType.WIN) {
                StatisticsUtil.setStatistics(request, response,"counter", ++counter);
                StatisticsUtil.setStatistics(request, response,"counterWon", ++counterWon);
                getServletContext().getRequestDispatcher("/youwon.jsp").forward(request, response);
                return;
            } else {
                log.error("У вопроса [{}] нет поля ChoiceType, либо в нём ошибка", answer.getId());
                getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
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
            log.error("Проблема с вопросом id [{}] (нет такого вопроса, либо у него не установлены ответы)", questionId);
            getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
        }

        getServletContext().getRequestDispatcher("/play.jsp").forward(request, response);
    }

}