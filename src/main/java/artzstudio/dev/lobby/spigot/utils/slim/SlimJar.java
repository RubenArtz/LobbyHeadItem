/*
 *     Copyright (C) 2026 Ruben_Artz (Artz Studio)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package artzstudio.dev.lobby.spigot.utils.slim;

import artzstudio.dev.lobby.spigot.Lobby;
import io.github.slimjar.app.builder.ApplicationBuilder;
import io.github.slimjar.app.builder.SpigotApplicationBuilder;
import io.github.slimjar.injector.loader.factory.InjectableFactory;
import io.github.slimjar.logging.ProcessLogger;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class SlimJar {
    private static final boolean DEBUG = Boolean.getBoolean("lobby.debug-slimjar");
    private static final boolean DISABLE_REMAPPER = Boolean.getBoolean("lobby.disable-remapper");

    private static final ReentrantLock lock = new ReentrantLock();
    private static final AtomicBoolean loaded = new AtomicBoolean();

    public static void load(Lobby plugin) {
        if (loaded.get()) return;
        lock.lock();

        try {
            if (loaded.getAndSet(true)) return;

            final var downloadPath = plugin.getDataFolder().getParentFile().toPath()
                    .resolve("Artz-Libraries")
                    .resolve(plugin.getName());

            ProcessLogger customLogger = new ProcessLogger() {
                @Override
                public void info(@NonNull String message, @Nullable Object... args) {
                    plugin.getLogger().info(message.formatted(args));
                }

                @Override
                public void error(@NonNull String message, @Nullable Object... args) {
                    plugin.getLogger().severe(message.formatted(args));
                }

                @Override
                public void debug(@NonNull String message, @Nullable Object... args) {
                    if (DEBUG) plugin.getLogger().info("[DEBUG] " + message.formatted(args));
                }
            };

            plugin.getLogger().info("Loading libraries...");

            try {
                new SpigotApplicationBuilder(plugin)
                        .logger(customLogger)
                        .downloadDirectoryPath(downloadPath)
                        .debug(DEBUG)
                        .remap(!DISABLE_REMAPPER)
                        .build();
            } catch (Throwable e) {
                try {
                    ApplicationBuilder.appending(plugin.getName())
                            .logger(customLogger) // Usamos el logger que creamos arriba
                            .injectableFactory(InjectableFactory.selecting(InjectableFactory.ERROR, InjectableFactory.INJECTABLE, InjectableFactory.WRAPPED, InjectableFactory.UNSAFE))
                            .downloadDirectoryPath(downloadPath)
                            .build();
                } catch (Throwable fallbackError) {
                    plugin.getLogger().severe("CRITICAL: Failed to download/load libraries via fallback!");

                    fallbackError.printStackTrace();
                }

                e.printStackTrace();
            }
            plugin.getLogger().info("Libraries loaded successfully!");
        } finally {
            lock.unlock();
        }
    }
}