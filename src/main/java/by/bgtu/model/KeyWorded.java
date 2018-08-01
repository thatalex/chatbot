package by.bgtu.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Set;

public interface KeyWorded {

    Set<KeyWord> getKeyWords();

    @JsonIgnore
    /**
     * @return matched KeyWord
     */
    default KeyWord getKeyWord(String word, String... nextWords){
        for (KeyWord keyWord : getKeyWords()) {
            if (keyWord.isEquals(word, nextWords)) {
                return keyWord;
            }
        }
        return null;
    }
}
