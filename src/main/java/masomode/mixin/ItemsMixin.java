package masomode.mixin;

import masomode.consumeeffect.CureConsumeEffect;
import masomode.Main;
import masomode.mobeffect.BleedingMobEffect;
import masomode.mobeffect.BoneFractureMobEffect;
import masomode.mobeffect.CustomMobEffects;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.Consumables;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(Items.class)
public class ItemsMixin {

    @Unique
    private static Item modifyItem(String string) {
        Item item = null;

        switch (string) {
            case "paper":
                CustomMobEffects.BLEED = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(Main.MOD_ID, "bleed"), new BleedingMobEffect(MobEffectCategory.HARMFUL, 9999999));
                item = Items.registerItem(
                        ResourceKey.create(Registries.ITEM, Identifier.withDefaultNamespace(string)),
                        Item::new,
                        new Item.Properties().food(new FoodProperties.Builder().alwaysEdible().build(), Consumables.defaultFood().consumeSeconds(5.0F).onConsume(new CureConsumeEffect(CustomMobEffects.BLEED)).build())
                );
                break;
            case "porkchop":
                item = Items.registerItem(
                        ResourceKey.create(Registries.ITEM, Identifier.withDefaultNamespace(string)),
                        Item::new,
                        new Item.Properties().food(Foods.PORKCHOP, Consumables.CHICKEN)
                );
                break;
            case "beef":
                item = Items.registerItem(
                        ResourceKey.create(Registries.ITEM, Identifier.withDefaultNamespace(string)),
                        Item::new,
                        new Item.Properties().food(Foods.BEEF, Consumables.CHICKEN)
                );
                break;
            case "rabbit":
                item = Items.registerItem(
                        ResourceKey.create(Registries.ITEM, Identifier.withDefaultNamespace(string)),
                        Item::new,
                        new Item.Properties().food(Foods.RABBIT, Consumables.CHICKEN)
                );
                break;
            case "mutton":
                item = Items.registerItem(
                        ResourceKey.create(Registries.ITEM, Identifier.withDefaultNamespace(string)),
                        Item::new,
                        new Item.Properties().food(Foods.MUTTON, Consumables.CHICKEN)
                );
                break;
            case "cod":
                item = Items.registerItem(
                        ResourceKey.create(Registries.ITEM, Identifier.withDefaultNamespace(string)),
                        Item::new,
                        new Item.Properties().food(Foods.COD, Consumables.CHICKEN)
                );
                break;
            case "salmon":
                item = Items.registerItem(
                        ResourceKey.create(Registries.ITEM, Identifier.withDefaultNamespace(string)),
                        Item::new,
                        new Item.Properties().food(Foods.SALMON, Consumables.CHICKEN)
                );
                break;
            case "sugar":
                item = Items.registerItem(
                        ResourceKey.create(Registries.ITEM, Identifier.withDefaultNamespace(string)),
                        Item::new,
                        new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.0F).build())
                );
                break;
            case "milk_bucket":
                CustomMobEffects.BONE_FRACTURE = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(Main.MOD_ID, "bone_fracture"), new BoneFractureMobEffect(MobEffectCategory.HARMFUL, 9999999));
                item = Items.registerItem(
                        ResourceKey.create(Registries.ITEM, Identifier.withDefaultNamespace(string)),
                        Item::new,
                        new Item.Properties().craftRemainder(Items.BUCKET).component(DataComponents.CONSUMABLE, Consumables.defaultDrink().onConsume(new CureConsumeEffect(CustomMobEffects.BONE_FRACTURE)).build()).usingConvertsTo(Items.BUCKET).stacksTo(1)
                );
                break;
            case "wooden_axe":
                item = Items.registerItem(
                        ResourceKey.create(Registries.ITEM, Identifier.withDefaultNamespace(string)),
                        properties -> new AxeItem(ToolMaterial.WOOD, 2.0F, -3.2F, properties),
                        new Item.Properties()
                );
                break;
            case "stone_axe":
                item = Items.registerItem(
                        ResourceKey.create(Registries.ITEM, Identifier.withDefaultNamespace(string)),
                        properties -> new AxeItem(ToolMaterial.STONE, 2.0F, -3.2F, properties),
                        new Item.Properties()
                );
                break;
            case "golden_axe":
                item = Items.registerItem(
                        ResourceKey.create(Registries.ITEM, Identifier.withDefaultNamespace(string)),
                        properties -> new AxeItem(ToolMaterial.GOLD, 3.0F, -3.0F, properties),
                        new Item.Properties()
                );
                break;
            case "copper_axe":
                item = Items.registerItem(
                        ResourceKey.create(Registries.ITEM, Identifier.withDefaultNamespace(string)),
                        properties -> new AxeItem(ToolMaterial.COPPER, 3.0F, -3.2F, properties),
                        new Item.Properties()
                );
                break;
            case "iron_axe":
                item = Items.registerItem(
                        ResourceKey.create(Registries.ITEM, Identifier.withDefaultNamespace(string)),
                        properties -> new AxeItem(ToolMaterial.IRON, 2.0F, -3.1F, properties),
                        new Item.Properties()
                );
                break;
            case "diamond_axe":
                item = Items.registerItem(
                        ResourceKey.create(Registries.ITEM, Identifier.withDefaultNamespace(string)),
                        properties -> new AxeItem(ToolMaterial.DIAMOND, 3.0F, -3.0F, properties),
                        new Item.Properties()
                );
                break;
        }

        return item;
    }

    @Inject(method = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;)Lnet/minecraft/world/item/Item;", at = @At("HEAD"), cancellable = true)
    private static void registerItem(String string, CallbackInfoReturnable<Item> callbackInfoReturnable) {
        Item item = modifyItem(string);

        if (item != null) {
            callbackInfoReturnable.setReturnValue(item);
        }
    }

    @Inject(method = "Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Ljava/util/function/Function;)Lnet/minecraft/world/item/Item;", at = @At("HEAD"), cancellable = true)
    private static void registerItem(String string, Function<Item.Properties, Item> function, CallbackInfoReturnable<Item> callbackInfoReturnable) {
        Item item = modifyItem(string);

        if (item != null) {
            callbackInfoReturnable.setReturnValue(item);
        }
    }

    @Inject(method = " Lnet/minecraft/world/item/Items;registerItem(Ljava/lang/String;Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", at = @At("HEAD"), cancellable = true)
    private static void registerItem(String string, Item.Properties properties, CallbackInfoReturnable<Item> callbackInfoReturnable) {
        Item item = modifyItem(string);

        if (item != null) {
            callbackInfoReturnable.setReturnValue(item);
        }
    }
}
