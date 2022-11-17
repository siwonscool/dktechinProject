package chatServer.vo;

import java.util.Map;

public class Answer4Vo {

    private final Map<String, Integer> answer4Map;
    private final int[] answer4Array;

    public Answer4Vo(Map<String, Integer> answer4Map, int[] answer4List) {
        this.answer4Map = answer4Map;
        this.answer4Array = answer4List;
    }

    public Map<String, Integer> getAnswer4Map() {
        return answer4Map;
    }

    public int[] getAnswer4Array() {
        return answer4Array;
    }
}
