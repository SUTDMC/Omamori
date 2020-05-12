package moe.chesnot.omamori.runnables;

import moe.chesnot.omamori.OmamoriManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * A {@link BukkitRunnable} to be scheduled that when run, removes protection on the player.
 */
public class ExpireProtection extends BukkitRunnable {
    private final OmamoriManager manager;
    private final UUID player;

    public ExpireProtection(OmamoriManager manager, UUID player){
        this.manager = manager;
        this.player = player;
    }

    public ExpireProtection(OmamoriManager manager, Player player){
        this.manager = manager;
        this.player = player.getUniqueId();
    }

    @Override
    public void run() {
        this.manager.expireProtection(player);
        Player _player = Bukkit.getPlayer(player);
        if(_player == null){
            return;
        }
        _player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Your protection has expired"));
    }
}
