package org.jefferies.oneinthechamber.listener;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.jefferies.gamelibs.utils.item.ItemBuilder;
import org.jefferies.gamelibs.utils.listener.GListener;
import org.jefferies.oneinthechamber.OITCGamemode;
import org.jefferies.oneinthechamber.menu.ScoreboardMenu;
import org.jefferies.oneinthechamber.shooter.Shooter;
import org.jefferies.oneinthechamber.states.ActiveState;
import org.jefferies.oneinthechamber.states.WinnerState;

import static org.jefferies.gamelibs.utils.chat.Format.translate;

@AllArgsConstructor
public class OITCListener extends GListener {

    private OITCGamemode gamemode;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(translate("&8[&a+&8] &e" + event.getPlayer().getName()));
        Shooter shooter = null;
        if (!Shooter.getShooters().containsKey(event.getPlayer().getUniqueId())) {
            shooter = new Shooter(event.getPlayer());
        } else {
            shooter = Shooter.getShooters().get(event.getPlayer().getUniqueId());
            shooter.resetChamber();
        }
        event.getPlayer().getInventory().setArmorContents(new ItemStack[4]);
        event.getPlayer().getInventory().setContents(new ItemStack[36]);
        event.getPlayer().getInventory().setItem(0, gamemode.getGunItem(shooter));
        event.getPlayer().getInventory().setItem(8, new ItemBuilder(Material.PAPER, "&eScoreboard").buildItem());
        event.getPlayer().teleport(gamemode.getSpawnLocation());
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onShowScoreboard(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getType() != Material.PAPER) return;
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            new ScoreboardMenu(Shooter.getShooters().get(event.getPlayer().getUniqueId())).openMenu(event.getPlayer());
        }
    }

    @EventHandler
    public void onShoot(PlayerInteractEvent event) {
        if(gamemode.getState() instanceof WinnerState) return;
        if (event.getItem() == null) return;
        if (event.getItem().getType() != Material.IRON_HOE) return;
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Shooter shooter = Shooter.getShooters().get(event.getPlayer().getUniqueId());
            if (shooter.hasBulletInChamber()) {
                event.setCancelled(true);
                shooter.shoot(event.getPlayer());
            } else {
                event.setCancelled(true);
                event.getPlayer().sendMessage(translate("&cNo harpoons left in the chamber!"));
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onAttacked(EntityDamageByEntityEvent event) {
        if(gamemode.getState() instanceof ActiveState) {
            if (event.getEntity() instanceof Player) {
                if (event.getDamager() instanceof Player) {
                    Player damager = (Player) event.getDamager();
                    if (damager.getItemInHand().getType() != Material.IRON_HOE) {
                        event.setDamage(event.getDamage() * 2);
                    } else {
                        damager.sendMessage(translate("&cYou cannot attack players with this!"));
                        event.setCancelled(true);
                    }
                } else if (event.getDamager() instanceof Arrow) {
                    Arrow arrow = (Arrow) event.getDamager();
                    if (arrow.getShooter() instanceof Player) {
                        Player player = (Player) event.getEntity();
                        player.setHealth(0);
                    } else {
                        event.setCancelled(true);
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        event.getDrops().clear();
        event.setDroppedExp(0);
        Shooter.getShooters().get(event.getEntity().getUniqueId()).resetChamber();
        if (event.getEntity().getKiller() != null) {
            Shooter.getShooters().get(event.getEntity().getKiller().getUniqueId()).kill();
            event.getEntity().getKiller().sendMessage(translate("&aYou killed " + event.getEntity().getName()));
            event.getEntity().getLocation().getWorld().playSound(event.getEntity().getLocation(), Sound.CHICKEN_EGG_POP, 3, 3);
            event.getEntity().sendMessage(translate("&c" + event.getEntity().getKiller().getName() + " killed you."));
        } else {
            event.getEntity().sendMessage(translate("&cYou died"));
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(gamemode.getSpawnLocation());
        event.getPlayer().getInventory().setArmorContents(new ItemStack[4]);
        event.getPlayer().getInventory().setContents(new ItemStack[36]);
        event.getPlayer().getInventory().setItem(0, gamemode.getGunItem(Shooter.getShooters().get(event.getPlayer().getUniqueId())));
        event.getPlayer().getInventory().setItem(8, new ItemBuilder(Material.PAPER, "&eScoreboard").buildItem());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(translate("&8[&c-&8] &e" + event.getPlayer().getName()));
    }

}
