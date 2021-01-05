package org.jefferies.oneinthechamber.states;

import org.bukkit.scheduler.BukkitRunnable;
import org.jefferies.gamelibs.utils.date.DateUtils;
import org.jefferies.gamelibs.utils.duration.Duration;
import org.jefferies.gamelibs.utils.state.GameState;
import org.jefferies.oneinthechamber.OITCGamemode;
import org.jefferies.oneinthechamber.shooter.Shooter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.jefferies.gamelibs.utils.chat.Format.translate;

public class ActiveState extends GameState {

    private OITCGamemode gamemode;

    private long startedAt;
    private long endAt;

    public ActiveState(OITCGamemode gamemode) {
        this.gamemode = gamemode;
    }

    @Override
    public String getStateID() {
        return "active";
    }

    public String getTimeElapsed() {
        return DateUtils.formatTimeMillisFull(System.currentTimeMillis() - startedAt);
    }

    @Override
    public void onEnable() {
        startedAt = System.currentTimeMillis();
        endAt = startedAt + Duration.fromString("30m").getValue();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (System.currentTimeMillis() >= endAt) {
                    if (Shooter.getShooters().values().isEmpty()) {
                        gamemode.getServer().getConsoleSender().sendMessage(translate("&cThere was no action this match therefore we have reset the timer!"));
                        OITCGamemode.getGamemode().setState(new ActiveState(gamemode));
                    } else {
                        List<Shooter> shooters = new ArrayList<>(Shooter.getShooters().values());
                        shooters.sort(Comparator.comparingInt(Shooter::getKills));
                        Collections.reverse(shooters);
                        gamemode.setState(new WinnerState(gamemode, shooters.get(0)));
                    }
                    cancel();
                }
            }
        }.runTaskTimer(gamemode, 0, 20);
    }

    @Override
    public void onDisable() {

    }
}
