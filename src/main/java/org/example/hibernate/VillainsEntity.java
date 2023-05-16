package org.example.hibernate;

import org.example.dto.Villains;

import jakarta.persistence.*;

@Entity
@Table(name = "villain")
@NamedQuery(name= "find_all", query="from VillainsEntity e")
public class VillainsEntity
        implements Parser<Villains> {

    @Id
    public String name;

    @Override
    public Villains parseTo() {
        return new Villains(name);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [name = '" + name + "\']";
    }
}
