package game.check;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	String nms;
	String cb;
	Biome biomes[] = new Biome[] { Biome.PLAINS, Biome.DESERT, Biome.SAVANNA, Biome.TAIGA };

	public void onEnable() {
		String s = getServer().getClass().getName();
		this.nms = ("net.minecraft.server." + s.replace(".", ",").split(",")[3] + ".");
		this.cb = ("org.bukkit.craftbukkit." + s.replace(".", ",").split(",")[3] + ".");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You need to be a player to execute this command!");
			return true;
		}
		Player p = (Player) sender;
		if (!p.hasPermission("vg")) {
			sender.sendMessage(ChatColor.RED + "You don't have the permission to execute this command!");
			return true;
		}
		try {
			if (args.length == 5) {
				create(p.getLocation().getWorld(), Integer.parseInt(args[0]), Integer.parseInt(args[1]),
						Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
				return true;
			}
			if (args.length == 2) {
				Location l = p.getLocation();
				create(l.getWorld(), l.getBlockX(), l.getBlockZ(), Integer.parseInt(args[0]), 1000,
						Integer.parseInt(args[1]));
				return true;
			}
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "You need to enter numbers!");
			return true;
		} catch (ArrayIndexOutOfBoundsException e) {
			sender.sendMessage(ChatColor.RED + "You need to enter 0 to 3 in [type]!");
		}
		sender.sendMessage(ChatColor.RED + "/village [size] [type: oak(0),  sandstone(1),  acacia(2),  spruce(3)]");
		sender.sendMessage(ChatColor.RED
				+ "/village [x] [z] [size] [radian] [type: oak(0),  sandstone(1),  acacia(2),  spruce(3)]");
		return true;
	}

	public void create(World w, int i, int j, int k, int r, int t) {
		if (t < 0 || t > 3)
			throw new ArrayIndexOutOfBoundsException();

		if (nms.contains("1_13")) {
			Create_1_13.create(w, i, j, k, r, t);
			return;
		}

		try {
			Class<?> wg = Class.forName(this.nms + "WorldGenVillage$WorldGenVillageStart");
			Class<?> cw = Class.forName(this.cb + "CraftWorld");
			Class<?> sb = Class.forName(this.nms + "StructureBoundingBox");
			Class<Integer> Int = Integer.TYPE;
			Method cwm = cw.getMethod("getHandle", new Class[0]);
			Method wgm = wg.getMethod("a", new Class[] { Class.forName(this.nms + "World"), Random.class, sb });

			Constructor<?> sbc = sb.getConstructor(new Class[] { Int, Int, Int, Int });
			Object sbo = sbc.newInstance(new Object[] { Integer.valueOf(i - r), Integer.valueOf(j - r),
					Integer.valueOf(i + r), Integer.valueOf(j + r) });

			Constructor<?> wgc = wg
					.getConstructor(new Class[] { Class.forName(this.nms + "World"), Random.class, Int, Int, Int });

			Object wgo = wgc.newInstance(new Object[] { cwm.invoke(cw.cast(w), new Object[0]), new Random(),
					Integer.valueOf(i >> 4), Integer.valueOf(j >> 4), Integer.valueOf(k) });

			wgm.invoke(wgo, new Object[] { cwm.invoke(cw.cast(w), new Object[0]), new Random(), sbo });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
