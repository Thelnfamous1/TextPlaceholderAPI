package eu.pb4.placeholders.api.node.parent;

import eu.pb4.placeholders.api.ParserContext;
import eu.pb4.placeholders.api.node.TextNode;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public final class ChatFormattingNode extends ParentNode {
    private final ChatFormatting formatting;

    public ChatFormattingNode(TextNode[] children, ChatFormatting formatting) {
        super(children);
        this.formatting = formatting;
    }

    @Override
    protected Component applyFormatting(MutableComponent out, ParserContext context) {
        return out.withStyle(this.formatting);
    }

    @Override
    public ParentTextNode copyWith(TextNode[] children) {
        return new ChatFormattingNode(children, this.formatting);
    }
}
