package by.bgtu.model;


import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "subject")
public class Subject implements KeyWorded {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "subject_id")
    @JsonIgnore
    private Integer id;

    @NotNull
    @Column(name = "name", unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "subject_keyword", joinColumns = @JoinColumn(name = "keyword_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private Set<KeyWord> keyWords = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "subject_id")
    private Set<SubjectParameterValue> subjectParameterValues = new HashSet<>();


    public Subject() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Subject(String name) {
        this.name = name;
    }

    public static Builder newBuilder(String name) {
        return new Builder(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<KeyWord> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(Set<KeyWord> keyWords) {
        this.keyWords = keyWords;
    }

    public Set<SubjectParameterValue> getSubjectParameterValues() {
        return subjectParameterValues;
    }

    public void setSubjectParameterValues(Set<SubjectParameterValue> subjectParameterValues) {
        this.subjectParameterValues = subjectParameterValues;
    }

    public SubjectParameterValue getParameterValue(String name) {
        for (SubjectParameterValue subjectParameterValue : subjectParameterValues) {
            if (subjectParameterValue.getParameter().isEquals(name)) {
                return subjectParameterValue;
            }
        }
        return null;

    }

    public boolean isEquals(String word) {
        if (word.trim().toLowerCase().startsWith(name.toLowerCase())) return true;
        for (KeyWord keyWord : keyWords) {
            if (keyWord.isEquals(word)) {
                return true;
            }
        }
        return false;
    }

    public SubjectParameterValue getParameterValue(Integer id) {
        for (SubjectParameterValue subjectParameterValue : subjectParameterValues) {
            if (subjectParameterValue.getId().equals(id)) {
                return subjectParameterValue;
            }
        }
        return null;
    }

    public static class Builder {

        private final Subject subject;

        private Builder(String name) {
            subject = new Subject(name);
        }

        public Builder addKeyword(String keyword, String... words) {
            KeyWord keyWord = new KeyWord(keyword, words);
            subject.keyWords.add(keyWord);
            return this;
        }

        public Builder addParameter(String name, String value, KeyWord... keyWords) {
            SubjectParameterValue subjectParameterValue = new SubjectParameterValue(name, value, keyWords);
            subjectParameterValue.setSubject(subject);
            subject.add(subjectParameterValue);
            return this;
        }

        public Subject build() {
            return subject;
        }

    }

    private void add(SubjectParameterValue subjectParameterValue) {
        subjectParameterValues.add(subjectParameterValue);
    }

    public String keyWordsToString() {
        return Util.toString(keyWords);
    }


    public String parametersToString() {
        StringBuilder builder = new StringBuilder();
        if (!subjectParameterValues.isEmpty()) {
            for (SubjectParameterValue value : subjectParameterValues) {
                builder.append(value).append("; ");
            }
            builder.setLength(builder.length() - 2);
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return name + " (" + keyWordsToString() + "): " + parametersToString() + " ";
    }
}
