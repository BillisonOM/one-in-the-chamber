package org.jefferies.oneinthechamber.board;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.jefferies.gamelibs.utils.board.GBoardAdapter;
import org.jefferies.oneinthechamber.OITCGamemode;
import org.jefferies.oneinthechamber.menu.ScoreboardMenu;
import org.jefferies.oneinthechamber.shooter.Shooter;
import org.jefferies.oneinthechamber.states.ActiveState;
import org.jefferies.oneinthechamber.states.WinnerState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OITCBoard extends GBoardAdapter {

    private OITCGamemode gamemode;

    @Override
    public String title(Player player) {
        return "&eHarpoon Chamber";
    }

    @Override
    public List<String> lines(Player player) {
        List<String> lines = new ArrayList<String>();
        lines.add("&7&m------------------------");
        if (gamemode.getState() instanceof ActiveState) {
            ActiveState state = (ActiveState) gamemode.getState();
            if (Shooter.getShooters().values().isEmpty()) {
                lines.add("&cNo shooters online!");
            } else {
                lines.add("&eTime&7: &f" + state.getTimeElapsed());
                lines.add("&eKills&7: &f" + Shooter.getShooters().get(player.getUniqueId()).getKills());
                lines.add("");
                lines.add("&eOnline Shooters&7: &f" + gamemode.getServer().getOnlinePlayers().stream().collect(Collectors.toList()).size());
            }
        } else if (gamemode.getState() instanceof WinnerState) {
            WinnerState state = (WinnerState) gamemode.getState();
            lines.add("&eWinner&7: &f" + state.getWinner().getName());
            lines.add("&eWinner Kills&7: &f" + state.getWinner().getKills());
            lines.add("");
            lines.add("&eYour Kills&7: &f" + Shooter.getShooters().get(player.getUniqueId()).getKills());
            lines.add("&eYour Position&7: &f" + getScoreboardPlacement(Shooter.getShooters().get(player.getUniqueId())));
        }
        lines.add("&7&m------------------------");
        return lines;
    }

    private int getScoreboardPlacement(Shooter shooter) {
        List<Shooter> shooters = new ArrayList<>(Shooter.getShooters().values());
        shooters.sort(Comparator.comparingInt(Shooter::getKills));
        Collections.reverse(shooters);
        int shown = 0;
        for (Shooter personOfInterest : shooters) {
            shown++;
            if (personOfInterest.getName().equalsIgnoreCase(shooter.getName())) {
                return shown;
            }
        }
        return shown;
    }
}
