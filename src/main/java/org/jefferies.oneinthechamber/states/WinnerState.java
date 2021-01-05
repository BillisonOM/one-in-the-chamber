package org.jefferies.oneinthechamber.states;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import org.jefferies.gamelibs.utils.state.GameState;
import org.jefferies.oneinthechamber.OITCGamemode;
import org.jefferies.oneinthechamber.shooter.Shooter;

import static org.jefferies.gamelibs.utils.chat.Format.translate;

@Getter
@AllArgsConstructor
public class WinnerState extends GameState {

    private OITCGamemode gamemode;
    private Shooter winner;

    @Override
    public String getStateID() {
        return "winner";
    }

    @Override
    public void onEnable() {
        gamemode.getServer().broadcastMessage(translate("&e" + winner.getName() + " &fhas won this match!"));
        new BukkitRunnable() {
            @Override
            public void run() {
                gamemode.getServer().shutdown();
            }
        }.runTaskLater(gamemode, 400);
    }

    @Override
    public void onDisable() {

    }
}
