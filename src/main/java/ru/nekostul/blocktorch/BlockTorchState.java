package ru.nekostul.blocktorch;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockTorchState {

    public static boolean ENABLED = true;
    public static boolean MESSAGE_SHOWN = false;

    private static final Set<Item> BLOCKED_ITEMS = new HashSet<>();

    // дефолты для мода BlockTorch
    private static final List<String> DEFAULT_ITEMS = List.of(
            "minecraft:torch",
            "minecraft:soul_torch"
    );

    // вызывается при старте клиента
    public static void loadFromConfig() {
        BLOCKED_ITEMS.clear();

        ENABLED = BlockTorchConfig.CLIENT.enabled.get();

        List<? extends String> ids = BlockTorchConfig.CLIENT.blockedItems.get();

        // 🔥 ЕСЛИ КОНФИГ ПУСТОЙ — ЗАГРУЖАЕМ ДЕФОЛТЫ
        if (ids.isEmpty()) {
            for (String id : DEFAULT_ITEMS) {
                Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(id));
                if (item != null) {
                    BLOCKED_ITEMS.add(item);
                }
            }
            saveToConfig(); // сразу записываем дефолты
            return;
        }

        // обычная загрузка
        for (String id : ids) {
            Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(id));
            if (item != null) {
                BLOCKED_ITEMS.add(item);
            }
        }
    }

    private static void saveToConfig() {
        BlockTorchConfig.CLIENT.enabled.set(ENABLED);

        BlockTorchConfig.CLIENT.blockedItems.set(
                BLOCKED_ITEMS.stream()
                        .map(item -> BuiltInRegistries.ITEM.getKey(item).toString())
                        .toList()
        );
    }

    public static void toggle() {
        ENABLED = !ENABLED;
        MESSAGE_SHOWN = false;
        saveToConfig();
    }

    public static void toggleItem(Item item) {
        if (BLOCKED_ITEMS.contains(item)) {
            BLOCKED_ITEMS.remove(item);
        } else {
            BLOCKED_ITEMS.add(item);
        }
        MESSAGE_SHOWN = false;
        saveToConfig();
    }

    public static boolean isBlocked(Item item) {
        return BLOCKED_ITEMS.contains(item);
    }

    public static Set<Item> getBlockedItems() {
        return BLOCKED_ITEMS;
    }
}