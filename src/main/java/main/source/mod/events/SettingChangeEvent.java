package cheaters.get.banned.events;

import cheaters.get.banned.gui.config.settings.Setting;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SettingChangeEvent extends Event {

    public Setting setting;
    public Object oldValue;
    public Object newValue;

    public SettingChangeEvent(Setting setting, Object oldValue, Object newValue) {
        this.setting = setting;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

}
