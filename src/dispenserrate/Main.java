package dispenserrate;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{

	private static Main main;

	File configurationConfig;
	public FileConfiguration config;

	public static Main getMain()
	{
		return main;
	}

	public void onEnable()
	{
		main = this;
		configurationConfig = new File(getDataFolder(), "config.yml");
		config = YamlConfiguration.loadConfiguration(configurationConfig);
		if (!configurationConfig.exists())
		{
			loadConfig();
		}
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
	}

	public void savec()
	{
		try
		{
			config.save(configurationConfig);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void loadConfig()
	{
		config.addDefault("cooldown", 5);
		config.options().copyDefaults(true);
		savec();

	}

}
