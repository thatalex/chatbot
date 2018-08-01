package by.bgtu.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * This class describes object for storing conversation history
 */
public class Conversation implements Serializable {

    private final List<Entry> entries = new ArrayList<>();

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries.clear();
        this.entries.addAll(entries);
    }

    public void add(String question, String answers) {
        entries.add(new Entry(question, answers));
    }

    static class Entry implements Serializable {
        private final String question;

        private final String answer;

        Entry(String question, String answer) {
            this.question = question != null ? question : "";
            this.answer = answer != null ? answer : "";
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswer() {
            return answer;
        }
    }
}
