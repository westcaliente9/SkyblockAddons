package cheaters.get.banned.events;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Modified from Skytils under GPL-3.0
 * https://github.com/Skytils/SkytilsMod/blob/master/LICENSE
 */
public class BlockChangeEvent extends Event {

    public BlockPos position;
    public IBlockState oldBlock;
    public IBlockState newBlock;

    public BlockChangeEvent(BlockPos position, IBlockState oldBlock, IBlockState newBlock) {
        this.position = position;
        this.oldBlock = oldBlock;
        this.newBlock = newBlock;
    }

}
