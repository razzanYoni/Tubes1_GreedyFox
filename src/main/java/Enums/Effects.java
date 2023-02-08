package Enums;

public enum Effects {
    Afterburner(1),
    AsteroidField(2),
    GasCloud(4),
    Superfood(8),
    Shield(16);

    public final Integer value;

    private Effects(Integer value) {
        this.value = value;
    }
}
