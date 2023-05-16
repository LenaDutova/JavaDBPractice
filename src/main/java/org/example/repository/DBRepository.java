package org.example.repository;

import org.example.dto.*;

import java.util.Set;

public interface DBRepository {

    Set<Villains> getVillains ();
    Villain getVillain (Villains dto);

    Set<Minion> getFreeMinions ();

    boolean addContract (Villain villain, Minion... minions);
    boolean removeContract (Villain villain, Minion... minions);
}
