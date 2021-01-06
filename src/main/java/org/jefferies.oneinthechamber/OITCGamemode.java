package org.jefferies.oneinthechamber;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jefferies.gamelibs.utils.config.Configuration;
import org.jefferies.gamelibs.utils.item.ItemBuilder;
import org.jefferies.gamelibs.utils.state.GameState;
import org.jefferies.oneinthechamber.board.OITCBoard;
import org.jefferies.oneinthechamber.command.AddGameSpawnCommand;
import org.jefferies.oneinthechamber.listener.OITCListener;
import org.jefferies.oneinthechamber.shooter.Shooter;
import org.jefferies.oneinthechamber.states.ActiveState;
import org.jefferies.oneinthechamber.states.WinnerState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class OITCGamemode extends JavaPlugin {

    @Getter
    private static OITCGamemode gamemode;

    private GameState state;

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
        setState(new ActiveState(this));
        new AddGameSpawnCommand(this);
        new OITCBoard(this);
    }

    public ItemStack getGunItem(Shooter shooter) {
        ItemStack stack = new ItemBuilder(Material.IRON_HOE, shooter.hasBulletInChamber() ? "&7Harpoon &8(&e" + shooter.getBullets() + " left&8)" : "&7Harpoon &8(&cEmpty&8)").addLoreLine(shooter.hasBulletInChamber() ? "&a" + shooter.getBullets() + " rounds left." : "&cNo rounds left.").buildItem();
        if (!shooter.hasBulletInChamber()) {
            stack.setDurability((short) 249);
        } else {
            if (shooter.getBullets() == 1) {
                stack.setDurability((short) 210);
            } else if (shooter.getBullets() == 2) {
                stack.setDurability((short) 170);
            } else if (shooter.getBullets() == 3) {
                stack.setDurability((short) 130);
            } else if (shooter.getBullets() == 4) {
                stack.setDurability((short) 90);
            } else if(shooter.getBullets() == 5){
                stack.setDurability((short) 50);
            } else {
                stack.setDurability((short) 0);
            }
        }
        return stack;
    }

    public Location getSpawnLocation() {
        return spawns.get(new Random().nextInt(spawns.size()));
    }

    public void setState(GameState state) {
        if (this.state != null) {
            this.state.onDisable();
        }
        this.state = state;
        state.onEnable();
    }
}
