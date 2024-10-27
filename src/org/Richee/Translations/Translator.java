package org.Richee.Translations;

import org.Richee.Core;
import org.Richee.Prefix;
import org.bukkit.ChatColor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class Translator {
    private static final Pattern translationPattern = Pattern.compile("^((?:\\w\\.?)+): (\"(.+)\"|\\|)");
    private static final Pattern appendPattern = Pattern.compile("^ {2}(.*)");
    private static final Pattern filePattern = Pattern.compile("translations/\\w{2}/((?:\\w+/?)+)\\.ya?ml");

    private static HashMap<String, String> translations;

    public static void LoadTranslations(String langId) throws IOException, URISyntaxException {
        Core.log(Level.INFO, "Loading translation \"" + langId + "\"");

        translations = new HashMap<>();

        var jar = new JarFile(new File(Translator.class.getProtectionDomain().getCodeSource().getLocation().toURI()));
        var entries = jar.entries();

        while (entries.hasMoreElements()) {
            var fileName = entries.nextElement().getName();
            var fileMatch = filePattern.matcher(fileName);

            if (fileMatch.matches()) {
                var reader = new BufferedReader(new InputStreamReader(
                    Translator.class.getResourceAsStream("/" + fileName)
                ));

                var append = false;
                String line, key = null, value = null;

                while ((line = reader.readLine()) != null) {
                    if (!line.startsWith("#")) {
                        if (append) {
                            var appendMatcher = appendPattern.matcher(line);

                            if (appendMatcher.matches()) {
                                value += appendMatcher.group(1) + "\n";

                            } else {
                                Core.log(Level.FINE, key + ": " + value.replace("\n", "<br/>"));
                                translations.put(key, value);
                                append = false;
                            }
                        }

                        var translationMatch = translationPattern.matcher(line);

                        if (translationMatch.matches()) {
                            if (translationMatch.group(2).equals("|")) {
                                key = fileMatch.group(1).replace('/', '.') + "." + translationMatch.group(1);
                                value = "";
                                append = true;
                            } else {
                                key = fileMatch.group(1).replace('/', '.') + "." + translationMatch.group(1);
                                value = translationMatch.group(3);
                                Core.log(Level.FINE, key + ": " + value);
                                translations.put(
                                    key,
                                    value
                                );
                            }
                        }
                    }
                }

                // Handle end of file, when last line was appending
                if (!translations.containsKey(key)) {
                    translations.put(key, value);
                }
            }
        }

        jar.close();
    }

    public static String id(String id, Object... params) {
        return id(Prefix.NONE, id, params);
    }

    public static String id(Prefix prefix, String id, Object... params) {
        return ChatColor.translateAlternateColorCodes(
            '&',
            (
                prefix
                + (
                    translations != null && translations.containsKey(id)
                        ? String.format(translations.get(id), params)
                        : id
                )
            )
            .replace("\\","")
        );
    }
}
