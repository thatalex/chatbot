package by.bgtu.service;

import by.bgtu.model.*;

import java.util.List;

public interface AdminService {
    Answer getAnswer(Integer id);

    Subject save(Subject subject);

    List<Subject> getSubjects();

    Subject getSubject(Integer id);

    void deleteSubject(Integer id);

    void deleteAnswer(Integer id);

    void save(Answer value);

    List<Answer> getAnswers();

    List<KeyWord> getKeyWords();

    List<SubjectParameterValue> getParametersValues();

    void deleteKeyWord(Integer id);

    void save(KeyWord keyWord);

    KeyWord getKeyWord(Integer id);

    List<Parameter> getParameters();

    Parameter getParameter(Integer id);

    void deleteParameter(Integer id);

    void save(Parameter parameter);

    void save(SubjectParameterValue param_value);

    void deleteSubjectParameterValue(Integer id);

    KeyWord getKeyWord(String value);

    Subject getSubject(String name);
}
