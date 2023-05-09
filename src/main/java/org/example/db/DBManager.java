package org.example.db;

import org.example.dto.Minion;
import org.example.dto.Villain;
import org.example.dto.Villains;

import java.util.Set;

public interface DBManager {
    Set<Villains> getVillains ();
    Villain getVillain (String name);

    Set<Minion> getFreeMinions ();

    boolean addContract (Villain villain, Minion... minions);
    boolean removeContract (Villain villain, Minion... minions);
}
