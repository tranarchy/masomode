package masomode.mixin;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    @Shadow
    private RecipeMap recipes;

    @Unique
    private static final List<String> itemsToRemove = Arrays.asList(
            "wooden_axe", "wooden_pickaxe", "wooden_shovel", "wooden_hoe", "wooden_sword", "wooden_spear",
            "copper_torch", "copper_lantern",
            "polished_deepslate", "polished_andesite", "polished_diorite", "polished_granite", "polished_basalt", "polished_blackstone", "polished_tuff",
            "enchanting_table"
    );

    @Inject(method = "apply", at = @At("HEAD"), cancellable = true)
    protected void apply(RecipeMap recipeMap, ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfo info) {
        List<RecipeHolder<?>> modifiedRecipes = new ArrayList<>();

        for (RecipeHolder<?> recipeHolder : recipeMap.values()) {
            if (itemsToRemove.contains(recipeHolder.id().identifier().getPath()))
                continue;

            modifiedRecipes.add(recipeHolder);
        }

        this.recipes = RecipeMap.create(modifiedRecipes);

        info.cancel();
    }
}
