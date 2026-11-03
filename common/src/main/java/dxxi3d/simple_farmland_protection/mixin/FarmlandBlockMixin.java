package dxxi3d.simple_farmland_protection.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dxxi3d.simple_farmland_protection.FarmlandProtection;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FarmlandBlock.class)
public abstract class FarmlandBlockMixin {

    @WrapOperation(
        method = "fallOn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/FarmlandBlock;turnToBaseBlock(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"
        ),
        require = 0
    )
    private void wrapTurnToBaseBlock(
        FarmlandBlock farmland,
        Entity entity,
        BlockState state,
        Level level,
        BlockPos pos,
        Operation<Void> original,
        @Local(argsOnly = true) double fallDistance
    ) {
        if (FarmlandProtection.canTrample(entity, level, fallDistance)) {
            original.call(farmland, entity, state, level, pos);
        }
    }
}
