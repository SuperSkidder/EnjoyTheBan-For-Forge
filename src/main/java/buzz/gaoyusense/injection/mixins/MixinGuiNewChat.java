package buzz.gaoyusense.injection.mixins;

import com.enjoytheban.api.EventBus;
import com.enjoytheban.api.events.misc.EventChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiNewChat.class)
public class MixinGuiNewChat {
    @Inject(method = "printChatMessageWithOptionalDeletion", at = @At("RETURN"),cancellable = true)
    public void printChatMessageWithOptionalDeletion(IChatComponent chatComponent, int chatLineId, CallbackInfo ci) {
        EventChat ec = new EventChat(chatComponent.getUnformattedText());
        EventBus.getInstance().call(ec);
    }
}
