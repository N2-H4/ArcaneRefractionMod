package com.N2H4.arcanerefraction.Utils;

import com.mojang.authlib.GameProfile;
import java.lang.ref.WeakReference;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;

public class FakePlayerMiner extends FakePlayer {

    public static final UUID FakePlayerUUID = UUID.fromString("ae6c3c9c-22cf-43c4-bc1a-812f1cc36367");
    public static final GameProfile FakePlayerProfile = new GameProfile(FakePlayerUUID, "[ArcaneRefractionFakePlayer]");
    private static WeakReference<FakePlayerMiner> INSTANCE;

    private FakePlayerMiner(ServerLevel world) {
        super(world, FakePlayerProfile);
    }

    @Override
    public boolean canBeAffected(@NotNull MobEffectInstance effect) {
        return false;
    }

    public void cleanupFakePlayer(ServerLevel world) {
        setServerLevel(world.getServer().overworld());
    }

    @SuppressWarnings("WeakerAccess")
    public static FakePlayerMiner setupFakePlayer(ServerLevel world) {
        FakePlayerMiner actual = INSTANCE == null ? null : INSTANCE.get();
        if (actual == null) {
            actual = new FakePlayerMiner(world);
            INSTANCE = new WeakReference<>(actual);
        }
        FakePlayerMiner player = actual;
        player.setServerLevel(world);
        return player;
    }

    public static FakePlayerMiner setupFakePlayer(ServerLevel world, double x, double y, double z) {
        FakePlayerMiner player = setupFakePlayer(world);
        player.setPosRaw(x, y, z);
        return player;
    }

    public static void releaseInstance(ServerLevel world) {
        FakePlayerMiner actual = INSTANCE == null ? null : INSTANCE.get();
        if (actual != null && actual.serverLevel() == world) {
            actual.setServerLevel(world.getServer().overworld());
        }
    }
}
