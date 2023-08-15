package eu.pb4.placeholders.api.node;

import eu.pb4.placeholders.api.ParserContext;
import net.minecraft.network.chat.Component;

public record KeybindNode(String value) implements TextNode {
    @Override
    public Component toText(ParserContext context, boolean removeSingleSlash) {
        return Component.keybind(this.value());
    }
}
