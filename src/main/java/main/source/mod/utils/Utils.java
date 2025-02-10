package cheaters.get.banned.utils;

import cheaters.get.banned.Shady;
import cheaters.get.banned.events.TickEndEvent;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.net.URI;
import java.util.List;
import java.util.*;
import java.util.regex.Pattern;

public class Utils {

    public static boolean inSkyBlock = false;
    public static boolean inDungeon = false;
    public static boolean forceSkyBlock = false;
    public static boolean forceDungeon = false;

    public static void log(Object content) {
        if((boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")) {
            System.out.println(content);
        }
    }

    public static int romanToInt(String s) {
        Map<Character, Integer> numerals = new HashMap<>();
        numerals.put('I', 1);
        numerals.put('V', 5);
        numerals.put('X', 10);
        numerals.put('L', 50);
        numerals.put('C', 100);
        numerals.put('D', 500);
        numerals.put('M', 1000);

        int result = 0;
        for(int i = 0; i < s.length(); i++) {
            int add = numerals.get(s.charAt(i));
            if(i < s.length() - 1) {
                int next = numerals.get(s.charAt(i + 1));
                if(next / add == 5 || next / add == 10) {
                    add = next - add;
                    i++;
                }
            }
            result = result + add;
        }

        return result;
    }

    public static String getLogo() {
        final ArrayList<String> logos = new ArrayList<>(Arrays.asList("logo-fsr", "logo-sbe", "logo-pride"));

        int month = Calendar.getInstance().get(Calendar.MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        // April Fools
        if(EstonianUtils.isEstoniaDay()) return "logo-estonian";
        // December 21-31 (Christmas)
        if(month == Calendar.DECEMBER && day > 20) return "logo-christmas";
        // October 26-31 (Halloween)
        if(month == Calendar.OCTOBER && day > 25) return "logo-halloween";
        // October 11 (National Coming Out Day)
        if(month == Calendar.OCTOBER && day == 11) return "logo-pride";

        // 80% Chance For Normal Logo
        if(Math.random() < 0.8) return "logo";

        return (String) ArrayUtils.getRandomItem(logos);
    }

    public static void openUrl(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch(Exception ignored) {}
    }

    public static String removeFormatting(String input) {
        return input.replaceAll("§[0-9a-fk-or]", "");
    }

    public static String getSkyBlockID(ItemStack item) {
        if(item != null) {
            NBTTagCompound extraAttributes = item.getSubCompound("ExtraAttributes", false);
            if(extraAttributes != null && extraAttributes.hasKey("id")) {
                return extraAttributes.getString("id");
            }
        }
        return "";
    }

    public static List<String> getLore(ItemStack item) {
        if(item != null) {
            return item.getTooltip(Shady.mc.thePlayer, false);
        } else {
            return null;
        }
    }

    public static String getInventoryName() {
        if(Shady.mc.thePlayer == null || Shady.mc.theWorld == null) return "null";
        return Shady.mc.thePlayer.openContainer.inventorySlots.get(0).inventory.getName();
    }

    public static String getGuiName(GuiScreen gui) {
        if(gui instanceof GuiChest) {
            return ((ContainerChest) ((GuiChest) gui).inventorySlots).getLowerChestInventory().getDisplayName().getUnformattedText();
        }
        return "";
    }

    public static void sendMessage(String message) {
        if(Shady.mc.thePlayer != null && Shady.mc.theWorld != null) {
            if(!message.contains("§")) {
                message = message.replace("&", "§");
            }
            Shady.mc.thePlayer.addChatMessage(new ChatComponentText(message));
        }
    }

    public static void sendMessageAsPlayer(String message) {
        Shady.mc.thePlayer.sendChatMessage(message);
    }

    public static void executeCommand(String command) {
        if(ClientCommandHandler.instance.executeCommand(Shady.mc.thePlayer, command) == 0) {
            sendMessageAsPlayer(command);
        }
    }

    public static void sendModMessage(String message) {
        if(message.contains("§")) {
            sendMessage("§" + FontUtils.getRainbowCode('7') + "ShadyAddons > §f" + message);
        } else {
            sendMessage("&" + FontUtils.getRainbowCode('7') + "ShadyAddons > &f" + message);
        }
    }

    private static final Pattern numberPattern = Pattern.compile("[^0-9.]");
    public static String removeAllExceptNumbersAndPeriods(String text) {
        return numberPattern.matcher(text).replaceAll("");
    }

    public static Color addAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static Color addAlphaPct(Color color, float alpha) {
        int alphaNum = Math.round(255 * alpha);
        return addAlpha(color, alphaNum);
    }

    public static boolean isInteractable(Block block) {
        return new ArrayList<>(Arrays.asList(
                Blocks.chest,
                Blocks.lever,
                Blocks.trapped_chest,
                Blocks.wooden_button,
                Blocks.stone_button,
                Blocks.skull
        )).contains(block);
    }

    public static void copyToClipboard(String text) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
    }

    @SafeVarargs
    public static <T> T firstNotNull(T...args) {
        for(T arg : args) {
            if(arg != null) {
                return arg;
            }
        }
        return null;
    }

    private int ticks = 0;
    @SubscribeEvent
    public void onTick(TickEndEvent event) {
        if(forceDungeon || forceSkyBlock) {
            if(forceSkyBlock) inSkyBlock = true;
            if(forceDungeon) inSkyBlock = true; inDungeon = true;
            return;
        }

        if(ticks % 20 == 0) {
            if(Shady.mc.thePlayer != null && Shady.mc.theWorld != null) {
                ScoreObjective scoreboardObj = Shady.mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
                if(scoreboardObj != null) {
                    inSkyBlock = removeFormatting(scoreboardObj.getDisplayName()).contains("SKYBLOCK");
                }

                inDungeon =
                        inSkyBlock &&
                        ScoreboardUtils.scoreboardContains("The Catacombs") &&
                        !ScoreboardUtils.scoreboardContains("Queue") ||
                        ScoreboardUtils.scoreboardContains("Dungeon Cleared:");
            }
            ticks = 0;
        }
        ticks++;
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        forceDungeon = false;
        forceSkyBlock = false;
    }

}
