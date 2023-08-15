package eu.pb4.placeholders.impl;

import eu.pb4.placeholders.api.node.*;
import eu.pb4.placeholders.api.node.parent.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.contents.KeybindContents;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.ScoreContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;


@ApiStatus.Internal
public class GeneralUtils {
    public static String durationToString(long x) {
        long seconds = x % 60;
        long minutes = (x / 60) % 60;
        long hours = (x / (60 * 60)) % 24;
        long days = x / (60 * 60 * 24);

        if (days > 0) {
            return String.format("%dd%dh%dm%ds", days, hours, minutes, seconds);
        } else if (hours > 0) {
            return String.format("%dh%dm%ds", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%dm%ds", minutes, seconds);
        } else if (seconds > 0) {
            return String.format("%ds", seconds);
        } else {
            return "---";
        }
    }

    public static boolean isEmpty(Component text) {
        return (
                text.getContents() == ComponentContents.EMPTY
                || (text.getContents() instanceof LiteralContents l && l.text().isEmpty())
               ) && text.getSiblings().isEmpty();
    }

    public static MutableComponent toGradient(Component base, GradientNode.GradientProvider posToColor) {
        return recursiveGradient(base, posToColor, 0, getGradientLength(base)).text();
    }

    private static int getGradientLength(Component base) {
        int length = base.getContents() instanceof LiteralContents l ? l.text().length() : base.getContents() == ComponentContents.EMPTY ? 0 : 1;

        for (var text : base.getSiblings()) {
            length += getGradientLength(text);
        }

        return length;
    }

    private static TextLengthPair recursiveGradient(Component base, GradientNode.GradientProvider posToColor, int pos, int totalLength) {
        if (base.getStyle().getColor() == null) {
            MutableComponent out = Component.empty().setStyle(base.getStyle());
            if (base.getContents() instanceof LiteralContents literalTextContent) {
                for (String letter : literalTextContent.text().replaceAll("\\p{So}|.", "$0\0").split("\0+")) {
                    if (!letter.isEmpty()) {
                        out.append(Component.literal(letter).setStyle(Style.EMPTY.withColor(posToColor.getColorAt(pos++, totalLength))));

                    }
                }
            } else {
                out.append(base.plainCopy().setStyle(Style.EMPTY.withColor(posToColor.getColorAt(pos++, totalLength))));

            }

            for (Component sibling : base.getSiblings()) {
                var pair = recursiveGradient(sibling, posToColor, pos, totalLength);
                pos = pair.length;
                out.append(pair.text);
            }
            return new TextLengthPair(out, pos);
        }
        return new TextLengthPair(base.copy(), pos + base.getString().length());
    }

    public static int hvsToRgb(float hue, float saturation, float value) {
        int h = (int) (hue * 6) % 6;
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        return switch (h) {
            case 0 -> rgbToInt(value, t, p);
            case 1 -> rgbToInt(q, value, p);
            case 2 -> rgbToInt(p, value, t);
            case 3 -> rgbToInt(p, q, value);
            case 4 -> rgbToInt(t, p, value);
            case 5 -> rgbToInt(value, p, q);
            default -> 0;
        };
    }

    public static int rgbToInt(float r, float g, float b) {
        return (((int) (r * 0xff)) & 0xFF) << 16 | (((int) (g * 0xff)) & 0xFF) << 8 | (((int) (b * 0xff) & 0xFF));
    }

    public static HSV rgbToHsv(int rgb) {
        float b = (float) (rgb % 256) / 255;
        rgb = rgb >> 8;
        float g = (float) (rgb % 256) / 255;
        rgb = rgb >> 8;
        float r = (float) (rgb % 256) / 255;

        float cmax = Math.max(r, Math.max(g, b));
        float cmin = Math.min(r, Math.min(g, b));
        float diff = cmax - cmin;
        float h = -1, s = -1;

        if (cmax == cmin) {
            h = 0;
        } else if (cmax == r) {
            h = (0.1666f * ((g - b) / diff) + 1) % 1;
        } else if (cmax == g) {
            h = (0.1666f * ((b - r) / diff) + 0.333f) % 1;
        } else if (cmax == b) {
            h = (0.1666f * ((r - g) / diff) + 0.666f) % 1;
        }
        if (cmax == 0) {
            s = 0;
        } else {
            s = (diff / cmax);
        }

        return new HSV(h, s, cmax);
    }

    public static Component removeHoverAndClick(Component input) {
        var output = cloneText(input);
        removeHoverAndClick(output);
        return output;
    }

    private static void removeHoverAndClick(MutableComponent input) {
        if (input.getStyle() != null) {
            input.setStyle(input.getStyle().withHoverEvent(null).withClickEvent(null));
        }

        if (input.getContents() instanceof TranslatableContents text) {
            for (int i = 0; i < text.getArgs().length; i++) {
                var arg = text.getArgs()[i];
                if (arg instanceof MutableComponent argText) {
                    removeHoverAndClick(argText);
                }
            }
        }

        for (var sibling : input.getSiblings()) {
            removeHoverAndClick((MutableComponent) sibling);
        }

    }

    public static MutableComponent cloneText(Component input) {
        MutableComponent baseText;
        if (input.getContents() instanceof TranslatableContents translatable) {
            var obj = new ArrayList<>();

            for (var arg : translatable.getArgs()) {
                if (arg instanceof Component argText) {
                    obj.add(cloneText(argText));
                } else {
                    obj.add(arg);
                }
            }

            baseText = Component.translatable(translatable.getKey(), obj.toArray());
        } else {
            baseText = input.plainCopy();
        }

        for (var sibling : input.getSiblings()) {
            baseText.append(cloneText(sibling));
        }

        baseText.setStyle(input.getStyle());
        return baseText;
    }

    public static Component getItemText(ItemStack stack) {
        if (!stack.isEmpty()) {
            MutableComponent mutableText = Component.empty().append(stack.getDisplayName());
            if (stack.hasCustomHoverName()) {
                mutableText.withStyle(ChatFormatting.ITALIC);
            }

            mutableText.withStyle(stack.getRarity().getStyleModifier()).withStyle((style) -> {
                return style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(stack)));
            });

            return mutableText;
        }

        return Component.empty().append(ItemStack.EMPTY.getDisplayName());
    }

    public static ParentNode convertToNodes(Component input) {
        var list = new ArrayList<TextNode>();

        if (input.getContents() instanceof LiteralContents content) {
            list.add(new LiteralNode(content.text()));
        } else if (input.getContents() instanceof TranslatableContents content) {
            var args = new ArrayList<>();
            for (var arg : content.getArgs()) {
                if (arg instanceof Component text) {
                    args.add(convertToNodes(text));
                } else if (arg instanceof String s) {
                    args.add(new LiteralNode(s));
                } else {
                    args.add(arg);
                }
            }


            list.add(new TranslatedNode(content.getKey(), args.toArray()));
        } else if (input.getContents() instanceof ScoreContents content) {
            list.add(new ScoreNode(content.getName(), content.getObjective()));
        } else if (input.getContents() instanceof KeybindContents content) {
            list.add(new KeybindNode(content.getName()));
        }


        for (var child : input.getSiblings()) {
            list.add(convertToNodes(child));
        }

        if (input.getStyle() == Style.EMPTY) {
            return new ParentNode(list.toArray(new TextNode[0]));
        } else {
            var style = input.getStyle();
            var hoverValue = style.getHoverEvent() != null && style.getHoverEvent().getAction() == HoverEvent.Action.SHOW_TEXT
                    ? convertToNodes(style.getHoverEvent().getValue(HoverEvent.Action.SHOW_TEXT)) : null;

            var clickValue = style.getClickEvent() != null ? new LiteralNode(style.getClickEvent().getValue()) : null;
            var insertion = style.getInsertion() != null ? new LiteralNode(style.getInsertion()) : null;

            return new StyledNode(list.toArray(new TextNode[0]), style, hoverValue, clickValue, insertion);
        }
    }

    public static TextNode removeColors(TextNode node) {
        if (node instanceof ParentTextNode parentNode) {
            var list = new ArrayList<TextNode>();

            for (var child : parentNode.getChildren()) {
                list.add(removeColors(child));
            }

            if (node instanceof ColorNode || node instanceof ChatFormattingNode) {
                return new ParentNode(list.toArray(new TextNode[0]));
            } else if (node instanceof StyledNode styledNode) {
                return new StyledNode(list.toArray(new TextNode[0]), styledNode.rawStyle().withColor((TextColor) null), styledNode.hoverValue(), styledNode.clickValue(), styledNode.insertion());
            }

            return parentNode.copyWith(list.toArray(new TextNode[0]));
        } else {
            return node;
        }
    }

    public record HSV(float h, float s, float v) {
    }

    public record TextLengthPair(MutableComponent text, int length) {
        public static final TextLengthPair EMPTY = new TextLengthPair(null, 0);
    }

    public record Pair<L, R>(L left, R right) {
    }
}
