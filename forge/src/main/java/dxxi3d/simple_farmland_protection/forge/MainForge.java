package dxxi3d.simple_farmland_protection.forge;

import dxxi3d.simple_farmland_protection.ModLogger;
import dxxi3d.simple_farmland_protection.SharedConstants;
import net.minecraftforge.fml.common.Mod;

@Mod(SharedConstants.MOD_ID)
public final class MainForge {
    public MainForge() {
        ModLogger.sendInitialized();
    }
}
