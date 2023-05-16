package org.example.hibernate;

import org.example.dto.Villain;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "villain")
public class VillainEntity
        implements Parser<Villain> {

    @Id
    public String name;
    public String nickname;
    public short evilness;

//    @OneToMany (mappedBy = "villain")
//    private Set<ContractEntity> contracts = new HashSet<>();

    @OneToMany
    @JoinTable (
            name = "contract",
            joinColumns = @JoinColumn(name="name_villain"),
            inverseJoinColumns = @JoinColumn(name = "name_minion"))

    public Set<MinionEntity> minions = new HashSet<>();

    @Override
    public String toString() {
        String msg = getClass().getSimpleName() + " [" +
                "name = '" + name + "', " +
                "nickname='" + nickname + "', " +
                "evilness=" + evilness + "'";

        if (!minions.isEmpty()){
            msg += ", has " + minions.size() + " minions:";
            for (MinionEntity minion: minions) {
                msg += System.lineSeparator() + minion;
            }
        }

        msg += ']';
        return msg;
    }

    @Override
    public Villain parseTo() {
        Villain dto = new Villain(name, nickname, evilness);
        if (!minions.isEmpty()){
            for (MinionEntity entity : minions) {
                dto.addMinion(entity.parseTo());
            }
        }
        return dto;
    }
}
