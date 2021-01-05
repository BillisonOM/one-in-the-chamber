package org.jefferies.oneinthechamber.shooter;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.jefferies.oneinthechamber.OITCGamemode;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Shooter {

    @Getter
    private static Map<UUID, Shooter> shooters = new HashMap();

    private UUID uuid;
    private boolean hasBulletInChamber;
    private String name;
    private int kills;

    public Shooter(Player player) {
        uuid = player.getUniqueId();
        name = player.getName();
        hasBulletInChamber = true;
        shooters.put(uuid, this);
        kills = 0;
    }

    public void kill() {
        kills++;
        if (!hasBulletInChamber) {
            hasBulletInChamber = true;
            Bukkit.getPlayer(uuid).getInventory().setItem(0, OITCGamemode.getGamemode().getGunItem(this));
        }
    }

    public int getKills() {
        return kills;
    }

    public boolean hasBulletInChamber() {
        return hasBulletInChamber;
    }

    public String getName() {
        return name;
    }

    public void shoot(Player player) {
        hasBulletInChamber = false;
        Arrow arrow = player.shootArrow();
        arrow.setVelocity(player.getLocation().getDirection().multiply(5));
        arrow.setTicksLived(20);
        player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 3, 3);
        Bukkit.getPlayer(uuid).getInventory().setItem(0, OITCGamemode.getGamemode().getGunItem(this));
    }

    public void resetChamber() {
        hasBulletInChamber = true;
    }
}
