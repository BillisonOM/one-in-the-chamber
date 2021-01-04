package org.jefferies.oneinthechamber;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jefferies.gamelibs.utils.config.Configuration;
import org.jefferies.gamelibs.utils.item.ItemBuilder;
import org.jefferies.oneinthechamber.board.OITCBoard;
import org.jefferies.oneinthechamber.command.AddGameSpawnCommand;
import org.jefferies.oneinthechamber.listener.OITCListener;
import org.jefferies.oneinthechamber.shooter.Shooter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class OITCGamemode extends JavaPlugin {

    @Getter
    private static OITCGamemode gamemode;

    private Configuration configuration;
    private List<Location> spawns;

    @Override
    public void onEnable() {
        gamemode = this;
        new OITCListener(this);
        configuration = new Configuration("spawns.yml", this);
        spawns = new ArrayList<>();
        for (String key : configuration.getConfig().getKeys(false)) {
            spawns.add(configuration.getLocation(key));
        }
        new AddGameSpawnCommand(this);
        new OITCBoard(this);
    }

    public ItemStack getGunItem(Shooter shooter) {
        ItemStack stack = new ItemBuilder(Material.IRON_HOE, shooter.hasBulletInChamber() ? "&7Harpoon" : "&7Harpoon &8(&cEmpty&8)").buildItem();
        if(!shooter.hasBulletInChamber()){
            stack.setDurability((short) 249);
        } else {
            stack.setDurability((short) 0);
        }
        return stack;
    }

    public Location getSpawnLocation() {
        return spawns.get(new Random().nextInt(spawns.size()));
    }
}
