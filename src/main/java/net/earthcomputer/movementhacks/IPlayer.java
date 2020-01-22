package net.earthcomputer.movementhacks;

public interface IPlayer {

    boolean isFlying();

    void setFlying(boolean flying);

    boolean isSprinting();

    void setSprinting(boolean sprinting);

    boolean isImmuneToFallDamage();

}
