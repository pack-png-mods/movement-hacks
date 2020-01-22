package net.earthcomputer.movementhacks.mixin;

import net.earthcomputer.movementhacks.IPlayer;
import net.minecraft.src.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Entity.class)
public class MixinEntity {

    @ModifyVariable(method = "func_351_a", ordinal = 2, at = @At("HEAD"))
    private float modifyVelocity(float oldVelocity) {
        if (this instanceof IPlayer) {
            IPlayer player = (IPlayer) this;
            if (player.isFlying())
                return 0.5f;
            else if (player.isSprinting())
                return oldVelocity * 1.5f;
        }
        return oldVelocity;
    }

}
