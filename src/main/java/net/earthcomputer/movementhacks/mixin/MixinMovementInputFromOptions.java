package net.earthcomputer.movementhacks.mixin;

import net.earthcomputer.movementhacks.IPlayer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MovementInput;
import net.minecraft.src.MovementInputFromOptions;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MovementInputFromOptions.class)
public class MixinMovementInputFromOptions extends MovementInput {

    @Shadow private boolean[] field_1179_f;

    @Unique private int ticksSinceJump = 100;
    @Unique private boolean sprinting;
    @Unique private boolean wasJumping;
    @Unique private boolean doublePressedJump;

    @Inject(method = "func_796_a", at = @At("HEAD"))
    private void preReadInput(CallbackInfo ci) {
        wasJumping = field_1179_f[4];
    }

    @Inject(method = "func_796_a", at = @At("TAIL"))
    private void onReadInput(CallbackInfo ci) {
        sprinting = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
        boolean pressedJump = field_1179_f[4] && !wasJumping;
        doublePressedJump = false;
        if (pressedJump) {
            if (ticksSinceJump < 5) {
                doublePressedJump = true;
                ticksSinceJump = 100;
            } else {
                ticksSinceJump = 0;
            }
        }
    }

    @Inject(method = "func_797_a", at = @At("TAIL"))
    private void onApplyInput(EntityPlayer player, CallbackInfo ci) {
        ticksSinceJump++;
        IPlayer iPlayer = (IPlayer) player;
        iPlayer.setSprinting(sprinting);
        if (doublePressedJump) {
            iPlayer.setFlying(!iPlayer.isFlying());
            doublePressedJump = false;
        }
    }

}
