package org.Richee.Translations;

import org.Richee.Core;
import org.Richee.Severity;
import org.bukkit.ChatColor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translator {
    private static final Pattern translationPattern = Pattern.compile("((?:\\w\\.?)+): \"(.+)\"");
    private static final Pattern filePattern = Pattern.compile("translations/\\w{2}/((?:\\w+/?)+)\\.ya?ml");

    private static HashMap<String, String> translations;

    public static void LoadTranslations(String langId) throws IOException, URISyntaxException {
        Core.log(Severity.NONE, "Loading translation \"" + langId + "\"");

        translations = new HashMap<>();

        var jar = new JarFile(new File(Translator.class.getProtectionDomain().getCodeSource().getLocation().toURI()));
        var entries = jar.entries();

        while (entries.hasMoreElements()) {
            var s = entries.nextElement().getName();
            var fileMatch = filePattern.matcher(s);

            if (fileMatch.matches()) {
                var reader = new BufferedReader(new InputStreamReader(
                    Translator.class.getResourceAsStream("/" + s)
                ));

                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.startsWith("#")) {
                        Matcher translationMatch = translationPattern.matcher(line);

                        if (translationMatch.matches()) {
                            var key = fileMatch.group(1).replace('/', '.') + "." + translationMatch.group(1);
                            Core.log(Severity.DEBUG, key);
                            translations.put(key, translationMatch.group(2));
                        }
                    }
                }
            }
        }
    }

    public static String[] ids(String regex) {
        Pattern pattern = Pattern.compile(regex);
        List<String> ids = new ArrayList<>();

        for (String key : translations.keySet()) {
            if (pattern.matcher(key).matches()) {
                ids.add(key);
            }
        }

        return ids.toArray(new String[] {});
    }

    public static String id(String id, Object... params) {
        return id(Severity.NONE, id, params);
    }

    public static String id(Severity severity, String id, Object... params) {
        if (translations != null && translations.containsKey(id)) {
            String text = String.format(translations.get(id), params);
            String prefix = Core.getPrefix(severity);
            return ChatColor.translateAlternateColorCodes('&', (prefix + text).replace("\\",""));
        }

        return Core.getPrefix(severity) + id;
    }
}
