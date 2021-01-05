package org.jefferies.oneinthechamber.menu;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jefferies.gamelibs.utils.item.ItemBuilder;
import org.jefferies.gamelibs.utils.menu.Button;
import org.jefferies.gamelibs.utils.menu.pagination.PaginatedMenu;
import org.jefferies.oneinthechamber.shooter.Shooter;

import java.util.*;

import static org.jefferies.gamelibs.utils.chat.Format.translate;

@AllArgsConstructor
public class ScoreboardMenu extends PaginatedMenu {

    private Shooter open;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Scoreboard";
    }

    @Override
    public void onOpen(Player player) {
        player.sendMessage(translate("&eLoading scoreboards...."));
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        List<Shooter> shooters = new ArrayList<>(Shooter.getShooters().values());
        shooters.sort(Comparator.comparingInt(Shooter::getKills));
        Collections.reverse(shooters);
        int shown = 0;
        for (Shooter shooter : shooters) {
            shown++;
            buttons.put(buttons.size(), new ShooterButton(shown, shooter));
        }
        return buttons;
    }

    @AllArgsConstructor
    public class ShooterButton extends Button {
        private int placement;
        private Shooter shooter;
        @Override
        public ItemStack getButtonItem(Player player) {
            ItemBuilder builder = new ItemBuilder(Material.SKULL_ITEM, "&8#" + placement +  " &e" + shooter.getName()).setHeadOwner(shooter.getName());
            if(shooter.getName().equalsIgnoreCase(open.getName())){
                builder.addFlag(ItemFlag.HIDE_ENCHANTS);
                builder.addEnchantment(Enchantment.DURABILITY, 1);
            }
            builder.addLoreLine("&fKills &8\u00BB &e" + shooter.getKills());
            return builder.buildItem();
        }
    }
}
