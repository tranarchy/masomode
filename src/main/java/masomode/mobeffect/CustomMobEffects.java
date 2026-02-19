package masomode.mobeffect;

import masomode.Main;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class CustomMobEffects {
    public static Holder<MobEffect> BLEED;
    public static Holder<MobEffect> BONE_FRACTURE;
    public static Holder<MobEffect> FEAR;
    public static Holder<MobEffect> INFECTION;
    public static Holder<MobEffect> PARALYZED;

    public static void register() {
        FEAR =  Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(Main.MOD_ID, "fear"), new FearMobEffect(MobEffectCategory.HARMFUL, 9999999));
        INFECTION = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(Main.MOD_ID, "infection"), new InfectionMobEffect(MobEffectCategory.HARMFUL, 9999999));
        PARALYZED = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(Main.MOD_ID, "paralyzed"), new ParalyzedMobEffect(MobEffectCategory.HARMFUL, 9999999));
    }
}
