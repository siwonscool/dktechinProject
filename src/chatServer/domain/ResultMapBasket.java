package chatServer.domain;

import chatServer.enumeration.MaxDataSize;
import chatServer.vo.Answer4Vo;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ResultMapBasket {

    private final Map<String, Integer> randomWordsMap;

    public ResultMapBasket(Map<String, Integer> wordsMap) {
        validateWordsCount(wordsMap);
        this.randomWordsMap = wordsMap;
    }

    private void validateWordsCount(Map<String, Integer> wordsMap) {
        if (wordsMap.size() > MaxDataSize.MAX_DATA_SIZE.getValue()) {
            throw new IllegalArgumentException("최대 문자열의 갯수를 초과하였습니다.");
        }
    }

    public int findMatchingNumber(String key) {
        if (randomWordsMap.get(key) == null) {
            return -1;
        } else {
            return randomWordsMap.get(key);
        }
    }

    public Answer4Vo findMatchingPrefix(String word) {
        int L = 0, R = 1;
        //결과 출력을위한 map
        Map<String, Integer> resultMap = new HashMap<>();
        //결과 출력을 위한 Array
        int[] resultArray = new int[MaxDataSize.MAX_DATA_SIZE.getValue() + 1];

        while (L < word.length()) {
            if (R == word.length() + 1) {
                L++;
                //공백을 부분문자열에 포함하지 않기위해 R 을 L+1로 초기화
                R = L + 1;
                continue;
            }
            String prefix = word.substring(L, R);
            Integer idx = randomWordsMap.get(prefix);
            //부분문자열이 존재하면 결과값에 저장하고 R 증가 존재하지 않으면 L 증가 및 R 초기화
            if (idx != null) {
                if (resultMap.get(prefix) == null) {
                    resultArray[idx]++;
                }
                resultMap.put(prefix, idx);
                R++;
            } else {
                L++;
                //공백을 부분문자열에 포함하지 않기위해 R 을 L+1로 초기화
                R = L + 1;
            }
        }

        return new Answer4Vo(resultMap, resultArray);
    }
}
