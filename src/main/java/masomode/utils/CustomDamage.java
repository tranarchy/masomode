package masomode.utils;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CustomDamage {
    public enum DAMAGE_TYPE {
        BLUNT, SLASHING, PIERCING, MAGIC, OTHER
    }

    public static DAMAGE_TYPE getDamageType(ItemStack weaponItem) {
        DAMAGE_TYPE damageType;

        if (weaponItem.is(ItemTags.AXES) || weaponItem.is(ItemTags.SHOVELS) || weaponItem.is(Items.MACE) || weaponItem.is(Items.AIR)) {
            damageType = DAMAGE_TYPE.BLUNT;
        } else if (weaponItem.is(ItemTags.SWORDS) || weaponItem.is(Items.SHEARS)) {
            damageType = DAMAGE_TYPE.SLASHING;
        } else if (
                weaponItem.is(ItemTags.SPEARS) ||
                        weaponItem.is(Items.TRIDENT) || weaponItem.is(ItemTags.PICKAXES) ||
                        weaponItem.is(ItemTags.HOES)
        ) {
            damageType = DAMAGE_TYPE.PIERCING;
        } else {
            damageType = DAMAGE_TYPE.OTHER;
        }

        return damageType;
    }

    public static DAMAGE_TYPE getDamageType(DamageSource damageSource) {
        DAMAGE_TYPE damageType;

        if (damageSource.is(DamageTypes.ARROW)) {
            damageType = DAMAGE_TYPE.PIERCING;
        } else if (damageSource.is(DamageTypes.MAGIC)) {
            damageType = DAMAGE_TYPE.MAGIC;
        } else {
            damageType = DAMAGE_TYPE.OTHER;
        }

        return damageType;
    }

    public static HashMap<EntityType<?>, List<Float>> resistances = new HashMap<>() {{
        put(EntityType.WITHER, Arrays.asList(1.0f, 0.6f, 0.4f, 1.0f, 1.0f));
        put(EntityType.WITHER_SKELETON, Arrays.asList(1.0f, 0.6f, 0.4f, 1.0f, 1.0f));
        put(EntityType.SKELETON, Arrays.asList(1.2f, 0.8f, 0.6f, 1.0f, 1.0f));
        put(EntityType.DROWNED, Arrays.asList(0.7f, 1.1f, 0.7f, 1.0f, 1.0f));
        put(EntityType.HUSK, Arrays.asList(0.7f, 1.1f, 0.7f, 1.0f, 1.0f));
        put(EntityType.ZOMBIE, Arrays.asList(0.8f, 1.2f, 0.8f, 1.0f, 1.0f));
        put(EntityType.ZOMBIFIED_PIGLIN, Arrays.asList(0.7f, 1.1f, 0.7f, 1.0f, 1.0f));
        put(EntityType.WITCH, Arrays.asList(1.0f, 1.2f, 1.2f, 1.0f, 1.0f));
        put(EntityType.CREEPER, Arrays.asList(0.8f, 0.8f, 1.2f, 1.0f, 1.0f));
        put(EntityType.BLAZE, Arrays.asList(1.0f, 0.8f, 0.6f, 1.0f, 1.0f));
        put(EntityType.PIGLIN_BRUTE, Arrays.asList(1.0f, 0.6f, 0.8f, 1.0f, 1.0f));
        put(EntityType.PIGLIN, Arrays.asList(1.2f, 0.8f, 1.0f, 1.0f, 1.0f));
        put(EntityType.VINDICATOR, Arrays.asList(1.2f, 0.8f, 1.0f, 1.0f, 1.0f));
        put(EntityType.PILLAGER, Arrays.asList(1.2f, 0.8f, 1.0f, 1.0f, 1.0f));
    }};
}
