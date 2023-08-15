package eu.pb4.placeholders.api.node.parent;

import eu.pb4.placeholders.api.ParserContext;
import eu.pb4.placeholders.api.node.TextNode;
import eu.pb4.placeholders.impl.GeneralUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;

public final class GradientNode extends ParentNode {
    private final GradientProvider gradientProvider;

    public GradientNode(TextNode[] children, GradientProvider gradientBuilder) {
        super(children);
        this.gradientProvider = gradientBuilder;
    }

    @Override
    protected Component applyFormatting(MutableComponent out, ParserContext context) {
        return GeneralUtils.toGradient(out, this.gradientProvider);
    }

    @Override
    public ParentTextNode copyWith(TextNode[] children) {
        return new GradientNode(children, this.gradientProvider);
    }

    @FunctionalInterface
    public interface GradientProvider {
        TextColor getColorAt(int index, int length);
    }
}
