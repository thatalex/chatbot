package by.bgtu.service;

import by.bgtu.model.Answer;
import by.bgtu.model.KeyWord;
import by.bgtu.model.Subject;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface JSONService {

    /**
     * return parsed Subjects from JSON file
     * @throws IOException
     */
    Subject[] getSubjects(MultipartFile file) throws IOException;

    /**
     * return parsed Answers from JSON file
     * @throws IOException
     */
    Answer[] getAnswers(MultipartFile file) throws IOException;

    /**
     * return parsed KeyWords from JSON file
     * @throws IOException
     */
    KeyWord[] getKeyWords(MultipartFile file) throws IOException;

    /**
     * returm objects as resource
     * @param objects array of objects
     * @return {@link Resource}
     * @throws IOException
     */
    Resource getResource(Object[] objects) throws IOException;
}
