package com.mrzak34.thunderhack.modules;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.mrzak34.thunderhack.Thunderhack;
import com.mrzak34.thunderhack.events.Render2DEvent;
import com.mrzak34.thunderhack.events.Render3DEvent;
import com.mrzak34.thunderhack.modules.client.MainSettings;
import com.mrzak34.thunderhack.notification.Notification;
import com.mrzak34.thunderhack.setting.Bind;
import com.mrzak34.thunderhack.setting.Setting;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import com.mrzak34.thunderhack.notification.NotificationManager;

import java.util.Objects;

import static com.mrzak34.thunderhack.util.PlayerUtils.getPlayerPos;

public class Module extends Feature {

    private final String description;
    private final String eng_description;


    private final Category category;
    public Setting<Boolean> enabled = this.register(new Setting<>("Enabled", false));
    public Setting<String> displayName;
    public boolean hidden;
    public boolean settingopened;
    public Setting<Bind> bind = this.register(new Setting<>("Keybind",  new Bind(-1)));
    public Setting<Boolean> drawn = this.register(new Setting<>("Drawn", true));


    public Module(String name, String description, Category category) {
        super(name);
        this.displayName = this.register(new Setting<String>("DisplayName", name));
        this.description = description;
        this.category = category;
        this.eng_description = "no english_description";
    }


    public Module(String name, String description,String eng_description, Category category) {
        super(name);
        this.displayName = this.register(new Setting<String>("DisplayName", name));
        this.description = description;
        this.eng_description = eng_description;
        this.category = category;
    }

    public boolean isSetting(){
        return this.settingopened;
    }

    public void setSetting(Boolean a){
        this.settingopened = a;
    }


    public void render(int mouseX, int mouseY, float partialTicks) {

    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onToggle() {
    }

    public void onLoad() {
    }


    public void onTick() {
    }

    public void onLogin() {
    }

    public void onLogout() {
    }

    public void onUpdate() {
    }

    public void onRender2D(Render2DEvent event) {
    }

    public void onRender3D(Render3DEvent event) {
    }

    public void onUnload() {
    }

    public String getDisplayInfo() {
        return null;
    }

    public boolean isOn() {
        return this.enabled.getValue();
    }

    public boolean isOff() {
        return !this.enabled.getValue();
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.enable();
        } else {
            this.disable();
        }
    }

    public void enable() {
        this.enabled.setValue(true);
        this.onToggle();
        this.onEnable();

        if((Objects.equals(this.getDisplayName(), "ThunderGui") || (Objects.equals(this.getDisplayName(), "ClickGUI")))){
        }else {
            mc.world.playSound(getPlayerPos(), SoundEvents.BLOCK_NOTE_XYLOPHONE, SoundCategory.AMBIENT, 150.0f, 2.0F, true);
        }


        if((!Objects.equals(this.getDisplayName(), "ElytraSwap")  && (!Objects.equals(this.getDisplayName(), "ClickGui"))&& (!Objects.equals(this.getDisplayName(), "ThunderGui")) && (!Objects.equals(this.getDisplayName(), "Windows")))) {
            NotificationManager.publicity(this.getDisplayName()  + " was enabled!", 2, Notification.Type.ENABLED);
        }
        if (Thunderhack.moduleManager.getModuleByClass(MainSettings.class).notifyToggles.getValue()) {
            TextComponentString text = new TextComponentString(Thunderhack.commandManager.getClientMessage() + " " + ChatFormatting.GREEN + this.getDisplayName() + " toggled on.");
            Module.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(text, 1);
        }
        if (this.isOn()) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    public void disable() {
        MinecraftForge.EVENT_BUS.unregister(this);
        if(mc.player == null){
            return;
        }
        if((Objects.equals(this.getDisplayName(), "ThunderGui") || (Objects.equals(this.getDisplayName(), "ClickGUI")))){
        } else {
            mc.world.playSound(getPlayerPos(), SoundEvents.BLOCK_NOTE_XYLOPHONE, SoundCategory.AMBIENT, 150.0f, 1.0F, true);

        }

        this.enabled.setValue(false);
        if((!Objects.equals(this.getDisplayName(), "ElytraSwap") && (!Objects.equals(this.getDisplayName(), "ThunderGui")) && (!Objects.equals(this.getDisplayName(), "ClickGui"))  && (!Objects.equals(this.getDisplayName(), "Windows")))) {
            NotificationManager.publicity(this.getDisplayName() + " was disabled!", 2, Notification.Type.DISABLED);
        }
        if (Thunderhack.moduleManager.getModuleByClass(MainSettings.class).notifyToggles.getValue()) {
            TextComponentString text = new TextComponentString(Thunderhack.commandManager.getClientMessage() + " " + ChatFormatting.RED + this.getDisplayName() + " toggled off.");
            if(text != null) {
                Module.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(text, 1);
            }
        }
        this.onToggle();
        this.onDisable();
    }

    public void toggle() {
        this.setEnabled(!this.isEnabled());
    }

    public boolean isValidBind(String s){
        return s.length() < 2;
    }

    public String getDisplayName() {
        return this.displayName.getValue();
    }


    public String getDescription() {
        if(Thunderhack.moduleManager.getModuleByClass(MainSettings.class).language.getValue() == MainSettings.Language.RU) {
            return this.description;
        } else {
            if(!Objects.equals(eng_description, "english_description")) {
                return this.eng_description;
            } else {
                return this.description;
            }
        }
    }

    public boolean isDrawn() {
        return this.drawn.getValue();
    }

    public void setDrawn(boolean drawn) {
        this.drawn.setValue(drawn);
    }


    public Category getCategory() {
        return this.category;
    }

    public String getInfo() {
        return null;
    }

    public Bind getBind() {
        return this.bind.getValue();
    }

    public void setBind(int key) {
        this.bind.setValue(new Bind(key));
    }

    public boolean listening() {
        return  this.isOn();
    }

    public String getFullArrayString() {
        return this.getDisplayName() + ChatFormatting.GRAY + (this.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + this.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
    }


    public enum Category {
        COMBAT("Combat"),
        MISC("Misc"),
        RENDER("Render"),
        MOVEMENT("Movement"),
        PLAYER("Player"),
        CLIENT("Client"),
        HUD("HUD");

        private final String name;

        Category(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

