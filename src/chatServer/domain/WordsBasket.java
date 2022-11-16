package chatServer.domain;

import java.util.Random;

public class WordsBasket {
    private final Random random = new Random();

    private String[] words  = new String[53];;
    private String[] result = new String[4];

    public WordsBasket(){
        initializeWords();
        shuffleWords();
    }

    private void initializeWords(){
        //words 배열 a-z, A-Z, 공백 초기화
        for (int i = 0; i < 52; i++) {
            if (i < 26){
                words[i] = (char)(97 + i) + "";
            }else {
                words[i] = (char)(65 + i - 26) + "";
            }
        }
        words[52]="";
    }

    private void shuffleWords(){
        for (int i = 0; i < words.length; i++) {
            int idx1 = random.nextInt(words.length);
            int idx2 = random.nextInt(words.length);

            String temp = words[idx1];
            words[idx1] = words[idx2];
            words[idx2] = temp;
        }
    }

    public String[] getWords(){
        return this.words;
    }

    public String[] getResult(){
        return this.result;
    }
}
