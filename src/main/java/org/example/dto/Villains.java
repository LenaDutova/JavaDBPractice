package org.example.dto;

import org.example.dto.exceptions.NullOrEmptyException;

import java.util.Objects;

public class Villains {

    protected String name;

    public Villains(String name) {
        this.setName(name);
    }

    // region // setters & getters

    public void setName(String name) {
        if (NullOrEmptyException.checkString(name)) this.name = name;
        else throw new NullOrEmptyException(this, "name");
    }

    public String getName() {
        return name;
    }

    // endregion

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [name = '" + name + "\']";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Villains villains = (Villains) o;
        return name.equals(villains.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
