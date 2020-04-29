package xyz.nkomarn.Ember.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.TextComponent;
import xyz.nkomarn.Ember.Ember;
import xyz.nkomarn.Ember.util.ChatColor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ChatListener {
    public static Set<UUID> TOGGLED_PLAYERS = new HashSet<>();

    @Subscribe
    public void onChat(PlayerChatEvent event) {
        if (TOGGLED_PLAYERS.contains(event.getPlayer().getUniqueId()) && event.getPlayer().hasPermission("channel.staff")) {
            event.setResult(PlayerChatEvent.ChatResult.denied());
            sendStaffChatMessage(event.getPlayer(), event.getMessage());
        } else if (event.getMessage().startsWith("!")) {
            event.setResult(PlayerChatEvent.ChatResult.denied());
            if (event.getPlayer().hasPermission("channel.staff")) {
                sendStaffChatMessage(event.getPlayer(), event.getMessage().substring(1));
            }
        }
    }

    private void sendStaffChatMessage(Player player, String message) {
        Ember.getProxy().getAllPlayers().stream()
                .filter(onlinePlayer -> onlinePlayer.hasPermission("channel.staff"))
                .forEach(onlinePlayer -> onlinePlayer.sendMessage(TextComponent.of(ChatColor.translate('&', String.format(
                        "&8[&c%s&8] &c%s", player.getUsername(), message
                )))));
    }
}
