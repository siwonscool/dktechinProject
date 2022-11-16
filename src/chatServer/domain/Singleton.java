package chatServer.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Singleton {
    private final Random random = new Random();

    WordsBasket wordsBasket;
    WordsMapBasket wordsMapBasket;
    WordsListBasket wordsListBasket;

    private Map<String, Integer> randomWordsMap = new HashMap<>();
    private ArrayList<String>[] randomWordsList = new ArrayList[MaxDataSize.MAX_DATA_SIZE.getValue() + 1];

    private Singleton(){
        wordsBasket = new WordsBasket();
        initializeRandomWordsList();
    }

    public void shuffleWords() {
        permutationRepetition(0,4);

        this.wordsMapBasket = new WordsMapBasket(randomWordsMap);
        this.wordsListBasket = new WordsListBasket(randomWordsList);
    }

    private static class LazyHolder{
        private static final Singleton instance = new Singleton();
    }

    public static Singleton getInstance(){
        return LazyHolder.instance;
    }

    private void initializeRandomWordsList(){
        for (int i = 0; i < randomWordsList.length; i++) {
            randomWordsList[i] = new ArrayList<>();
        }
    }

    // 중복 조합 53H4
    private void permutationRepetition(int cnt, int digits){
        // 4개를 선택했으므로, 결과를 담고 재귀를 종료한다.
        if (cnt == digits){
            //700만개 데이터만 담는다.
            if (randomWordsMap.size() == MaxDataSize.MAX_DATA_SIZE.getValue()){
                return;
            }
            int listIdx = random.nextInt(7000000) + 1;
            String resultStr = String.join("", wordsBasket.getResult());
            randomWordsMap.put(resultStr, listIdx);
            randomWordsList[listIdx].add(resultStr);
            return;
        }

        // 대상 집합을 words 배열을 순회하며 문자를 하나 선택한다.
        for (int i = 0; i < wordsBasket.getWords().length; i++){
            wordsBasket.getResult()[cnt] = wordsBasket.getWords()[i];
            // 중복 선택이 가능하므로, 인덱스를 그대로 넘겨주며 재귀 호출한다.
            permutationRepetition(cnt + 1, 4);
        }
    }
}
