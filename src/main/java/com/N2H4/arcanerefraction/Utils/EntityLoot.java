package com.N2H4.arcanerefraction.Utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import java.util.ArrayList;
import java.util.List;

public class EntityLoot 
{
    public static void dropLoot(Level level, LivingEntity e)
    {
        List<ItemStack> drops=getLoot(e, level);
        for (ItemStack drop : drops)
        {
            dropItems(level, e.position().x(), e.position().y(), e.position().z(), drop);
        }
    }

    public static List<ItemStack> getLoot(LivingEntity e, Level level)
    {
        ResourceLocation resourcelocation = e.getLootTable();
        LootTable loottable = e.level().getServer().getLootData().getLootTable(resourcelocation);
        LootParams.Builder lootparams$builder = new LootParams.Builder((ServerLevel)e.level())
            .withParameter(LootContextParams.THIS_ENTITY, e)
            .withParameter(LootContextParams.ORIGIN, e.position())
            .withParameter(LootContextParams.DAMAGE_SOURCE, level.damageSources().magic())
            .withOptionalParameter(LootContextParams.KILLER_ENTITY, level.damageSources().magic().getEntity())
            .withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, level.damageSources().magic().getDirectEntity());
        LootParams lootparams = lootparams$builder.create(LootContextParamSets.ENTITY);
        return loottable.getRandomItems(lootparams,e.getLootTableSeed());
    }

    public static ItemEntity dropItems(Level level, double x, double y, double z, ItemStack stack) 
    {
        if (level.isClientSide) 
        {
            return null;
        }
        ItemEntity ei = new ItemEntity(level, x, y, z, stack);
        applyRandomDropOffset(level,ei);
        level.addFreshEntity(ei);
        ei.setPickUpDelay(20);
        return ei;
    }

    public static void applyRandomDropOffset(Level level, ItemEntity item) 
    {
        item.lerpMotion(level.getRandom().nextFloat() * 0.3F - 0.15D,
        level.getRandom().nextFloat() * 0.3F - 0.05D,
        level.getRandom().nextFloat() * 0.3F - 0.15D);
    }

}
