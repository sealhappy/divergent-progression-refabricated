package sealhappy.divergeprog.item;

import sealhappy.divergeprog.DivergentProgression;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;

public class ModItems {

    // Bars
    public static final Item FLINT_BAR =
            register("flint_bar", Item::new, new Item.Properties());

    public static final Item COPPER_BAR =
            register("copper_bar", Item::new, new Item.Properties());

    public static final Item IRON_BAR =
            register("iron_bar", Item::new, new Item.Properties());

    public static final Item GOLD_BAR =
            register("gold_bar", Item::new, new Item.Properties());

    public static final Item DIAMOND_BAR =
            register("diamond_bar", Item::new, new Item.Properties());

    public static final Item RAW_COPPER_BAR =
            register("raw_copper_bar", Item::new, new Item.Properties());

    public static final Item RAW_IRON_BAR =
            register("raw_iron_bar", Item::new, new Item.Properties());

    public static final Item RAW_GOLD_BAR =
            register("raw_gold_bar", Item::new, new Item.Properties());


    // Flint Tools
    public static final Item FLINT_SWORD =
            register("flint_sword", Item::new,
                    new Item.Properties().sword(
                            ModToolMaterials.FLINT, 3.0F, -2.4F).repairable(Items.FLINT));

    public static final Item FLINT_SPEAR =
            register("flint_spear", Item::new,
                    new Item.Properties().spear(
                            ModToolMaterials.FLINT,
                            0.85F, 0.82F, 0.65F,
                            4.0F, 9.0F, 8.25F, 5.1F, 12.5F, 4.6F
                    ).repairable(Items.FLINT));

    public static final Item FLINT_SHOVEL =
            register("flint_shovel",
                    Item::new,
                    new Item.Properties().shovel(ModToolMaterials.FLINT, 1.5F, -3.0F).repairable(Items.FLINT));

    public static final Item FLINT_PICKAXE =
            register("flint_pickaxe", Item::new,
                    new Item.Properties().pickaxe(
                            ModToolMaterials.FLINT, 1.0F, -2.8F).repairable(Items.FLINT));

    public static final Item FLINT_AXE =
            register("flint_axe",
                    Item::new,
                    new Item.Properties().axe(ModToolMaterials.FLINT, 6.0F, -3.2F).repairable(Items.FLINT));

    public static final Item FLINT_HOE =
            register("flint_hoe",
                    Item::new,
                    new Item.Properties().hoe(ModToolMaterials.FLINT, 0.0F, -3.0F).repairable(Items.FLINT));


    // Item Registration
    private static Item register(
            String name,
            java.util.function.Function<Item.Properties, Item> factory,
            Item.Properties properties) {

        ResourceKey<Item> itemKey = ResourceKey.create(
                Registries.ITEM,
                DivergentProgression.id(name)
        );

        Item item = factory.apply(properties.setId(itemKey));

        return Registry.register(
                BuiltInRegistries.ITEM,
                itemKey,
                item
        );
    }


    // Creative Inventory — insert custom items beside their vanilla equivalents.
    public static void initialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(tab -> {
            tab.insertAfter(Items.FLINT, FLINT_BAR);
            tab.insertAfter(Items.RAW_COPPER, RAW_COPPER_BAR);
            tab.insertAfter(Items.COPPER_INGOT, COPPER_BAR);
            tab.insertAfter(Items.RAW_IRON, RAW_IRON_BAR);
            tab.insertAfter(Items.IRON_INGOT, IRON_BAR);
            tab.insertAfter(Items.RAW_GOLD, RAW_GOLD_BAR);
            tab.insertAfter(Items.GOLD_INGOT, GOLD_BAR);
            tab.insertAfter(Items.DIAMOND, DIAMOND_BAR);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(tab -> {
            tab.insertAfter(Items.WOODEN_SWORD, FLINT_SWORD);
            tab.insertAfter(Items.WOODEN_SPEAR, FLINT_SPEAR);
            tab.insertAfter(Items.WOODEN_AXE, FLINT_AXE);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(tab -> {
            tab.insertAfter(Items.WOODEN_SHOVEL, FLINT_SHOVEL);
            tab.insertAfter(Items.WOODEN_PICKAXE, FLINT_PICKAXE);
            tab.insertAfter(Items.WOODEN_AXE, FLINT_AXE);
            tab.insertAfter(Items.WOODEN_HOE, FLINT_HOE);
        });
    }
}
