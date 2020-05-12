package moe.chesnot.omamori;

import moe.chesnot.omamori.listeners.MobAggroListener;
import moe.chesnot.omamori.listeners.UseItemListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;

public final class Omamori extends JavaPlugin {

    private OmamoriManager manager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        PluginManager pm = getServer().getPluginManager();
        manager = new OmamoriManager();
        pm.registerEvents(new MobAggroListener(manager), this);
        pm.registerEvents(new UseItemListener(this, manager), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        HandlerList.unregisterAll(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equals("od")){
            sender.sendMessage(manager.dump());
            return true;
        }
        return false;
    }
}
