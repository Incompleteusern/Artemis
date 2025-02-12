/*
 * Copyright © Wynntils 2022.
 * This file is released under AGPLv3. See LICENSE for full license details.
 */
package com.wynntils.mc.mixin;

import com.wynntils.mc.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestMenu.class)
public abstract class ChestMenuMixin {
    @Inject(method = "quickMoveStack", at = @At("HEAD"))
    public void onQuickMoveStack(Player player, int index, CallbackInfoReturnable<ItemStack> cir) {
        EventFactory.onChestMenuQuickMove(((ChestMenu) (Object) this).containerId);
    }
}
