package ru.nekostul.blocktorch;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class BlockTorchConfig {

    public static final ForgeConfigSpec SPEC;
    public static final Client CLIENT;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        CLIENT = new Client(builder);
        SPEC = builder.build();
    }

    public static class Client {

        public final ForgeConfigSpec.BooleanValue enabled;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> blockedItems;

        Client(ForgeConfigSpec.Builder builder) {
            builder.push("general");

            enabled = builder
                    .comment("Enable BlockTorch")
                    .define("enabled", true);

            blockedItems = builder
                    .comment("Blocked items (registry names)")
                    .defineListAllowEmpty(
                            "blockedItems",
                            List.of(
                                    "minecraft:torch",
                                    "minecraft:soul_torch"
                            ),
                            o -> o instanceof String
                    );

            builder.pop();
        }
    }
}
