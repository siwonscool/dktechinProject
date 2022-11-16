package chatServer.domain;

import java.util.ArrayList;

public class ResultListBasket {

    private final ArrayList<String>[] randomWordsList;

    public ResultListBasket(ArrayList<String>[] randomWordsList) {
        this.randomWordsList = randomWordsList;
    }

    public String findMatchingWords(int idx){
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < randomWordsList[idx].size(); i++) {
            if (i != randomWordsList[idx].size() - 1){
                result.append(randomWordsList[idx].get(i)+", ");
            }else{
                result.append(randomWordsList[idx].get(i));
            }
        }
        return result.toString();
    }

}
