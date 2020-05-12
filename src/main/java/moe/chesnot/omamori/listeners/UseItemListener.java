package moe.chesnot.omamori.listeners;

import moe.chesnot.omamori.Omamori;
import moe.chesnot.omamori.OmamoriManager;
import moe.chesnot.omamori.runnables.ExpireProtection;
import moe.chesnot.omamori.runnables.SetMobAware;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class UseItemListener implements Listener {

    private final OmamoriManager manager;
    private final Plugin plugin;

    public UseItemListener(Omamori plugin, OmamoriManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }
        Player player = event.getPlayer();
        if (manager.isOmamori(item)) {
            if (manager.isCursed(player)) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("[Omamori] You are cursed!"));
                event.setCancelled(true);
                return;
            }
            if (!manager.isProtected(player)) {
                manager.protectPlayer(player);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("[Omamori] You are under protection!"));
                event.setCancelled(true);
                item.addEnchantment(Enchantment.DURABILITY, 1);
                // only air has no item meta
                item.getItemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);
                // (new ExpireProtection(manager, player)).runTaskLater(plugin, 100);
                List<Entity> nearby = player.getNearbyEntities(32, 32, 32);
                for (Entity entity : nearby) {
                    if (entity instanceof Monster) {
                        Monster monster = (Monster) entity;
                        if(monster.getTarget() == null){
                            continue;
                        }
                        if(monster.getTarget().getEntityId() == player.getEntityId()){
                            monster.setTarget(null);
                        }
                    }
                }
                return;
            }
            item.removeEnchantment(Enchantment.DURABILITY);
            manager.expireProtection(player);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("[Omamori] Your protection is turned off"));
        }
    }
}
