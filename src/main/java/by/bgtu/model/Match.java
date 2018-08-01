package by.bgtu.model;

import java.util.HashSet;
import java.util.Set;

public class Match<T extends KeyWorded> implements Comparable<Match<T>> {
    private final T object;
    private final Set<KeyWord> keyWords = new HashSet<>();

    public Match(T subject) {
        this.object = subject;
    }

    public T getObject() {
        return object;
    }

    public Set<KeyWord> getKeyWords() {
        return keyWords;
    }

    @Override
    public int compareTo(Match o) {
        if (o == null) return -1;
        double thisRate = getMatchRate();
        double otherRate = o.getMatchRate();
        return Double.compare(otherRate, thisRate);
    }

    public void addKeyWord(KeyWord key) {
        keyWords.add(key);
    }

    /**
     * return rate of match. Best matches has higher value, worst - equals to zero
     */
    public int getMatchRate() {
        return keyWords.size() * 1000  + (keyWords.size() * 100) / object.getKeyWords().size();
    }
}
