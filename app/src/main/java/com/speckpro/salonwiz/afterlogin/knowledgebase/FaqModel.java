package com.speckpro.salonwiz.afterlogin.knowledgebase;

public class FaqModel {
    private String question;
    private String answer;


    public FaqModel(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String title) {
        this.question = title;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String description) {
        this.answer = answer;
    }

}
