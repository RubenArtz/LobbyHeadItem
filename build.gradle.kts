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

plugins {
    id("java")
    alias(libs.plugins.shadow)
    alias(libs.plugins.slimjar)
}

group = "artzstudio"
version = "2.1.21"

val slimJarBase = "artzstudio.dev.lobby.slimjar."
val libsBase = "artzstudio.dev.lobby.relocated."

registerOutputTask("Ruben_Artz", "F:/Ruben_Artz/Artz Studio/1.21.11/plugins")

repositories {
    mavenCentral()

    maven("https://jitpack.io")
    maven("https://libraries.minecraft.net")
    maven("https://repo.tcoded.com/releases")
    maven("https://repository.rubenmatias.com/releases")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")

}

dependencies {
    compileOnly(libs.spigotmc)
    compileOnly(libs.authlib)
    compileOnly(libs.placeholderapi)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    implementation(libs.slimjarRuntime)
    implementation(libs.slimjarHelperSpigot)

    slim(libs.bstats)
    slim(libs.xseries)
    slim(libs.foliaLib)
    slim(libs.boostedYaml)
}

tasks {
    shadowJar {
        archiveFileName.set("Lobby Head Item.jar")

        relocate("io.github.slimjar", "${libsBase}slimjar")

        exclude("com/cryptomorin/xseries/abstractions/**")
        exclude("com/cryptomorin/xseries/messages/**")

        exclude("com/cryptomorin/xseries/NoteBlockMusic*")
        exclude("com/cryptomorin/xseries/XBiome*")
        exclude("com/cryptomorin/xseries/XBlock*")
        exclude("com/cryptomorin/xseries/XEnchantment*")
        exclude("com/cryptomorin/xseries/XEntity*")
        exclude("com/cryptomorin/xseries/XEntityType*")
        exclude("com/cryptomorin/xseries/XItemStack*")
        exclude("com/cryptomorin/xseries/XPotion*")
        exclude("com/cryptomorin/xseries/XTag*")
        exclude("com/cryptomorin/xseries/XWorldBorder*")
    }

    slimJar {
        relocate("com.tcoded.folialib", "${slimJarBase}folialib")
        relocate("com.cryptomorin.xseries", "${slimJarBase}xseries")
        relocate("org.bstats", "${slimJarBase}bstats")
        relocate("dev.dejvokep.boostedyaml", "${slimJarBase}boostedyaml")
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}

fun registerOutputTask(name: String, path: String) {
    if (!System.getProperty("os.name").lowercase().contains("windows")) {
        return
    }

    tasks.register<Copy>("build$name") {
        group = "build plugin"
        dependsOn(tasks.shadowJar)
        from(tasks.shadowJar.get().archiveFile)
        into(file(path))
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}