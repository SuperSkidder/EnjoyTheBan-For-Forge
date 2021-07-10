package buzz.gaoyusense.injection.mixins;

import com.enjoytheban.api.EventBus;
import com.enjoytheban.api.events.misc.EventInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ContainerPlayer.class)
public class MixinContainerPlayer {
    @Inject(method = "onContainerClosed", at = @At("HEAD"), cancellable = true)
    public void onContainerClosed(EntityPlayer playerIn, CallbackInfo ci) {
        EventInventory event = new EventInventory(playerIn);
        EventBus.getInstance().call(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
