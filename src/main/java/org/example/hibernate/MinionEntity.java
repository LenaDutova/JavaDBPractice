package org.example.hibernate;

import org.example.dto.Minion;

import jakarta.persistence.*;

@Entity
@Table(name = "minion")
public class MinionEntity
        implements Parser<Minion>{

    @Id
    public String name;
    public String weakness;

    @Column (name = "eye_count")
    public short eyesCount;

//    @OneToOne (mappedBy = "minion")
//    public ContractEntity contract;

    @ManyToOne
    @JoinTable (
            name = "contract",
            joinColumns = @JoinColumn(name="name_minion"),
            inverseJoinColumns = @JoinColumn(name = "name_villain"))
    public VillainEntity villain;

    @Override
    public Minion parseTo() {
        return new Minion(name, weakness, eyesCount);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                " [name = '" + name + "', " +
                "weakness = '" + weakness + "', " +
                "eye_count=" + eyesCount +
                ((villain != null) ? ", has contract with '" + villain.name + "']" : "]");
    }
}
