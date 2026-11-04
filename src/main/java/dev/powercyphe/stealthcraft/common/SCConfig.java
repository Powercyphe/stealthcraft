package dev.powercyphe.stealthcraft.common;

import eu.midnightdust.lib.config.MidnightConfig;

public class SCConfig extends MidnightConfig {

    @Entry
    public static SeenMode seenMode = SeenMode.KILL;

    @Entry
    @Condition(requiredOption = "seenMode", requiredValue = "DAMAGE")
    public static DamageMode damageMode = DamageMode.ENTITY;

    @Entry
    @Condition(requiredOption = "seenMode", requiredValue = "DAMAGE")
    public static float damageAmount = 3;

    @Entry
    @Condition(requiredOption = "seenMode", requiredValue = "TARGET")
    public static TargetMode targetMode = TargetMode.SINGLE;

    @Entry
    @Condition(requiredOption = "seenMode", requiredValue = "TARGET")
    public static int targetTicks = 300;

    @Entry
    @Condition(requiredOption = "seenMode", requiredValue = "TARGET")
    @Condition(requiredOption = "targetMode", requiredValue = "RADIUS")
    public static int targetRadius = 30;


    @Entry(min = 0, max = 100, isSlider = true)
    public static int minDetectionTicks = 10;

    @Entry
    public static boolean entityGoalTweaks = true;

    @Entry(isColor = true)
    public static String coneColor = "#FF1100";

    public enum SeenMode {
        KILL,
        DAMAGE,
        TARGET
    }

    public enum DamageMode {
        ENTITY,
        FULL_BYPASS
    }

    public enum TargetMode {
        SINGLE,
        RADIUS,
        ALL
    }
}
