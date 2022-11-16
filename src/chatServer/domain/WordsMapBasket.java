package chatServer.domain;

import chatServer.vo.Answer4Vo;
import java.nio.charset.StandardCharsets;
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

    public Answer4Vo findMatchingPrefix(String word){
        int L = 0,R = 1;
        Map<String,Integer> resultMap = new HashMap<>();
        int[] resultArray = new int[MaxDataSize.MAX_DATA_SIZE.getValue() + 1];

        while (L < word.length()){
            String prefix = word.substring(L,R);
            Integer idx = randomWordsMap.get(prefix);
            if (idx != null){
                resultMap.put(prefix,idx);
                resultArray[idx + 1]++;
                R++;
            }else {
                L++;
                R = L + 1;
            }
        }

        Answer4Vo answer4Vo = new Answer4Vo(resultMap,resultArray);

        return answer4Vo;
    }
}
