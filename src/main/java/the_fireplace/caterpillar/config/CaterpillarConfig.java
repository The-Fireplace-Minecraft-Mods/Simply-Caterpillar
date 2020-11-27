package the_fireplace.caterpillar.config;

/**
 * This holds the baked (runtime) values for our config.
 * These values should never be from changed outside this package.
 * This can be split into multiple classes (Server, Client, Player, Common)
 * but has been kept in one class for simplicity
 */
public class CaterpillarConfig {

    // Client
    public static boolean drill_head;
    public static boolean coal;
    public static boolean power;
    public static boolean storage;
    public static boolean decoration_selection;
    public static boolean decoration_selection_zero;
    public static boolean decoration_pattern;
    public static boolean reinforcement_1;
    public static boolean reinforcement_2;

    // Server
    public static boolean firstUse;
    public static boolean useParticles;
    public static boolean breakBedrock;
    public static boolean enableSounds;
}
