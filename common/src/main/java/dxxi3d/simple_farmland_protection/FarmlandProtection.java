package dxxi3d.simple_farmland_protection;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;

import java.util.Optional;

public final class FarmlandProtection {
    private static final double BASE_FALL_DISTANCE = 1.51;
    private static final double FEATHER_FALLING_BONUS = 2.0;
    private static final float MIN_TRAMPLE_VOLUME = 0.512F;

    private FarmlandProtection() {
    }

    public static boolean canTrample(Entity entity, Level level, double fallDistance) {
        if (!(entity instanceof LivingEntity living)) {
            return false;
        }

        if (isSmallEntity(living)) {
            return false;
        }

        if (!(living instanceof Player) && level instanceof ServerLevel serverLevel) {
            if (!serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)) {
                return false;
            }
        }

        double threshold = BASE_FALL_DISTANCE + (getFeatherFallingLevel(living, level) * FEATHER_FALLING_BONUS);
        return fallDistance > threshold;
    }

    private static boolean isSmallEntity(LivingEntity living) {
        if (living.isBaby()) {
            return true;
        }

        float volume = living.getBbWidth() * living.getBbWidth() * living.getBbHeight();
        return volume <= MIN_TRAMPLE_VOLUME;
    }

    private static int getFeatherFallingLevel(LivingEntity living, Level level) {
        ItemStack boots = living.getItemBySlot(EquipmentSlot.FEET);
        if (boots.isEmpty()) {
            return 0;
        }

        Optional<Registry<Enchantment>> enchantmentRegistry = level.registryAccess().lookup(Registries.ENCHANTMENT);
        if (enchantmentRegistry.isEmpty()) {
            return 0;
        }

        Optional<Holder.Reference<Enchantment>> featherFalling = enchantmentRegistry.get().get(Enchantments.FEATHER_FALLING);
        if (featherFalling.isEmpty()) {
            return 0;
        }

        return EnchantmentHelper.getItemEnchantmentLevel(featherFalling.get(), boots);
    }
}
