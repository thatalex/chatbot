package by.bgtu.service;

import by.bgtu.model.Answer;
import by.bgtu.model.KeyWord;
import by.bgtu.model.Subject;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service("JSONService")
public class JSONServiceImpl implements JSONService {

    @Override
    public Subject[] getSubjects(MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Subject[] subjects;
        try (InputStream inputStream = file.getInputStream()) {
            subjects = objectMapper.readValue(inputStream, Subject[].class);
        }
        return subjects;
    }

    @Override
    public Answer[] getAnswers(MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Answer[] answers;
        try (InputStream inputStream = file.getInputStream()) {
            answers = objectMapper.readValue(inputStream, Answer[].class);
        }
        return answers;
    }

    @Override
    public KeyWord[] getKeyWords(MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        KeyWord[] keyWords;
        try (InputStream inputStream = file.getInputStream()) {
            keyWords = objectMapper.readValue(inputStream, KeyWord[].class);
        }
        return keyWords;
    }

    @Override
    public Resource getResource(Object[] objects) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes = objectMapper.writeValueAsBytes(objects);
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            return new InputStreamResource(inputStream);
        }
    }

}
