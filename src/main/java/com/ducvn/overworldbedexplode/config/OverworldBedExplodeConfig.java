package com.ducvn.overworldbedexplode.config;

import net.minecraft.nbt.StringNBT;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public final class OverworldBedExplodeConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Double> explosion_chance;
    public static final ForgeConfigSpec.ConfigValue<Boolean> aplly_on_all_dimensions;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> mob_list;
    public static final ForgeConfigSpec.ConfigValue<Integer> mob_number;
    public static final ForgeConfigSpec.ConfigValue<Double> teleport_random_player_chance;

    static {
        BUILDER.push("Overworld Bed Explode Config");

        explosion_chance = BUILDER.comment("Bed explosion chance from 0.0 to 1.0, default: 1.0").define("Bed Explosion Chance", 1.0);
        aplly_on_all_dimensions = BUILDER.comment("Apply this mod on all dimension?(true/false) default: true").define("Apply On All Dimension", true);
        mob_list = BUILDER.comment("List of mob can spawn when bed explode. Example: [\"minecraft:zombie\",\"minecraft:skeleton\"] will spawn zombie and skeleton when bed explode").defineList("Mob List", new ArrayList<String>(), (p)
        -> {return p instanceof String;});
        mob_number = BUILDER.comment("Number of mobs can spawn, random between 1 and x (x is the input number, x > 0). Example: 5 will spawn 1-5 each mob in the list").define("Number Of Mobs", 1);
        teleport_random_player_chance = BUILDER.comment("Teleport random player to bed explode location. (0.0-1.0)").define("Teleport Chance",0.0);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
