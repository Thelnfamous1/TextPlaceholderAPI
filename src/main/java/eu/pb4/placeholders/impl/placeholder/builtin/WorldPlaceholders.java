package eu.pb4.placeholders.impl.placeholder.builtin;

import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WorldPlaceholders {
    static final int CHUNK_AREA = (int)Math.pow(17.0D, 2.0D);

    public static void register() {
        Placeholders.register(new ResourceLocation("world", "time"), (ctx, arg) -> {
            ServerLevel world;
            if (ctx.player() != null) {
                world = ctx.player().getLevel();
            } else {
                world = ctx.server().overworld();
            }

            long dayTime = (long) (world.getTimeOfDay(0.0F) * 3.6 / 60);

            return PlaceholderResult.value(String.format("%02d:%02d", (dayTime / 60 + 6) % 24, dayTime % 60));
        });

        Placeholders.register(new ResourceLocation("world", "time_alt"), (ctx, arg) -> {
            ServerLevel world;
            if (ctx.player() != null) {
                world = ctx.player().getLevel();
            } else {
                world = ctx.server().overworld();
            }

            long dayTime = (long) (world.getTimeOfDay(0.0F) * 3.6 / 60);
            long x = (dayTime / 60 + 6) % 24;
            long y = x % 12;
            if (y == 0) {
                y = 12;
            }
            return PlaceholderResult.value(String.format("%02d:%02d %s", y, dayTime % 60, x > 11 ? "PM" : "AM" ));
        });

        Placeholders.register(new ResourceLocation("world", "day"), (ctx, arg) -> {
            ServerLevel world;
            if (ctx.player() != null) {
                world = ctx.player().getLevel();
            } else {
                world = ctx.server().overworld();
            }

            return PlaceholderResult.value("" + world.getTimeOfDay(0.0F) / 24000);
        });

        Placeholders.register(new ResourceLocation("world", "id"), (ctx, arg) -> {
            ServerLevel world;
            if (ctx.player() != null) {
                world = ctx.player().getLevel();
            } else {
                world = ctx.server().overworld();
            }

            return PlaceholderResult.value(world.dimension().location().toString());
        });

        Placeholders.register(new ResourceLocation("world", "name"), (ctx, arg) -> {
            ServerLevel world;
            if (ctx.player() != null) {
                world = ctx.player().getLevel();
            } else {
                world = ctx.server().overworld();
            }
            List<String> parts = new ArrayList<>();
            {
                String[] words = world.dimension().location().getPath().split("_");
                for (String word : words) {
                    String[] s = word.split("", 2);
                    s[0] = s[0].toUpperCase(Locale.ROOT);
                    parts.add(String.join("", s));
                }
            }
            return PlaceholderResult.value(String.join(" ", parts));
        });



        Placeholders.register(new ResourceLocation("world", "player_count"), (ctx, arg) -> {
            ServerLevel world;
            if (ctx.player() != null) {
                world = ctx.player().getLevel();
            } else {
                world = ctx.server().overworld();
            }

            return PlaceholderResult.value("" + world.players().size());
        });

        Placeholders.register(new ResourceLocation("world", "mob_count"), (ctx, arg) -> {
            ServerLevel world;
            if (ctx.player() != null) {
                world = ctx.player().getLevel();
            } else {
                world = ctx.server().overworld();
            }

            NaturalSpawner.SpawnState info = world.getChunkSource().getLastSpawnState();

            MobCategory spawnGroup = null;
            if (arg != null) {
                spawnGroup = MobCategory.valueOf(arg.toUpperCase(Locale.ROOT));
            }

            if (spawnGroup != null) {
                return PlaceholderResult.value("" + info.getMobCategoryCounts().getInt(spawnGroup));
            } else {
                int x = 0;

                for (int value : info.getMobCategoryCounts().values()) {
                    x += value;
                }
                return PlaceholderResult.value("" + x);
            }
        });

        Placeholders.register(new ResourceLocation("world", "mob_cap"), (ctx, arg) -> {
            ServerLevel world;
            if (ctx.player() != null) {
                world = ctx.player().getLevel();
            } else {
                world = ctx.server().overworld();
            }

            NaturalSpawner.SpawnState info = world.getChunkSource().getLastSpawnState();

            MobCategory spawnGroup = null;
            if (arg != null) {
                spawnGroup = MobCategory.valueOf(arg.toUpperCase(Locale.ROOT));
            }

            if (spawnGroup != null) {
                return PlaceholderResult.value("" + spawnGroup.getMaxInstancesPerChunk() * info.getSpawnableChunkCount() / CHUNK_AREA);
            } else {
                int x = 0;

                for (MobCategory group : MobCategory.values()) {
                    x += group.getMaxInstancesPerChunk();
                }
                return PlaceholderResult.value("" + x * info.getSpawnableChunkCount() / CHUNK_AREA);
            }
        });
    }
}
