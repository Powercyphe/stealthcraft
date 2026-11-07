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
    @Condition(requiredOption = "targetMode", requiredValue = "RADIUS")
    public static int targetRadius = 30;

    @Entry
    @Condition(requiredOption = "seenMode", requiredValue = "TARGET")
    public static int targetTicks = 300;

    @Comment
    public static String spacer$mode;


    @Entry(min = 0, max = 100, isSlider = true)
    public static int minDetectionTicks = 0;

    @Entry
    public static boolean entityGoalTweaks = false;

    @Comment
    public static String spacer$color;

    @Entry(isColor = true)
    public static String coneColor = "#FF1100";

    @Entry(min = 0F, max = 1F, isSlider = true)
    public static float coneColorOpacity = 0.2F;

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
