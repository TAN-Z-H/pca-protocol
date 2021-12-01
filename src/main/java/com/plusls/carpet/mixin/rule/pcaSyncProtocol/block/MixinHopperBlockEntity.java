package com.plusls.carpet.mixin.rule.pcaSyncProtocol.block;

import com.plusls.carpet.ModInfo;
import com.plusls.carpet.PcaSettings;
import com.plusls.carpet.network.PcaSyncProtocol;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.util.Tickable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(HopperBlockEntity.class)
public abstract class MixinHopperBlockEntity extends LootableContainerBlockEntity implements Hopper, Tickable {

    protected MixinHopperBlockEntity(BlockEntityType<?> blockEntityType) {
        super(blockEntityType);
    }

    @Inject(method = "insertAndExtract", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;markDirty()V"))
    private void onInsertAndExtract(Supplier<Boolean> extractMethod, CallbackInfoReturnable<Boolean> cir) {
        if (PcaSettings.pcaSyncProtocol && PcaSyncProtocol.syncBlockEntityToClient(this)) {
            ModInfo.LOGGER.debug("update HopperBlockEntity: {}", pos);
        }
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (PcaSettings.pcaSyncProtocol && PcaSyncProtocol.syncBlockEntityToClient(this)) {
            ModInfo.LOGGER.debug("update HopperBlockEntity: {}", this.pos);
        }
    }
}