package by.bgtu.service;

public interface ChatService {

    /**
     * return string containing answer for given questions
     * @param question sentences with questions
     * @return answers for given given questions
     */
    String getAnswers(String question);
}
