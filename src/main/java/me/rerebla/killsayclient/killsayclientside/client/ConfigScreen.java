package me.rerebla.killsayclient.killsayclientside.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ConfigScreen implements ModMenuApi {
    public static Config config = new Config();
    public Screen createConfig(Screen parent){
    ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(new LiteralText("KillSay Config"));

        builder.setSavingRunnable(() -> {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            String data = gson.toJson(config);
            String file = String.valueOf(FabricLoader.getInstance().getConfigDir().resolve("config_kill_say.json"));
            FileOutputStream fos = new FileOutputStream(file);
            IOUtils.write(data, fos, StandardCharsets.UTF_8);
            fos.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    });
    ConfigEntryBuilder entryBuilder = builder.entryBuilder();
    ConfigCategory general = builder.getOrCreateCategory(new LiteralText("General"));
        general.addEntry(entryBuilder.startIntField(new LiteralText("Chance to trigger"), config.chance)
            .setTooltip(new LiteralText("At which percentage the chat-event triggers"))
            .setDefaultValue(100)
            .setSaveConsumer(newVal -> {
                System.out.println(newVal);

                config.chance = newVal;
    }).build());
    general.addEntry(entryBuilder.startStrList(new LiteralText("Chat options"), config.chatOptions)
    .setTooltip(new LiteralText("The chat options. Format attacker and target like this: <<attacker>> <<target>>"))
    .setDefaultValue(new ArrayList<>())
    .setInsertInFront(false)
    .setSaveConsumer(newStrListVal ->{
        System.out.println(newStrListVal);
        config.chatOptions.clear();
        config.chatOptions.addAll(newStrListVal);
    }).build());
    general.addEntry(entryBuilder.startBooleanToggle(new LiteralText("Toggle"), config.generalControl)
    .setTooltip(new LiteralText("Toggle to dis/enable the mod completely"))
    .setDefaultValue(true)
    .setSaveConsumer(newBoolVal ->{
        System.out.println(newBoolVal);
        config.generalControl = newBoolVal;
    }).build());
        return builder.build();
    }
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return this::createConfig;
    }
    public static void loadConfig() {
        try {
            String file = String.valueOf(FabricLoader.getInstance().getConfigDir().resolve("config_kill_say.json"));
            FileInputStream fis = new FileInputStream(file);
            String dataStr = IOUtils.toString(fis, StandardCharsets.UTF_8);
            fis.close();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            config = gson.fromJson(dataStr, Config.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
