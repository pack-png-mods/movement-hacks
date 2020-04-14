package net.earthcomputer.movementhacks.mixin;

import net.earthcomputer.movementhacks.IPlayer;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP extends EntityPlayer implements IPlayer {

    @Shadow public MovementInput field_787_a;
    @Unique private boolean flying;
    @Unique private boolean sprinting;
    @Unique private boolean immuneToFallDamage;

    public MixinEntityPlayerSP(World world) {
        super(world);
    }

    @Override
    public boolean isFlying() {
        return flying;
    }

    @Override
    public void setFlying(boolean flying) {
        this.flying = flying;
        if (flying) {
            immuneToFallDamage = true;
        } else {
            // step sound counter
            ((EntityAccessor) this).setField_653_b(1);
            this.field_641_aF = 0;
        }
    }

    @Override
    public boolean isSprinting() {
        return sprinting;
    }

    @Override
    public void setSprinting(boolean sprinting) {
        this.sprinting = sprinting;
    }

    @Override
    public boolean isImmuneToFallDamage() {
        return immuneToFallDamage;
    }

    @Inject(method = "onLivingUpdate", at = @At("RETURN"))
    private void onTick(CallbackInfo ci) {
        if (flying) {
            if (field_787_a.field_1175_e ^ field_787_a.field_1176_d) {
                if (field_787_a.field_1176_d) // jumping
                    motionY = 0.5;
                else // sneaking
                    motionY = -0.5;
            } else {
                motionY *= 0.5;
            }
            motionX *= 0.5;
            motionZ *= 0.5;
        }
        if (onGround)
            flying = immuneToFallDamage = false;
    }

    @Inject(method = "writeEntityToNBT", at = @At("RETURN"))
    private void onWriteEntityToNBT(NBTTagCompound nbt, CallbackInfo ci) {
        nbt.setBoolean("Flying", flying);
    }

    @Inject(method = "readEntityFromNBT", at = @At("RETURN"))
    private void onReadEntityFromNBT(NBTTagCompound nbt, CallbackInfo ci) {
        setFlying(nbt.getBoolean("Flying"));
    }
}
