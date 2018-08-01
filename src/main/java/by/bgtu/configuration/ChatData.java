package by.bgtu.configuration;

import by.bgtu.model.KeyWord;
import by.bgtu.model.Subject;
import by.bgtu.model.User;
import by.bgtu.repository.UserRepository;
import by.bgtu.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Predefined subjects
 */
public enum ChatData {
    ;
    public static List<Subject> getSubjects(){
        List<Subject> subjects = new ArrayList<>();
        subjects.add(Subject.newBuilder("терапевт")
                .addKeyword("терапевт", "врач-терапевт")
                .addKeyword("врач", "доктор")
                .addKeyword("Петров А.А", "Петров")
                .addParameter("время работы", "пн - пт, с 8.00 до 17.00",
                        new KeyWord("время", "график", "когда"),
                        new KeyWord("работа", "работ", "работает", "работы", "принимает", "прием"))
                .addParameter("место работы", "каб. 210",
                        new KeyWord("место", "где", "кабинет"),
                        new KeyWord("работа"))
                .build());
        return subjects;
    }
}
