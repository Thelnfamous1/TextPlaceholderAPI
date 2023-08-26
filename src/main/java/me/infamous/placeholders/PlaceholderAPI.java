package me.infamous.placeholders;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(PlaceholderAPI.MODID)
public class PlaceholderAPI {
    public static final String MODID = "placeholder_api";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PlaceholderAPI() {}
}
