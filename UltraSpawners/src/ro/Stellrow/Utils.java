package ro.Stellrow;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static String asColor(String toTranslate){
        return ChatColor.translateAlternateColorCodes('&',toTranslate);
    }
    public static List<String> loreAsColor(List<String> toTranslate){
        List<String> toRet = new ArrayList<>();
        for(String s : toTranslate){
            toRet.add(asColor(s));
        }
        return toRet;
    }
}
