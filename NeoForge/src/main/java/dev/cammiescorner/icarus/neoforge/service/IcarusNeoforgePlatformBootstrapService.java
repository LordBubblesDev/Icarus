package dev.cammiescorner.icarus.neoforge.service;

import com.google.auto.service.AutoService;
import dev.cammiescorner.icarus.util.IcarusPlatformBootstrapService;
import net.neoforged.fml.ModList;

@AutoService(IcarusPlatformBootstrapService.class)
public class IcarusNeoforgePlatformBootstrapService implements IcarusPlatformBootstrapService {

    @Override
    public boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }
}
