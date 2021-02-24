package me.rerebla.killsayclient.killsayclientside.mixin;

import me.rerebla.killsayclient.killsayclientside.client.Config;
import me.rerebla.killsayclient.killsayclientside.client.ConfigScreen;
import me.rerebla.killsayclient.killsayclientside.client.KillsayclientsideClient;
import net.minecraft.client.gui.hud.ChatHudListener;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Mixin(ChatHudListener.class)
public class chatMessageMixin {
    @Inject(method = "onChatMessage", at = @At(value = "HEAD",
            target = "Lnet/minecraft/client/gui/hud/ChatHudListener;onChatMessage(Lnet/minecraft/network/MessageType;Lnet/minecraft/text/Text;Ljava/util/UUID;)V"))
    private void onChatMessage(MessageType messageType, Text message, UUID senderUuid, CallbackInfo info){
        Config config = ConfigScreen.config;
        if(!config.generalControl || config.chatOptions.isEmpty() || config.chatOptions.indexOf("") == 0)return;
        if(new Random().nextInt(100)>= config.chance)return;
        Pattern regexPatternKey = Pattern.compile("key='([^']*)'");
        Matcher regexMatcherKey = regexPatternKey.matcher(String.valueOf(message));
        regexMatcherKey.find();
        if(!(regexMatcherKey.group(1).equals("death.attack.player")))return;
        Pattern regexPatternAttackerTarget = Pattern.compile("text='([^']*)'");
        Matcher regexMatcherAttackerTarget = regexPatternAttackerTarget.matcher(String.valueOf(message));
        regexMatcherAttackerTarget.find();
        String target = regexMatcherAttackerTarget.group(1);
        regexMatcherAttackerTarget.find();
        String attacker = regexMatcherAttackerTarget.group(1);
        if(target.equals(KillsayclientsideClient.instance.player.getEntityName())){return;}
        printMessage(target, attacker);
    }

    private void printMessage(String target, String attacker){
        String message = KillsayclientsideClient.instance.randomString();
        try {
            message = message.replace("<<target>>", target);
        }catch(Exception ignored){}
        try{
            message = message.replace("<<attacker>>", attacker);
        }catch(Exception ignored){}
        KillsayclientsideClient.instance.player.sendChatMessage(message);
    }
}
