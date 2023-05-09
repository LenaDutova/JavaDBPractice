package org.example.dto;

import org.example.dto.exceptions.NullOrEmptyException;

import java.util.HashSet;
import java.util.Set;

public class Villain
        extends Villains{

    private String nickname;
    private int evilness;

    private Set<Minion> minions = null;

    public Villain(String name, String nickname, int evilness) {
        super(name);
        this.setNickname (nickname);
        this.setEvilness (evilness);
    }

    // region // setters & getters

    public void setNickname(String nickname) {
        if (NullOrEmptyException.checkString(nickname)) this.nickname = nickname;
        else throw new NullOrEmptyException(this, "nickname");
    }

    public void setEvilness(int evilness) {
        if (evilness < 0) throw new IllegalArgumentException(getClass().getSimpleName() + ": Negative value of evilness");
        else this.evilness = evilness;
    }

    public void setAllMinions(Set<Minion> minions) {
        if (minions != null && !minions.isEmpty()) this.minions = minions;
    }

    public void addMinion(Minion minion) {
        if (minion!= null){
            if (minions == null || minions.isEmpty()) this.minions = new HashSet<Minion>();
            this.minions.add(minion);
        }
    }

    public void removeMinion(Minion minion) {
        if (minion!= null){
            if (minions != null && !minions.isEmpty()) this.minions.remove(minion);
        }
    }

    public String getNickname() {
        return nickname;
    }

    public int getEvilness() {
        return evilness;
    }

    public Set<Minion> getMinions() {
        return minions;
    }

    // endregion

    @Override
    public String toString() {
        String msg = getClass().getSimpleName() + " [" +
                "name = '" + name + '\'' +
                ", nickname = '" + nickname + '\'' +
                ", evilness = " + evilness;

        if (minions != null && !minions.isEmpty()){
            msg += ", has " + minions.size() + " minions:";
            for (Minion minion: minions) {
                msg += System.lineSeparator() + minion;
            }
        }

        msg += ']';
        return msg;
    }
}
