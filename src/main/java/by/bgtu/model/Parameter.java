package by.bgtu.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "parameter")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Parameter implements KeyWorded {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "parameter_id")
    @JsonIgnore
    private Integer id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "parameter_keyword", joinColumns = @JoinColumn(name = "keyword_id"),
            inverseJoinColumns = @JoinColumn(name = "parameter_id"))
    private Set<KeyWord> keyWords = new HashSet<>();

    @OneToMany(mappedBy = "parameter")
    @JsonIgnore
    private Set<SubjectParameterValue> subjectParameterValues;

    @Column(name = "name", unique = true)
    private String name;

    public Parameter() {
    }

    public Parameter(String name, KeyWord... keywords) {
        this.name = name;
        this.keyWords.addAll(Arrays.asList(keywords));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<SubjectParameterValue> getSubjectParameterValues() {
        return subjectParameterValues;
    }

    public void setSubjectParameterValues(Set<SubjectParameterValue> subjectParameterValues) {
        this.subjectParameterValues = subjectParameterValues;
    }

    public Set<KeyWord> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(Set<KeyWord> keyWords) {
        this.keyWords = keyWords;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String keyWordsToString() {
        return Util.toString(keyWords);
    }

    @Override
    public String toString() {
        return name;
    }
}
