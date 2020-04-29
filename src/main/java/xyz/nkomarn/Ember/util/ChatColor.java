package xyz.nkomarn.Ember.util;

/**
 * Simple utility class to translate color codes just like in Bukkit/Bungee.
 * Borrowed from https://github.com/SpigotMC/BungeeCord/blob/master/chat/src/main/java/net/md_5/bungee/api/ChatColor.java.
 */
public class ChatColor {
    private static final char COLOR_CHAR = '\u00A7';
    private static final String ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";

    public static String translate(char altColorChar, String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == altColorChar && ALL_CODES.indexOf(b[i + 1]) > -1) {
                b[i] = ChatColor.COLOR_CHAR;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }
}
