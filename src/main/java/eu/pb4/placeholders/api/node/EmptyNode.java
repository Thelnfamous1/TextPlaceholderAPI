package eu.pb4.placeholders.api.node;

import eu.pb4.placeholders.api.ParserContext;
import net.minecraft.network.chat.Component;

public record EmptyNode() implements TextNode {
    public static final EmptyNode INSTANCE = new EmptyNode();
    @Override
    public Component toText(ParserContext context, boolean removeSingleSlash) {
        return Component.empty();
    }
}
