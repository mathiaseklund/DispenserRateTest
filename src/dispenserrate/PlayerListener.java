package dispenserrate;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;

import net.md_5.bungee.api.ChatColor;

public class PlayerListener implements Listener
{

	private static PlayerListener instance = new PlayerListener();

	Main plugin = Main.getMain();

	public static PlayerListener getInstance()
	{
		return instance;
	}

	@EventHandler
	public void onBlockDispense(BlockDispenseEvent event)
	{
		Block block = event.getBlock();
		Location loc = block.getLocation();
		if (event.getItem() != null)
		{
			// Check if the item type is TNT
			if (event.getItem().getType() == Material.TNT)
			{
				// Get the location of the dispenser as a string.
				String location = locToString(event.getBlock().getLocation());
				// Check the cooldowns list if it contains the dispensers
				// location.
				if (!Lists.cooldowns.contains(location))
				{
					int cooldownTime = plugin.config.getInt("cooldown");
					Lists.cooldowns.add(location);
					loc.add(0, -0.5, 0);
					spawnCooldownHologram(loc, cooldownTime);
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
					{
						@Override
						public void run()
						{
							Lists.cooldowns.remove(location);
						}
					}, cooldownTime * 20);
				} else
				{
					// Cancel the event prohibiting the tnt from getting
					// dispensed.
					event.setCancelled(true);
				}
			}
		}
	}

	// Spawn the hologram object.
	public void spawnCooldownHologram(Location loc, int cooldownTime)
	{
		if (cooldownTime > 0)
		{
			ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
			as.setGravity(false);
			as.setCustomName("Cooldown: " + ChatColor.RED + cooldownTime);
			as.setCustomNameVisible(true);
			as.setVisible(false);
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
			{
				@Override
				public void run()
				{
					as.remove();
					spawnCooldownHologram(loc, (cooldownTime - 1));
				}
			}, 20);
		}
	}

	// Turns a Location object into a String object for storage.
	public String locToString(Location loc)
	{
		String location = "";
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		double x1 = loc.getDirection().getX();
		double y1 = loc.getDirection().getY();
		double z1 = loc.getDirection().getZ();
		String world = loc.getWorld().getName();
		location = x + ":" + y + ":" + z + ":" + world + ":" + x1 + ":" + y1 + ":" + z1;
		return location;
	}
}