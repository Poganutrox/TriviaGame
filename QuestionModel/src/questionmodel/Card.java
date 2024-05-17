package questionmodel;

import java.io.Serializable;

public class Card implements Serializable {
    String question;
    String correctAnswer;
    String category;
    String[] options = new String[4];

    public Card(String question, String[] options, String correctAnswer, String category) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.category = category;
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }
}
