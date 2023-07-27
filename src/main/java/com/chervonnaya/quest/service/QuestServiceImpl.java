package com.chervonnaya.quest.service;

import com.chervonnaya.quest.model.Answer;
import com.chervonnaya.quest.model.ChoiceType;
import com.chervonnaya.quest.model.Question;
import com.chervonnaya.quest.repository.AnswerRepository;
import com.chervonnaya.quest.repository.QuestionRepository;
import com.chervonnaya.quest.util.StatisticsUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;

@Slf4j
@NoArgsConstructor
public class QuestServiceImpl implements QuestService{

    private HttpSession currentSession;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private final AnswerRepository answerRepository = new AnswerRepository();
    private final QuestionRepository questionRepository = new QuestionRepository();

    public QuestServiceImpl(HttpSession currentSession, HttpServletRequest request, HttpServletResponse response) {
        this.currentSession = currentSession;
        this.request = request;
        this.response = response;
    }

    @Override
    public void getAllCounters() {
        StatisticsUtil.getStatistics(request, response,"counter");
        StatisticsUtil.getStatistics(request, response,"counterWon");
        StatisticsUtil.getStatistics(request, response,"counterLost");
    }

    public Answer getAnswer() {
        Answer answer = null;
        if(request.getParameter("answerid") != null) {
            int answerId = Integer.parseInt(request.getParameter("answerid"));
            answer = answerRepository.getAnswerById(answerId);
        }
        return answer;
    }

    public void handleLoss(Answer answer) {
        try {
            request.setAttribute("loosingCause", answer.getLoosingCause().getText());
        } catch (NullPointerException e) {
            log.error("У вопроса [{}] нет поля loosingCause", answer.getId());
            request.setAttribute("loosingCause", "Вы проиграли!");
        }
        int counter = StatisticsUtil.getStatistics(request, response,"counter");
        int counterLost = StatisticsUtil.getStatistics(request, response,"counterLost");
        StatisticsUtil.setStatistics(request, response,"counter", ++counter);
        StatisticsUtil.setStatistics(request, response,"counterLost", ++counterLost);
    }

    public void handleWin() {
        int counter = StatisticsUtil.getStatistics(request, response,"counter");
        int counterWon = StatisticsUtil.getStatistics(request, response,"counterWon");
        StatisticsUtil.setStatistics(request, response,"counter", ++counter);
        StatisticsUtil.setStatistics(request, response,"counterWon", ++counterWon);
    }

    @Override
    public String getRedirectUrl(Answer answer) {
        String redirectUrl;
        if(answer.getChoiceType() == ChoiceType.HASNEXT) {
            redirectUrl = "/play.jsp";
        } else if(answer.getChoiceType() == ChoiceType.WIN) {
            handleWin();
            redirectUrl = "/youwon.jsp";
        } else if(answer.getChoiceType() == ChoiceType.LOST) {
            handleLoss(answer);
            redirectUrl = "/gameover.jsp";
        } else {
            log.error("У вопроса [{}] нет поля ChoiceType, либо в нём ошибка", answer.getId());
            redirectUrl = "/error.jsp";
        }
        return redirectUrl;
    }

    public String handlePlayerChoice() {
        int questionId = 1;
        String redirectUrl = "/play.jsp";

        Answer answer = getAnswer();
        if(answer != null) {
            redirectUrl = getRedirectUrl(answer);

            if(answer.getChoiceType() == ChoiceType.HASNEXT) {
                questionId = answer.getNextQuestionId();
            }
        }


        int chapterNumber = 1;
        Question question;
        ArrayList<Answer> answers;

        try {
            question = questionRepository.getQuestionById(questionId);
            answers = question.getAnswers();
            Collections.shuffle(answers);
            request.setAttribute("question", question);
            request.setAttribute("answers", answers);
            if(currentSession.getAttribute("chapterNumber") != null) {
                chapterNumber = (int) currentSession.getAttribute("chapterNumber");
                chapterNumber++;
            }
            currentSession.setAttribute("chapterNumber", chapterNumber);
        } catch (NullPointerException e) {
            //вопроса или ответа с таким номером не существует
            log.error("Проблема с вопросом id [{}] (нет такого вопроса, либо у него не установлены ответы)", questionId);
            redirectUrl = "/error.jsp";
        }

        return redirectUrl;
    }
}
