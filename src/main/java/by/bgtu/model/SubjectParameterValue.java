package by.bgtu.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "subject_parameter_value")
public class SubjectParameterValue implements KeyWorded {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "subject_parameter_value_id")
    @JsonIgnore
    private Integer id;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
    @JsonIgnore
    private Subject subject;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "parameter_id")
    private Parameter parameter;

    @Column(name = "value")
    private String value;

    public SubjectParameterValue() {

    }

    public SubjectParameterValue(String name, String value, KeyWord[] keyWords) {
        this.parameter = new Parameter(name, keyWords);
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return parameter + " - " + value;
    }

    @JsonIgnore
    public Set<KeyWord> getKeyWords() {
        return parameter.getKeyWords();
    }

    @JsonIgnore
    public String getName() {
        return parameter.getName();
    }

    @Override
    @JsonIgnore
    public KeyWord getKeyWord(String word, String... nextWords) {
        return parameter.getKeyWord(word, nextWords);
    }


}
