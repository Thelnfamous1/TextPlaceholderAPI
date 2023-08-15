package eu.pb4.placeholders.impl.placeholder.builtin;

import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;
import eu.pb4.placeholders.impl.GeneralUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlot;
import org.apache.commons.lang3.time.DurationFormatUtils;


public class PlayerPlaceholders {
    public static void register() {
        Placeholders.register(new ResourceLocation("player", "name"), (ctx, arg) -> {
            if (ctx.hasPlayer()) {
                return PlaceholderResult.value(ctx.player().getName());
            } else if (ctx.hasGameProfile()) {
                return PlaceholderResult.value(Component.nullToEmpty(ctx.gameProfile().getName()));
            } else {
                return PlaceholderResult.invalid("No player!");
            }
        });

        Placeholders.register(new ResourceLocation("player", "name_visual"), (ctx, arg) -> {
            if (ctx.hasPlayer()) {
                return PlaceholderResult.value(GeneralUtils.removeHoverAndClick(ctx.player().getName()));
            } else if (ctx.hasGameProfile()) {
                return PlaceholderResult.value(Component.nullToEmpty(ctx.gameProfile().getName()));
            } else {
                return PlaceholderResult.invalid("No player!");
            }
        });

        Placeholders.register(new ResourceLocation("player", "name_unformatted"), (ctx, arg) -> {
            if (ctx.hasPlayer()) {
                return PlaceholderResult.value(ctx.player().getName().getString());
            } else if (ctx.hasGameProfile()) {
                return PlaceholderResult.value(Component.nullToEmpty(ctx.gameProfile().getName()));
            } else {
                return PlaceholderResult.invalid("No player!");
            }
        });

        Placeholders.register(new ResourceLocation("player", "ping"), (ctx, arg) -> {
            if (ctx.hasPlayer()) {
                return PlaceholderResult.value(String.valueOf(ctx.player().latency));
            } else {
                return PlaceholderResult.invalid("No player!");
            }
        });

        Placeholders.register(new ResourceLocation("player", "ping_colored"), (ctx, arg) -> {
            if (ctx.hasPlayer()) {
                int x = ctx.player().latency;
                return PlaceholderResult.value(Component.literal(String.valueOf(x)).withStyle(x < 100 ? ChatFormatting.GREEN : x < 200 ? ChatFormatting.GOLD : ChatFormatting.RED));
            } else {
                return PlaceholderResult.invalid("No player!");
            }
        });

        Placeholders.register(new ResourceLocation("player", "displayname"), (ctx, arg) -> {
            if (ctx.hasPlayer()) {
                return PlaceholderResult.value(ctx.player().getDisplayName());
            } else if (ctx.hasGameProfile()) {
                return PlaceholderResult.value(Component.nullToEmpty(ctx.gameProfile().getName()));
            } else {
                return PlaceholderResult.invalid("No player!");
            }
        });

        Placeholders.register(new ResourceLocation("player", "displayname_visual"), (ctx, arg) -> {
            if (ctx.hasPlayer()) {
                return PlaceholderResult.value(GeneralUtils.removeHoverAndClick(ctx.player().getDisplayName()));
            } else if (ctx.hasGameProfile()) {
                return PlaceholderResult.value(Component.nullToEmpty(ctx.gameProfile().getName()));
            } else {
                return PlaceholderResult.invalid("No player!");
            }
        });

        Placeholders.register(new ResourceLocation("player", "displayname_unformatted"), (ctx, arg) -> {
            if (ctx.hasPlayer()) {
                return PlaceholderResult.value(Component.literal(ctx.player().getDisplayName().getString()));
            } else if (ctx.hasGameProfile()) {
                return PlaceholderResult.value(Component.nullToEmpty(ctx.gameProfile().getName()));
            } else {
                return PlaceholderResult.invalid("No player!");
            }
        });

        Placeholders.register(new ResourceLocation("player", "inventory_slot"), (ctx, arg) -> {
            if (ctx.hasPlayer() && arg != null) {
                try {
                    int slot = Integer.parseInt(arg);

                    var inventory = ctx.player().getInventory();

                    if (slot >= 0 && slot < inventory.getContainerSize()) {
                        var stack = inventory.getItem(slot);

                        return PlaceholderResult.value(GeneralUtils.getItemText(stack));
                    }

                } catch (Exception e) {
                    // noop
                }
                return PlaceholderResult.invalid("Invalid argument");
            } else {
                return PlaceholderResult.invalid("No player or invalid argument!");
            }
        });

        Placeholders.register(new ResourceLocation("player", "equipment_slot"), (ctx, arg) -> {
            if (ctx.hasPlayer() && arg != null) {
                try {
                    var slot = EquipmentSlot.byName(arg);

                    var stack = ctx.player().getItemBySlot(slot);
                    return PlaceholderResult.value(GeneralUtils.getItemText(stack));
                } catch (Exception e) {
                    // noop
                }
                return PlaceholderResult.invalid("Invalid argument");
            } else {
                return PlaceholderResult.invalid("No player or invalid argument!");
            }
        });

        Placeholders.register(new ResourceLocation("player", "playtime"), (ctx, arg) -> {
            if (ctx.hasPlayer()) {
                int x = ctx.player().getStats().getValue(Stats.CUSTOM.get(Stats.PLAY_TIME));
                return PlaceholderResult.value(arg != null
                        ? DurationFormatUtils.formatDuration((long) x * 50, arg, true)
                        : GeneralUtils.durationToString((long) x / 20)
                );
            } else {
                return PlaceholderResult.invalid("No player!");
            }
        });

        Placeholders.register(new ResourceLocation("player", "statistic"), (ctx, arg) -> {
            if (ctx.hasPlayer() && arg != null) {
                try {
                    ResourceLocation identifier = ResourceLocation.tryParse(arg);
                    if (identifier != null) {
                        int x = ctx.player().getStats().getValue(Stats.CUSTOM.get(Registry.CUSTOM_STAT.get(identifier)));
                        return PlaceholderResult.value(String.valueOf(x));
                    }
                } catch (Exception e) {
                    /* Into the void you go! */
                }
                return PlaceholderResult.invalid("Invalid statistic!");
            } else {
                return PlaceholderResult.invalid("No player!");
            }
        });

        Placeholders.register(new ResourceLocation("player", "pos_x"), (ctx, arg) -> {
            if (ctx.hasPlayer()) {
                double value = ctx.player().getX();
                String format = "%.2f";

                if (arg != null) {
                    try {
                        int x = Integer.parseInt(arg);
                        format = "%." + x + "f";
                    } catch (Exception e) {
                        format = "%.2f";
                    }
                }

                return PlaceholderResult.value(String.format(format, value));
            } else {
                return PlaceholderResult.invalid("No player!");
            }
        });

        Placeholders.register(new ResourceLocation("player", "pos_y"), (ctx, arg) -> {
            if (ctx.hasPlayer()) {
                double value = ctx.player().getY();
                String format = "%.2f";

                if (arg != null) {
                    try {
                        int x = Integer.parseInt(arg);
                        format = "%." + x + "f";
                    } catch (Exception e) {
                        format = "%.2f";
                    }
                }

                return PlaceholderResult.value(String.format(format, value));
            } else {
                return PlaceholderResult.invalid("No player!");
            }
        });

        Placeholders.register(new ResourceLocation("player", "pos_z"), (ctx, arg) -> {
            if (ctx.hasPlayer()) {
                double value = ctx.player().getZ();
                String format = "%.2f";

                if (arg != null) {
                    try {
                        int x = Integer.parseInt(arg);
                        format = "%." + x + "f";
                    } catch (Exception e) {
                        format = "%.2f";
                    }
                }

                return PlaceholderResult.value(String.format(format, value));
            } else {
                return PlaceholderResult.invalid("No player!");
            }
        });

        Placeholders.register(new ResourceLocation("player", "uuid"), (ctx, arg) -> {
            if (ctx.hasPlayer()) {
                return PlaceholderResult.value(ctx.player().getStringUUID());
            } else if (ctx.hasGameProfile()) {
                return PlaceholderResult.value(Component.nullToEmpty("" + ctx.gameProfile().getId()));
            } else {
                return PlaceholderResult.invalid("No player!");
            }
        });

        Placeholders.register(new ResourceLocation("player", "health"), (ctx, arg) -> {
            if (ctx.hasPlayer()) {
                return PlaceholderResult.value(String.format("%.0f", ctx.player().getHealth()));
            } else {
                return PlaceholderResult.invalid("No player!");
            }
        });

        Placeholders.register(new ResourceLocation("player", "max_health"), (ctx, arg) -> {
            if (ctx.hasPlayer()) {
                return PlaceholderResult.value(String.format("%.0f", ctx.player().getMaxHealth()));
            } else {
                return PlaceholderResult.invalid("No player!");
            }
        });

        Placeholders.register(new ResourceLocation("player", "hunger"), (ctx, arg) -> {
            if (ctx.hasPlayer()) {
                return PlaceholderResult.value(String.format("%.0f", ctx.player().getFoodData().getFoodLevel()));
            } else {
                return PlaceholderResult.invalid("No player!");
            }
        });

        Placeholders.register(new ResourceLocation("player", "saturation"), (ctx, arg) -> {
            if (ctx.hasPlayer()) {
                return PlaceholderResult.value(String.format("%.0f", ctx.player().getFoodData().getSaturationLevel()));
            } else {
                return PlaceholderResult.invalid("No player!");
            }
        });
    }
}
