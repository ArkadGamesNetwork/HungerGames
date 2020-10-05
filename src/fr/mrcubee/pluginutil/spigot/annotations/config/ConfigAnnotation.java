package fr.mrcubee.pluginutil.spigot.annotations.config;

import java.lang.reflect.Field;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigAnnotation {

	private static void loadField(FileConfiguration config, Object object, Field field, Config configValue) {
		Object value;

		if (config == null  || object == null || field == null || configValue == null
		|| config.contains(configValue.path()))
			return;
		field.setAccessible(true);
		value = config.get(configValue.path());
		if (configValue.color() && (value instanceof String))
			value = ChatColor.translateAlternateColorCodes(configValue.colorChar(), (String) value);
		try {
			if (value != null)
				field.set(object, value);
		} catch (Exception ignored) {}
	}

    public static void loadClass(FileConfiguration fileConfiguration, Object... objects) {
        if (fileConfiguration == null || objects == null)
            return;
        for (Object object : objects)
            for (Field field : object.getClass().getDeclaredFields())
                loadField(fileConfiguration, object, field, field.getAnnotation(Config.class));
    }
}