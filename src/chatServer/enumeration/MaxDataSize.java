package chatServer.enumeration;

public enum MaxDataSize {
    MAX_DATA_SIZE(7000000);

    private int value;

    MaxDataSize(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
