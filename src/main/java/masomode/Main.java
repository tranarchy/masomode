package masomode;

import masomode.item.CustomItemTags;
import masomode.item.CustomItems;
import masomode.mobeffect.CustomMobEffects;
import masomode.block.CustomBlocks;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class Main implements ModInitializer {
	public static final String MOD_ID = "masomode";

	@Override
	public void onInitialize() {
        CustomMobEffects.register();
        CustomBlocks.register();
        CustomItems.register();
        CustomItemTags.register();

        BiomeModifications.addSpawn(BiomeSelectors.foundInOverworld(), MobCategory.MONSTER, EntityType.ILLUSIONER, 5, 1, 1);
	}
}