package by.bgtu.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "answer")
public class Answer implements KeyWorded {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "answer_id")
    @JsonIgnore
    private int id;

    @Column(name = "value")
    private String value;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "answer_keyword", joinColumns = @JoinColumn(name = "answer_id"),
            inverseJoinColumns = @JoinColumn(name = "keyword_id"))
    private Set<KeyWord> keyWords;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Set<KeyWord> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(Set<KeyWord> keyWords) {
        this.keyWords = keyWords;
    }

    @Override
    public String toString() {
        return value;
    }

    public String keyWordsToString() {
        return Util.toString(keyWords);
    }

    public boolean hasKeyWord(KeyWord keyWord) {
        for (KeyWord answerKeyWord : keyWords) {
            if (answerKeyWord.getValue().equals(keyWord.getValue())) {
                return true;
            }
        }
        return false;
    }
}