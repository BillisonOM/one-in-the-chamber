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

import static org.jefferies.gamelibs.utils.chat.Format.translate;

public class Shooter {

    @Getter
    private static Map<UUID, Shooter> shooters = new HashMap();

    private UUID uuid;
    private int bulletsInChamber;
    private String name;
    private int kills;

    public Shooter(Player player) {
        uuid = player.getUniqueId();
        name = player.getName();
        bulletsInChamber = 4;
        shooters.put(uuid, this);
        kills = 0;
    }

    public void kill() {
        kills++;
        bulletsInChamber++;
        Bukkit.getPlayer(uuid).getInventory().setItem(0, OITCGamemode.getGamemode().getGunItem(this));
    }

    public int getKills() {
        return kills;
    }

    public boolean hasBulletInChamber() {
        return bulletsInChamber >= 1;
    }

    public String getName() {
        return name;
    }

    public void shoot(Player player) {
        bulletsInChamber -= 1;
        Arrow arrow = player.shootArrow();
        arrow.setVelocity(player.getLocation().getDirection().multiply(10));
        arrow.setTicksLived(20);
        arrow.setCustomNameVisible(true);
        arrow.setCustomName(translate("&cHarpoon"));
        arrow.setBounce(false);
        player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 3, 3);
        Bukkit.getPlayer(uuid).getInventory().setItem(0, OITCGamemode.getGamemode().getGunItem(this));
    }

    public void resetChamber() {
        bulletsInChamber = 4;
    }

    public int getBullets(){
        return bulletsInChamber;
    }
}
