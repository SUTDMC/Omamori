package moe.chesnot.omamori.runnables;

import org.bukkit.entity.Mob;
import org.bukkit.scheduler.BukkitRunnable;

public class SetMobAware extends BukkitRunnable {
    private final Mob mob;

    public SetMobAware(Mob mob) {
        this.mob = mob;
    }

    @Override
    public void run() {
        mob.setAware(true);
    }
}
