package net.earthcomputer.movementhacks.mixin;

import net.earthcomputer.movementhacks.IPlayer;
import net.minecraft.src.EntityLiving;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLiving.class)
public class MixinEntityLiving {

    @Inject(method = "fall", at = @At("HEAD"), cancellable = true)
    private void onFall(CallbackInfo ci) {
        if (this instanceof IPlayer && ((IPlayer) this).isImmuneToFallDamage())
            ci.cancel();
    }

    // note this constant is different from 0.02f, so it doesn't affect stuff elsewhere
    @ModifyConstant(method = "func_435_b", constant = {@Constant(doubleValue = 0.02), @Constant(doubleValue = 0.08)})
    private double cancelGravity(double prevVal) {
        if (this instanceof IPlayer && ((IPlayer) this).isFlying())
            return 0;
        return prevVal;
    }

}
