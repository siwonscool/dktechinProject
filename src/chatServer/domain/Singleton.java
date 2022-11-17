package chatServer.domain;

import chatServer.enumeration.MaxDataSize;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Singleton {

    //ThreadPool 에서 Thread 를 관리하기 위해서 사용하는 ExecutorService 인터페이스
    public ExecutorService threadPool;
    private final Random random = new Random();

    WordsBasket wordsBasket;
    ResultMapBasket wordsMapBasket;
    ResultListBasket wordsListBasket;

    private final Map<String, Integer> randomWordsMap = new HashMap<>();
    private final ArrayList<String>[] randomWordsList = new ArrayList[
        MaxDataSize.MAX_DATA_SIZE.getValue() + 1];

    private Singleton() {
        threadPool = Executors.newCachedThreadPool();
        wordsBasket = new WordsBasket();
        initializeRandomWordsList();
    }

    private static class LazyHolder {

        private static final Singleton instance = new Singleton();
    }

    public static Singleton getInstance() {
        return LazyHolder.instance;
    }

    public void matchStringInt() {
        permutationRepetition(0, 4);
        this.wordsMapBasket = new ResultMapBasket(randomWordsMap);
        this.wordsListBasket = new ResultListBasket(randomWordsList);
    }

    private void initializeRandomWordsList() {
        for (int i = 0; i < randomWordsList.length; i++) {
            randomWordsList[i] = new ArrayList<>();
        }
    }

    // 중복 순열
    private void permutationRepetition(int cnt, int digits) {
        // 4개를 선택했으므로, 결과를 담고 재귀를 종료한다.
        if (cnt == digits) {
            //700만개 데이터만 담는다.
            if (randomWordsMap.size() == MaxDataSize.MAX_DATA_SIZE.getValue()) {
                return;
            }
            int listIdx = random.nextInt(7000000) + 1;
            String resultStr = String.join("", wordsBasket.getResult());
            //문제2를 위한 Map
            randomWordsMap.put(resultStr, listIdx);
            //문제3을 위한 Array
            randomWordsList[listIdx].add(resultStr);
            return;
        }

        // 대상 집합을 a-z, A-Z, "" 배열을 순회하며 문자를 하나 선택한다.
        for (int i = 0; i < wordsBasket.getWords().length; i++) {
            wordsBasket.getResult()[cnt] = wordsBasket.getWords()[i];
            // 중복 선택이 가능하므로, 인덱스를 그대로 넘겨주며 재귀 호출한다.
            permutationRepetition(cnt + 1, 4);
        }
    }
}
