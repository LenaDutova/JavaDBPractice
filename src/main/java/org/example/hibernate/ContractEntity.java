package org.example.hibernate;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "contract")
public class ContractEntity {

    @Id
    @GeneratedValue
    public int number;
    public Date start;

    @OneToOne
    @JoinColumn(name = "name_minion")
    public MinionEntity minion;

    @ManyToOne
    @JoinColumn(name = "name_villain")
    public VillainEntity villain;
}
