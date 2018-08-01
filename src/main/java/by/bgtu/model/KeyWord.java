package by.bgtu.model;

import by.bgtu.service.DistanceCalc;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "keyword")
public class KeyWord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "keyword_id")
    @JsonIgnore
    private Integer id;

    @Column(name = "value", unique = true)
    private String value;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "keyword_word", joinColumns = @JoinColumn(name = "keyword_id"),
            inverseJoinColumns = @JoinColumn(name = "word_id"))
    private Set<Word> words = new HashSet<>();

    @ManyToMany(mappedBy = "keyWords")
    @JsonIgnore
    private Set<Parameter> parameters;

    @ManyToMany(mappedBy = "keyWords")
    @JsonIgnore
    private Set<Answer> answers;


    @ManyToMany(mappedBy = "keyWords")
    @JsonIgnore
    private Set<Subject> subjects;

    @Transient
    private final static DistanceCalc distanceCalc = new DistanceCalc(1, 1, 1, 1);

    public KeyWord() {
    }

    public String getWordsString() {
        return wordsToString();
    }

    public void setWordsString(String wordsString) {
        if (wordsString != null) {
            String[] wordsArray = wordsString.split(",");
            words = new HashSet<>();
            for (String str : wordsArray) {
                Word word = new Word(str.trim());
                word.getKeyWords().add(this);
                words.add(word);
            }
        }
    }

    public KeyWord(String keyWord, String... words) {
        value = keyWord;
        for (String value : words) {
            Word word = new Word(value);
            this.words.add(word);
        }
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Set<Parameter> parameters) {
        this.parameters = parameters;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Set<Word> getWords() {
        return words;
    }

    public void setWords(Set<Word> words) {
        this.words = words;
    }

    @Override
    public String toString() {
        return value + " (" + wordsToString() + ")";
    }

    public String wordsToString() {
        return Util.toString(words);
    }

    /**
     * return true if given words matches this keyword or false otherwise
     */
    public boolean isEquals(String firstWord, String... nextWords) {
        String[] keyWords = value.toLowerCase().split(Util.SPLIT_EXPRESION);
        String[] userWords = new String[nextWords.length + 1];
        userWords[0] = firstWord;
        if (nextWords.length > 0) {
            System.arraycopy(nextWords, 0, userWords, 1, nextWords.length);
        }
        if (isEqualsArr(keyWords, userWords)) return true;
        for (Word word : words) {
            String[] simWords = word.getValue().toLowerCase().split(Util.SPLIT_EXPRESION);
            if (isEqualsArr(simWords, userWords)) return true;
        }
        return false;
    }

    /**
     * return true if keyWords matches to userWords or false otherwise
     */
    private static boolean isEqualsArr(String[] keyWords, String[] userWords) {
        if (userWords.length < keyWords.length) return false;
        int distance = 0;
        for (int i = 0; i < keyWords.length; i++) {
            if (keyWords[i].isEmpty()) return false;
            String userWord = userWords[i].toLowerCase();
            String keyword = keyWords[i];
            int len = keyword.length();
            int indexBegin = keyWords[i].indexOf("*");
            if (indexBegin > -1){
                keyword = keyWords[i].substring(0, indexBegin);
                len = Integer.MAX_VALUE;
            } else {
                indexBegin = keyWords[i].indexOf("?");
                if (indexBegin > -1){
                    keyword = keyWords[i].substring(0, indexBegin);
                    len = keyword.length() + Integer.parseInt(keyWords[i].substring(indexBegin + 1, keyWords[i].length()));
                }
            }
            if (userWord.length() > len) {
                return false;
            }
            if (userWord.startsWith(keyword)) {
                return true;
            }
            if (keyword.length() < 4) return false;
            distance += distanceCalc.compute(userWord, keyword);
        }
        return distance <= keyWords.length;
    }

    public void set(KeyWord value) {
        this.id = value.id;
        this.value = value.value;
        this.words = value.words;
    }
}
