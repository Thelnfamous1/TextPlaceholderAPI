package eu.pb4.placeholders.api.node;

import eu.pb4.placeholders.api.ParserContext;
import eu.pb4.placeholders.api.node.parent.ParentNode;
import eu.pb4.placeholders.impl.GeneralUtils;
import net.minecraft.network.chat.Component;

public interface TextNode {
    Component toText(ParserContext context, boolean removeSingleSlash);

    static TextNode convert(Component input) {
        return GeneralUtils.convertToNodes(input);
    }

    static TextNode of(String input) {
        return new LiteralNode(input);
    }

    static TextNode wrap(TextNode... nodes) {
        return new ParentNode(nodes);
    }

    static TextNode asSingle(TextNode... nodes) {
        return switch (nodes.length) {
            case 0 -> EmptyNode.INSTANCE;
            case 1 -> nodes[0];
            default -> wrap(nodes);
        };
    }

    static TextNode empty() {
        return EmptyNode.INSTANCE;
    }
}
