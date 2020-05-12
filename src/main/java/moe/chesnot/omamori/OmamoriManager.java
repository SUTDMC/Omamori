package moe.chesnot.omamori;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OmamoriManager {

    public enum PlayerState {
        /**
         * The player is not protected, but is available to be protected.
         */
        UNPROTECTED,
        /**
         * The player is currently protected.
         */
        PROTECTED,
        /**
         * The player is cursed, and cannot be protected.
         */
        CURSED
    }

    private final Map<UUID, PlayerState> playerStates = new HashMap<>();

    /**
     * Attempts to protect the player. Returns true if successfully protected.
     *
     * @param player the player to protect
     * @return true if protected, false if the player is unable to be protected (i.e. cursed)
     */
    public boolean protectPlayer(Player player) {
        PlayerState playerState = playerStates.get(player.getUniqueId());
        if (playerState == PlayerState.PROTECTED) {
            return false;
        }
        if (playerState != PlayerState.CURSED) {
            playerStates.put(player.getUniqueId(), PlayerState.PROTECTED);
            return true;
        }
        // means the player is cursed
        return false;
    }

    public boolean isProtected(Player player) {
        PlayerState playerState = playerStates.get(player.getUniqueId());
        if (playerState == null) {
            return false;
        }
        return playerState == PlayerState.PROTECTED;
    }

    public boolean isCursed(Player player) {
        PlayerState playerState = playerStates.get(player.getUniqueId());
        if (playerState == null) {
            return false;
        }
        return playerState == PlayerState.CURSED;
    }

    /**
     * Removes the protection effects from the player.
     *
     * @param player
     * @return
     */
    public boolean expireProtection(Player player) {
        return expireProtection(player.getUniqueId());
    }

    public boolean expireProtection(UUID player) {
        PlayerState playerState = playerStates.get(player);
        if (playerState != PlayerState.PROTECTED) {
            return false;
        }
        playerStates.put(player, PlayerState.UNPROTECTED);
        return true;
    }

    public boolean expireCurse(Player player) {
        return expireCurse(player.getUniqueId());
    }

    public boolean expireCurse(UUID player) {
        PlayerState playerState = playerStates.get(player);
        if (playerState != PlayerState.CURSED) {
            return false;
        }
        playerStates.put(player, PlayerState.UNPROTECTED);
        return true;
    }

    /**
     * Checks if the item is an Omamori as configured
     *
     * @param item the itemstack to check
     */
    public boolean isOmamori(@NotNull ItemStack item) {
        return item.getType().equals(Material.CARROT_ON_A_STICK);
    }

    public String dump() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<UUID, PlayerState> item : playerStates.entrySet()) {
            Player player = Bukkit.getPlayer(item.getKey());
            if (player == null) {
                // player is offline
                continue;
            }
            String playerName = player.getDisplayName();
            builder.append(playerName);
            builder.append(": ");
            builder.append(item.getValue().name());
            builder.append(", ");
        }
        return builder.toString();
    }
}
