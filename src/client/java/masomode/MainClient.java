package masomode;

import masomode.block.CustomBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.item.BucketItem;

public class MainClient implements ClientModInitializer {

    public static Minecraft mc;

	@Override
	public void onInitializeClient() {
        mc = Minecraft.getInstance();

        BlockRenderLayerMap.putBlock(CustomBlocks.BLUE_HERB, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(CustomBlocks.RED_HERB, ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(CustomBlocks.GREEN_HERB, ChunkSectionLayer.CUTOUT);
	}
}