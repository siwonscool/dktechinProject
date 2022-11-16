package chatServer.domain;

import java.util.HashMap;
import java.util.Map;

public class WordsMapBasket {

    private final Map<String,Integer> randomWordsMap;

    public WordsMapBasket(Map<String,Integer> wordsMap){
        validateWordsCount(wordsMap);
        this.randomWordsMap = wordsMap;
    }

    private void validateWordsCount(Map<String,Integer> wordsMap) {
        if(wordsMap.size() > MaxDataSize.MAX_DATA_SIZE.getValue()){
            throw new IllegalArgumentException("최대 문자열의 갯수를 초과하였습니다.");
        }
    }

    public int findMatchingNumber(String key){
        return randomWordsMap.get(key);
    }

}
