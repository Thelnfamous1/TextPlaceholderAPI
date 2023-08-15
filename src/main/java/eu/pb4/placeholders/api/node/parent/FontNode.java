package eu.pb4.placeholders.api.node.parent;

import eu.pb4.placeholders.api.ParserContext;
import eu.pb4.placeholders.api.node.TextNode;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public final class FontNode extends ParentNode {
    private final ResourceLocation font;

    public FontNode(TextNode[] children, ResourceLocation font) {
        super(children);
        this.font = font;
    }

    @Override
    protected Component applyFormatting(MutableComponent out, ParserContext context) {
        return out.setStyle(out.getStyle().withFont(font));
    }

    @Override
    public ParentTextNode copyWith(TextNode[] children) {
        return new FontNode(children, this.font);
    }
}
