package buzz.gaoyusense.injection.mixins;

import com.enjoytheban.api.EventBus;
import com.enjoytheban.api.events.misc.EventCollideWithBlock;
import com.enjoytheban.utils.Helper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Block.class)
public abstract class MixinBlock {

//    @Shadow
//    public abstract AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)

//    @Inject(method = "addCollisionBoxesToList",at = @At("HEAD"))
//    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity, CallbackInfo ci) {
//        if (collidingEntity == Helper.mc.thePlayer) {
//            EventCollideWithBlock e = EventBus.getInstance().call(new EventCollideWithBlock(this, pos, axisalignedbb));
//            axisalignedbb = e.getBoundingBox();
//        }
//    }
}
