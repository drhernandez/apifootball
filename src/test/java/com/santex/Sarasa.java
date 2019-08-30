package com.santex;

import com.santex.models.entities.Player;
import com.santex.models.entities.Team;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Sarasa {

    @Test
    public void test() {

        Team team1 = new Team();
        team1.setId(1L);
        team1.setName("team1");

        Team team2 = new Team();
        team2.setId(2L);
        team2.setName("team2");

        Team team3 = new Team();
        team3.setId(1L);
        team3.setName("team3");

        Player player = new Player();
        player.setId(1L);
        team1.addPlayer(player);
        team2.addPlayer(player);
        team3.addPlayer(player);

        assertEquals(player.getTeams().size(), 2);
    }

}
