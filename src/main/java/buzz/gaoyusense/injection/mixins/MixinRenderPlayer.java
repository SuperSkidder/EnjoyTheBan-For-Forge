package buzz.gaoyusense.injection.mixins;

import com.enjoytheban.api.EventBus;
import com.enjoytheban.api.events.rendering.EventPostRenderPlayer;
import com.enjoytheban.api.events.rendering.EventPreRenderPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public class MixinRenderPlayer {


    @Inject(method = "doRender", at = @At("HEAD"))
    public void doRenderH(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        EventPreRenderPlayer event = new EventPreRenderPlayer();
        EventBus.getInstance().call(event);
    }

    @Inject(method = "doRender", at = @At("RETURN"))
    public void doRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        EventPostRenderPlayer event2 = new EventPostRenderPlayer();
        EventBus.getInstance().call(event2);
    }

}
