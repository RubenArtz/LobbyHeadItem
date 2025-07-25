plugins {
    id ("java")
    id ("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "ruben_artz.lobby"
version = "2.0.21"

val lombokVer = "1.18.30"

registerOutputTask("Ruben_Artz", "D:\\Ruben_Artz\\STN Studios\\Development\\plugins")

repositories {
    mavenCentral()
    maven {
        name = ("spigotmc-repo")
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        name = ("sonatype")
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        name = ("jitpack.io")
        url = uri("https://jitpack.io")
    }

    maven { url = uri("https://libraries.minecraft.net") }
    maven { url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/") }
}

dependencies {
    compileOnly ("org.spigotmc:spigot-api:1.21.3-R0.1-SNAPSHOT")

    compileOnly ("org.projectlombok:lombok:$lombokVer")
    compileOnly ("com.mojang:authlib:1.5.25")
    compileOnly ("org.jetbrains:annotations:23.0.0")
    compileOnly ("me.clip:placeholderapi:2.11.6")
    compileOnly ("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")

    annotationProcessor ("org.projectlombok:lombok:$lombokVer")

    implementation ("org.bstats:bstats-bukkit:3.0.0")
    /*
    Keep up to date
    Url: https://github.com/CryptoMorin/XSeries/releases
     */
    implementation ("com.github.cryptomorin:XSeries:13.3.3")
    /*
    Keep up to date
    Url: https://github.com/Anon8281/UniversalScheduler
     */
    implementation ("com.github.Anon8281:UniversalScheduler:0.1.6")
}

tasks.shadowJar {
    archiveFileName.set("Lobby Head Item.jar")

    relocate ("org.bstats", "ruben_artz.lobby.libraries.bstats")
    relocate ("com.cryptomorin.xseries", "ruben_artz.lobby.libraries.xseries")
    relocate ("com.github.Anon8281.universalScheduler", "ruben_artz.lobby.libraries.universalScheduler")

    exclude ("com/cryptomorin/xseries/abstractions/**")
    exclude ("com/cryptomorin/xseries/messages/**")

    exclude ("com/cryptomorin/xseries/NoteBlockMusic*")
    exclude ("com/cryptomorin/xseries/XBiome*")
    exclude ("com/cryptomorin/xseries/XBlock*")
    exclude ("com/cryptomorin/xseries/XEnchantment*")
    exclude ("com/cryptomorin/xseries/XEntity*")
    exclude ("com/cryptomorin/xseries/XEntityType*")
    exclude ("com/cryptomorin/xseries/XItemStack*")
    exclude ("com/cryptomorin/xseries/XPotion*")
    exclude ("com/cryptomorin/xseries/XTag*")
    exclude ("com/cryptomorin/xseries/XWorldBorder*")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
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