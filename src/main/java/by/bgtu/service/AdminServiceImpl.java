package by.bgtu.service;

import by.bgtu.model.*;
import by.bgtu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Service("adminService")
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private KeyWordRepository keyWordRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ParameterRepository parameterRepository;

    @Autowired
    private ParameterValueRepository parameterValueRepository;

    @Override
    public Answer getAnswer(Integer id) {
        return answerRepository.findById(id);
    }

    @Override
    @Transactional
    public Subject save(Subject subject) {
        setId(subject);
        for (KeyWord keyWord : subject.getKeyWords()) {
            saveWords(keyWord);
            save(keyWord);
        }
        save(subject.getSubjectParameterValues());
        subjectRepository.save(subject);
        return subject;
    }

    private void save(Collection<SubjectParameterValue> values) {
        for (SubjectParameterValue value : values) {
            for (KeyWord keyWord: value.getKeyWords()) {
                saveWords(keyWord);
                save(keyWord);
            }
            setId(value.getParameter());
            parameterRepository.save(value.getParameter());
        }
    }

    @Override
    public List<Subject> getSubjects() {
        return subjectRepository.findAllByOrderByNameAsc();
    }

    @Override
    public Subject getSubject(Integer id) {
        return subjectRepository.findById(id);
    }

    @Override
    public void deleteSubject(Integer id) {
        subjectRepository.delete(id);
    }

    @Override
    public void deleteAnswer(Integer id) {
        answerRepository.deleteById(id);
    }

    @Override
    public void save(Answer value) {
        setId(value);
        for (KeyWord keyWord : value.getKeyWords()) {
            saveWords(keyWord);
            save(keyWord);
        }
        answerRepository.save(value);
    }

    @Override
    public List<Answer> getAnswers() {
        return answerRepository.findAllByOrderByValueAsc();
    }

    @Override
    public List<KeyWord> getKeyWords() {
        return keyWordRepository.findAllByOrderByValueAsc();
    }

    @Override
    public List<SubjectParameterValue> getParametersValues() {
        return parameterValueRepository.findAll();
    }

    @Override
    public void deleteKeyWord(Integer id) {
        KeyWord keyWord = keyWordRepository.findOne(id);
        if (keyWord != null){
            for (Subject subject: keyWord.getSubjects()){
                subject.getKeyWords().remove(keyWord);
            }
            for (Parameter parameter: keyWord.getParameters()){
                parameter.getKeyWords().remove(keyWord);
            }
        }
        keyWordRepository.delete(id);
    }

    @Override
    public void save(KeyWord keyWord) {
        if (keyWord.getWords().isEmpty()){
            KeyWord value = keyWordRepository.findByValue(keyWord.getValue());
            if (value != null){
                keyWord.set(value);
                return;
            }
        } else {
            saveWords(keyWord);
        }
        keyWordRepository.save(keyWord);
    }

    @Override
    public KeyWord getKeyWord(Integer id) {
        return keyWordRepository.getOne(id);
    }

    @Override
    public List<Parameter> getParameters() {
        return parameterRepository.findAllByOrderByNameAsc();
    }

    @Override
    public Parameter getParameter(Integer id) {
        return parameterRepository.getOne(id);
    }

    @Override
    @Transactional
    public void deleteParameter(Integer id) {
        Parameter parameter = parameterRepository.getOne(id);
        if (parameter != null){
            for (SubjectParameterValue value: parameter.getSubjectParameterValues()){
                value.setSubject(null);
                parameterValueRepository.delete(value);
            }
            parameterRepository.delete(parameter);
        }
    }

    @Override
    public void save(Parameter parameter) {
        setId(parameter);
        for (KeyWord keyWord : parameter.getKeyWords()) {
            saveWords(keyWord);
            save(keyWord);
        }
        parameterRepository.save(parameter);
    }

    @Override
    public void save(SubjectParameterValue param_value) {
        parameterValueRepository.save(param_value);
    }

    @Override
    public void deleteSubjectParameterValue(Integer id) {
        SubjectParameterValue value = parameterValueRepository.getOne(id);
        if (value != null){
            value.setSubject(null);
            value.setParameter(null);
            parameterValueRepository.delete(value);
        }
    }

    @Override
    public KeyWord getKeyWord(String value) {
        return keyWordRepository.findByValue(value);
    }

    @Override
    public Subject getSubject(String name) {
        return subjectRepository.findByName(name);
    }

    private void saveWords(KeyWord keyWord) {
        setId(keyWord);
        for (Word word : keyWord.getWords()) {
            setId(word);
            wordRepository.save(word);
        }
    }

    private void setId(Answer answer) {
        Answer value = answerRepository.findByValue(answer.getValue());
        if (value != null) {
            answer.setId(value.getId());
        }
    }

    private void setId(Subject subject) {
        Subject value = subjectRepository.findByName(subject.getName());
        if (value != null) {
            subject.setId(value.getId());
        }
    }

    private void setId(Parameter parameter) {
        Parameter value = parameterRepository.findByName(parameter.getName());
        if (value != null) {
            parameter.setId(value.getId());
        }

    }

    private void setId(Word word) {
        Word value = wordRepository.findByValue(word.getValue());
        if (value != null) {
            word.setId(value.getId());
        }

    }

    private void setId(KeyWord keyWord) {
        KeyWord value = keyWordRepository.findByValue(keyWord.getValue());
        if (value != null) {
            keyWord.setId(value.getId());
        }
    }

}
