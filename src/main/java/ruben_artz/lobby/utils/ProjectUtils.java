package ruben_artz.lobby.utils;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.particles.ParticleDisplay;
import com.cryptomorin.xseries.particles.Particles;
import com.cryptomorin.xseries.particles.XParticle;
import com.cryptomorin.xseries.profiles.builder.XSkull;
import com.cryptomorin.xseries.profiles.objects.Profileable;
import com.github.Anon8281.universalScheduler.bukkitScheduler.BukkitScheduler;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import ruben_artz.lobby.Lobby;
import ruben_artz.lobby.launch.Launcher;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class ProjectUtils {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    public static String setPlaceholders(OfflinePlayer player, String text) {
        if (isPluginEnabled("PlaceholderAPI")) {
            return PlaceholderAPI.setPlaceholders(player, text);
        }
        return text;
    }

    public static boolean isPluginEnabled(String s) {
        return Bukkit.getPluginManager().getPlugin(s) != null && Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(s)).isEnabled();
    }

    public static String placeholderReplace(final Player player, String input) {
        if ((input == null) || (input.isEmpty())) return input;
        input = input
                .replace("{Player}", player.getName())
                .replace("{Uuid}", player.getUniqueId().toString())
                .replace("{Address}", Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress());
        return input;
    }

    public static void setItem(
            final Player player,
            final int slot,
            final String material,
            final String name,
            final List<String> lore
    ) {
        final XMaterial mat = XMaterial.valueOf(material);
        ItemStack item = mat.parseItem();
        ItemMeta meta = item != null ? item.getItemMeta() : null;

        if (meta != null) meta.setDisplayName(addColor.addColors(player, ProjectUtils.placeholderReplace(player, name)));

        lore.replaceAll(s -> addColor.addColors(player, ProjectUtils.placeholderReplace(player , s)));
        if (meta != null) meta.setLore(lore);

        if (item != null) item.setItemMeta(meta);

        player.getInventory().setItem(slot - 1, item);
    }

    public static void setSkullOwner(
            final Player player,
            final int slot,
            final String pathName,
            final List<String> pathLore
    ) {
        ItemStack item = XMaterial.PLAYER_HEAD.parseItem();
        SkullMeta skullMeta = (SkullMeta) (item != null ? item.getItemMeta() : null);

        if (skullMeta != null) skullMeta.setDisplayName(addColor.addColors(player, ProjectUtils.placeholderReplace(player, pathName)));

        pathLore.replaceAll(s -> addColor.addColors(player, ProjectUtils.placeholderReplace(player, s)));
        if (skullMeta != null) skullMeta.setLore(pathLore);

        if (item != null && skullMeta != null) item.setItemMeta(XSkull.of(skullMeta).profile(Profileable.of(player)).apply());

        player.getInventory().setItem(slot - 1, item);
    }

    public static void setBow(
            final Player player,
            final int slot,
            final String material,
            final String name,
            final List<String> lore
    ) {
        final ItemStack itemBow = XMaterial.valueOf(material).parseItem();
        final ItemMeta itemMetaBow = itemBow != null ? itemBow.getItemMeta() : null;

        lore.replaceAll(s -> addColor.addColors(player, ProjectUtils.placeholderReplace(player, s)));

        if (itemMetaBow != null) itemMetaBow.setLore(lore);

        if (itemMetaBow != null) itemMetaBow.setDisplayName(addColor.addColors(player, ProjectUtils.placeholderReplace(player, name)));

        try {
            if (itemMetaBow != null) itemMetaBow.setUnbreakable(true);
        } catch (NoSuchMethodError ignored) {}

        if (itemMetaBow != null) itemMetaBow.addEnchant(Enchantment.ARROW_INFINITE, 1, true);

        if (itemBow != null) itemBow.setItemMeta(itemMetaBow);

        player.getInventory().setItem(slot - 1, itemBow);
    }

    public static void setArrow(
            final Player player,
            final int slot,
            final String item,
            final String name
    ) {
        final ItemStack itemArrow = XMaterial.valueOf(item).parseItem();
        final ItemMeta itemMetaArrow = itemArrow != null ? itemArrow.getItemMeta() : null;

        if (itemMetaArrow != null) itemMetaArrow.setDisplayName(addColor.addColors(player, ProjectUtils.placeholderReplace(player, name)));

        if (itemArrow != null) itemArrow.setItemMeta(itemMetaArrow);

        player.getInventory().setItem(slot, itemArrow);
    }

    public static boolean getWorldsItems(final Player player, final List<String> worldList) {
        for (final String s : worldList) {
            if (Bukkit.getWorld(s) != null) {
                if (!player.getWorld().getName().equalsIgnoreCase(s)) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }

    public static void sendCommands(@NotNull Player player, @NotNull String typeCommand, @NotNull List<String> commands) {
        switch(typeCommand) {
            case "PLAYER": {
                commands.forEach(s -> Bukkit.dispatchCommand(player, placeholderReplace(player, s)));
                break;
            }
            case "CONSOLE": {
                commands.forEach(s ->  Bukkit.dispatchCommand(Bukkit.getConsoleSender(), placeholderReplace(player, s)));
                break;
            }
        }
    }

    public static void sendSound(final Player player, final boolean isEnabled, final String soundName) {
        if (isEnabled) {
            if (player != null) XSound.play(soundName, soundPlayer -> soundPlayer.forPlayers(player));
        }
    }

    public static void sendParticles(final Player player, Particle... particles) {
        Arrays.stream(particles).forEach(s -> runTaskTimerTick(() -> Particles.circle(2, 20, ParticleDisplay.of(XParticle.of(s)).withLocation(player.getLocation()))));
    }

    private static void runTaskTimerTick(final Runnable runnable) {
        BukkitScheduler bukkitScheduler = new BukkitScheduler(plugin);
        AtomicLong repeater = new AtomicLong(5);

        bukkitScheduler.runTaskTimer(() -> {

            runnable.run();
            repeater.addAndGet(-40L);
            if (repeater.get() < 0L) bukkitScheduler.cancelTasks();
        }, 0L, 7L);
    }

    public static void syncRunTask(Runnable runnable) {
        Launcher.getScheduler().runTask(runnable);
    }

    public static void runTaskLater(long delay, Runnable runnable) {
        Launcher.getScheduler().runTaskLater(runnable, delay);
    }

    public static boolean isVersion_1_9_To_1_21() {
        return Bukkit.getVersion().contains("1.9") ||
                Bukkit.getVersion().contains("1.10") ||
                Bukkit.getVersion().contains("1.11") ||
                Bukkit.getVersion().contains("1.12") ||
                Bukkit.getVersion().contains("1.13") ||
                Bukkit.getVersion().contains("1.14") ||
                Bukkit.getVersion().contains("1.15") ||
                Bukkit.getVersion().contains("1.16") ||
                Bukkit.getVersion().contains("1.17") ||
                Bukkit.getVersion().contains("1.18") ||
                Bukkit.getVersion().contains("1.19") ||
                Bukkit.getVersion().contains("1.20") ||
                Bukkit.getVersion().contains("1.21");
    }
}
