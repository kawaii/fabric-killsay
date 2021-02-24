package me.rerebla.killsayclient.killsayclientside.client;

import io.github.prospector.modmenu.api.ModMenuApi;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class KillsayclientsideClient implements ClientModInitializer, ModMenuApi {

    public static KillsayclientsideClient instance;
    public ClientPlayerEntity player;

    @Override
    public void onInitializeClient() {
        if(instance == null){
            instance = this;
        }

        ClientTickEvents.START_CLIENT_TICK.register(callback ->{
            if(player != null && MinecraftClient.getInstance().player == null){return;}
            player = MinecraftClient.getInstance().player;
        });
        ConfigScreen.loadConfig();
    }

    public String randomString(){
        Random generator = new Random();
        int randomIndex = generator.nextInt(ConfigScreen.config.chatOptions.size());
        return ConfigScreen.config.chatOptions.get(randomIndex);
    }
}
