package org.example.dto;

import org.example.dto.exceptions.NullOrEmptyException;

import java.util.Objects;

public class Minion {
    private static final int MIN_EYES_COUNT = 0;
    private static final int MAX_EYES_COUNT = 10;

    private String name;
    private String weakness;
    private int eyesCount;

    public Minion(String name, String weakness, int eyesCount) {
        this.setName(name);
        this.setWeakness(weakness);
        this.setEyesCount(eyesCount);
    }

    // region // setters & getters

    public void setName(String name) {
        if (NullOrEmptyException.checkString(name)) this.name = name;
        else throw new NullOrEmptyException(this, "name");
    }
    public void setWeakness(String weakness) {
        if (NullOrEmptyException.checkString(weakness)) this.weakness = weakness;
        else throw new NullOrEmptyException(this, "weakness");
    }
    public void setEyesCount(int eyesCount) {
        if (eyesCount >= MIN_EYES_COUNT && eyesCount <= MAX_EYES_COUNT) this.eyesCount = eyesCount;
        else throw new IllegalArgumentException(getClass().getSimpleName() + ": Number of eyes out of range from " + MIN_EYES_COUNT + " to " + MAX_EYES_COUNT);
    }

    public String getName() {
        return name;
    }

    public String getWeakness() {
        return weakness;
    }

    public int getEyesCount() {
        return eyesCount;
    }

    // endregion

    @Override
    public String toString() {
        String msg = getClass().getSimpleName() + " [" +
                "name = '" + name + '\'' +
                ", eyesCount = " + eyesCount + '\'' +
                ", weakness = '" + weakness + '\'' + "]";
        return msg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Minion minion = (Minion) o;
        return name.equals(minion.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
