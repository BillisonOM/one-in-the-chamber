package org.jefferies.oneinthechamber.board;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.jefferies.gamelibs.utils.board.GBoardAdapter;
import org.jefferies.oneinthechamber.OITCGamemode;
import org.jefferies.oneinthechamber.shooter.Shooter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        if (Shooter.getShooters().values().isEmpty()) {
            lines.add("&cNo shooters online!");
        } else {
            List<Shooter> shooters = new ArrayList<>(Shooter.getShooters().values());
            shooters.sort(Comparator.comparingInt(Shooter::getKills));
            Collections.reverse(shooters);
            int shown = 0;
            for (Shooter shooter : shooters) {
                if (shown == 10) continue;
                shown++;
                lines.add("&8#" + shown + " &f" + shooter.getName() + " &8\u00BB &e" + shooter.getKills());
            }
        }
        lines.add("&7&m------------------------");
        return lines;
    }
}
