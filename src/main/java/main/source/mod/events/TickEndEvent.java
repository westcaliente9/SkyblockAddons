package cheaters.get.banned.events;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TickEndEvent extends Event {

    // An int will last 3.4 years we're fine ok
    private static int staticCount = 0;
    public int count;

    public TickEndEvent() {
        count = staticCount;
    }

    public boolean every(int ticks) {
        return count % ticks == 0;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            MinecraftForge.EVENT_BUS.post(new TickEndEvent());
            staticCount++;
        }
    }

}
