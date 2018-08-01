package by.bgtu.service;

import by.bgtu.model.*;
import by.bgtu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("chatService")
public class ChatServiceImpl implements ChatService {

    private static final String NEW_LINE = System.lineSeparator();
    private static final int MAX_MATCH_COUNT = 3;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    /**
     * return answers matched by object's and it parameters keywords
     * @param question  single sentence containing question
     * @return answer and match rate
     */
    private Result getAnswerByParameters(String question) {
        if (question.trim().isEmpty()) return new Result();
        List<String> words = new ArrayList<>(Arrays.asList(question.split(Util.SPLIT_EXPRESION)));
        StringBuilder stringBuilder = new StringBuilder();

        List<Subject> subjects = subjectRepository.findAllByOrderByNameAsc();

        // find most matched subject by keywords
        List<Match<Subject>> matchSubjects = getMatched(words, subjects, 1, Integer.MAX_VALUE);

        // save rates of matched subjects to map
        Map<Subject, Integer> matchedSubjectRates = new HashMap<>();
        for (Match<Subject> matchSubject: matchSubjects){
            matchedSubjectRates.put(matchSubject.getObject(), matchSubject.getMatchRate());
        }

        List<Match<SubjectParameterValue>> matchParameters = new ArrayList<>();
        for (Match<Subject> matchSubject : matchSubjects) {
            List<String> paramWords = new ArrayList<>(words);
            // find most matched parameters of subject by keywords
            matchParameters.addAll(getMatched(paramWords, matchSubject.getObject().getSubjectParameterValues(), 1, Integer.MAX_VALUE));
        }
        Set<Subject> filteredSubjects = new HashSet<>();
        for (Match<SubjectParameterValue> matchParameter : matchParameters) {
            filteredSubjects.add(matchParameter.getObject().getSubject());
        }
        int matchRate = Integer.MIN_VALUE;
        // build answer and set max match rate
        for (Subject subject : filteredSubjects) {
            stringBuilder.append(subject.getName()).append(": ");
            for (Match<SubjectParameterValue> matchParameter : matchParameters) {
                if (subject.equals(matchParameter.getObject().getSubject())) {
                    SubjectParameterValue parameter = matchParameter.getObject();
                    stringBuilder.append(parameter.getName()).append(" ").append(parameter.getValue()).append(", ");
                    int rate = matchedSubjectRates.get(matchParameter.getObject().getSubject()) + matchParameter.getMatchRate();
                    if (matchRate < rate){
                        matchRate = rate;
                    }
                }
            }
            stringBuilder.setLength(stringBuilder.length() - 2);
            stringBuilder.append(NEW_LINE);
        }

        if (stringBuilder.length() > 2) {
            stringBuilder.setLength(stringBuilder.length() - 2);
        }
        return new Result(stringBuilder.toString(), matchRate);
    }


    /**
     * return answers matched by keywords
     * @param question  single sentence containing question
     * @return answer and match rate
     */
    private Result getAnswerByKeyWords(String question) {
        if (question.trim().isEmpty()) return new Result();
        List<String> words = new ArrayList<>(Arrays.asList(question.split(Util.SPLIT_EXPRESION)));
        List<Answer> answers = answerRepository.findAllByOrderByValueAsc();
        List<Match<Answer>> matches = getMatched(words, answers, 2, MAX_MATCH_COUNT);
        StringBuilder builder = new StringBuilder();
        int matchRate = 0;
        for (Match<Answer> match : matches) {
            if (matchRate == 0) {
                matchRate = match.getMatchRate();
            }
            builder.append(match.getObject().getValue()).append(NEW_LINE);
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 2);
        }

        return new Result(builder.toString(), matchRate);
    }


    @Override
    public String getAnswers(String question) {
        question = question.replace("ё", "е");
        List<String> sentences = new ArrayList<>(Arrays.asList(question.split(Util.SPLIT_SENTENCE)));
        StringBuilder stringBuilder = new StringBuilder();
        for (String sentence : sentences) {
            Result answerByParameters = getAnswerByParameters(sentence);
            Result answerByKeyWords = getAnswerByKeyWords(sentence);
            if (answerByParameters.matchRate > 0
                    && answerByParameters.matchRate >= answerByKeyWords.matchRate) {
                stringBuilder.append(answerByParameters.answers).append(NEW_LINE);
            } else {
                if (answerByKeyWords.matchRate > 0) {
                    stringBuilder.append(answerByKeyWords.answers);
                }
            }
        }
        if (stringBuilder.length() > 0) return stringBuilder.toString();
        return "Пожалуйста, уточните вопрос";
    }


    /**
     * return list of {@link KeyWorded} subjects  matched given words
     * @param wordList list of words for matching
     * @param subjects list of all known subjects implementing {@link KeyWorded}
     * @param minMatchKeywords minimum size of matched keywords
     * @param maxMatches maximum count of matched subjects with same rate
     * @param <T> any instance of KeyWorded
     * @return list of matched subjects
     */
    private <T extends KeyWorded> List<Match<T>> getMatched(List<String> wordList, Collection<T> subjects,
                                                            int minMatchKeywords, int maxMatches) {
        List<Match<T>> matches = new ArrayList<>();
        for (T subject : subjects) {
            Match<T> matchSubject = new Match<>(subject);
            String[] words = wordList.toArray(new String[0]);
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                int size = words.length - i - 1;
                String[] nextWords = new String[size];
                if (size > 0) {
                    System.arraycopy(words, i + 1, nextWords, 0, size);
                }
                KeyWord key = subject.getKeyWord(word, nextWords);
                if (key != null) {
                    matchSubject.addKeyWord(key);
                }
            }
            int minSize = Math.min(minMatchKeywords, matchSubject.getObject().getKeyWords().size());
            minSize = Math.max(minSize, 1);
            if (matchSubject.getKeyWords().size() >= minSize) {
                matches.add(matchSubject);
            }
        }

        List<Match<T>> result = getMatchedWithSameMatchRate(matches, maxMatches);
        for (Match<T> match : matches) {
            wordList.removeAll(match.getKeyWords());
        }
        return result;
    }

    /**
     * Find Match with maximum rate and remove other Matches with rate less than maximum
     * @param matches list of Matches
     * @param maxCount maximum count of Match with same rate
     * @return list of Match with maximum rate
     */
    private <T extends KeyWorded>  List<Match<T>> getMatchedWithSameMatchRate(List<Match<T>> matches, int maxCount) {
        List<Match<T>> result = new ArrayList<>();
        if (!matches.isEmpty()) {
            List<Match<T>> sortedMatches = new ArrayList<>(matches);
            Collections.sort(sortedMatches);
            Match<T> firstMatch = sortedMatches.get(0);
            int matchRate = firstMatch.getMatchRate();
            for (Match<T> match : sortedMatches) {
                if (match.getMatchRate() >= matchRate) {
                    result.add(match);
                } else break;
            }
        }
        if (result.size() > maxCount) {
            return result.subList(0, maxCount);
        } else {
            return result;
        }
    }

    private class Result {
        private int matchRate;
        private final String answers;

        private Result(String answers, int matchRate) {
            this.answers = answers;
            this.matchRate = matchRate;
        }

        private Result() {
            answers = "";
        }
    }

}
