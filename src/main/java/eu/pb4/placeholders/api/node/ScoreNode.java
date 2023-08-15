package eu.pb4.placeholders.api.node;

import eu.pb4.placeholders.api.ParserContext;
import net.minecraft.network.chat.Component;

public record ScoreNode(String name, String objective) implements TextNode {
    @Override
    public Component toText(ParserContext context, boolean removeSingleSlash) {
        return Component.score(name, objective);
    }
}
