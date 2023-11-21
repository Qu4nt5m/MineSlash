package me.theboykiss.ovh.mineslash.Handlers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;

public class Utils {
    private static final Pattern HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})");
    private static final Pattern FORMAT_PATTERN = Pattern.compile("&[0-9A-Fa-fK-Ok-oRrLlMmNn]");

    private static String colorize(String text) {
        Matcher matcher = FORMAT_PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer();

        while(matcher.find()) {
            String formatCode = matcher.group();
            char formatChar = formatCode.charAt(1);
            ChatColor format = getByFormatCode(formatChar);
            matcher.appendReplacement(sb, format.toString());
        }

        matcher.appendTail(sb);
        return sb.toString();
    }

    private static ChatColor getByFormatCode(char code) {
        switch(code) {
            case '0':
                return ChatColor.BLACK;
            case '1':
                return ChatColor.DARK_BLUE;
            case '2':
                return ChatColor.DARK_GREEN;
            case '3':
                return ChatColor.DARK_AQUA;
            case '4':
                return ChatColor.DARK_RED;
            case '5':
                return ChatColor.DARK_PURPLE;
            case '6':
                return ChatColor.GOLD;
            case '7':
                return ChatColor.GRAY;
            case '8':
                return ChatColor.DARK_GRAY;
            case '9':
                return ChatColor.BLUE;
            case ':':
            case ';':
            case '<':
            case '=':
            case '>':
            case '?':
            case '@':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'P':
            case 'Q':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
            case '[':
            case '\\':
            case ']':
            case '^':
            case '_':
            case '`':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'p':
            case 'q':
            default:
                return ChatColor.RESET;
            case 'A':
            case 'a':
                return ChatColor.GREEN;
            case 'B':
            case 'b':
                return ChatColor.AQUA;
            case 'C':
            case 'c':
                return ChatColor.RED;
            case 'D':
            case 'd':
                return ChatColor.LIGHT_PURPLE;
            case 'E':
            case 'e':
                return ChatColor.YELLOW;
            case 'F':
            case 'f':
                return ChatColor.WHITE;
            case 'K':
            case 'k':
                return ChatColor.MAGIC;
            case 'L':
            case 'l':
                return ChatColor.BOLD;
            case 'M':
            case 'm':
                return ChatColor.STRIKETHROUGH;
            case 'N':
            case 'n':
                return ChatColor.UNDERLINE;
            case 'O':
            case 'o':
                return ChatColor.ITALIC;
            case 'R':
            case 'r':
                return ChatColor.RESET;
        }
    }

    private static String hexColors(String text) {
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer();

        while(matcher.find()) {
            String colorString = matcher.group(1);
            System.out.println("Color HEX encontrado: " + colorString); // Debugging
            ChatColor color = ChatColor.of("#" + colorString);
            matcher.appendReplacement(sb, color.toString());
        }

        matcher.appendTail(sb);
        return sb.toString();
    }


    public static String format(String text) {
        text = colorize(text); // Procesa los colores tradicionales &a, &b, etc.
        text = hexColors(text); // Procesa los colores HEX
        return text;
    }


}