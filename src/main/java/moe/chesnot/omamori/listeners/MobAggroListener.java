package moe.chesnot.omamori.listeners;

import moe.chesnot.omamori.OmamoriManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class MobAggroListener implements Listener {
    private final OmamoriManager manager;

    public MobAggroListener(OmamoriManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent event) {
        if (!(event.getEntity() instanceof Monster)) {
            return;
        }
        Entity target = event.getTarget();
        if (!(target instanceof Player)) {
            return;
        }
        // karma
        if(event.getReason() == EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY
        || event.getReason() == EntityTargetEvent.TargetReason.TARGET_ATTACKED_NEARBY_ENTITY
        || event.getReason() == EntityTargetEvent.TargetReason.TARGET_ATTACKED_OWNER){
            return;
        }
        // check if it is a protected player
        Player player = (Player) target;
        if (manager.isProtected(player)){
            event.setCancelled(true);
            event.setTarget(null);
            Monster monster = (Monster) event.getEntity();
            monster.setTarget(null);
        }
    }
}
