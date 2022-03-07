package me.codedred.xpbottles.versions;

import me.codedred.xpbottles.Main;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ExpBottle {
	private Main plugin;

	private String displayName;
	private List<String> lore = new ArrayList<String>();
	private boolean glow;

	public ExpBottle(Main plugin) {
		this.plugin = plugin;
		displayName = plugin.getConfig().getString("bottle.name");
		lore = plugin.getConfig().getStringList("bottle.lore");
		glow = plugin.getConfig().getBoolean("bottle.glow");
	}


	public boolean hasValue(ItemStack item) {
		if (item.hasItemMeta())
			return true;
		return false;
	}

	// TODO update this?
	public int getExpAmount(ItemStack item) {
		if (item.getItemMeta().getLore() != null && item.getItemMeta().getLore().size() > 0) {

			for (String lore : item.getItemMeta().getLore()) {

				if (lore.toUpperCase().contains("Exp")) {
					String[] expArray = lore.split(":")[0].trim().replace("§", "&").split("&");
					String value = "";

					if (expArray.length > 1) {
						value = expArray[expArray.length - 1].substring(1, expArray[expArray.length - 1].length());
					}

					return Integer.parseInt(value);
				}
			}
		}

		return 0;
	}

	public ItemStack getBottle(Player player, int exp) {
		ItemStack item = getItemStack(player, exp);

		return item;
	}

	private ItemStack getItemStack(Player player, int exp) {
		ItemStack item = new ItemStack(Material.getMaterial("EXPERIENCE_BOTTLE"), 1);
		ItemMeta meta = item.getItemMeta();

		List<String> updatedLore = new ArrayList<String>();
		for (String l : lore)
			updatedLore.add(plugin.f(l.replace("%signer%", player.getName()).replace("%exp%", Integer.toString(exp))));

		meta.setLore(updatedLore);

		meta.setDisplayName(plugin.f(displayName));

		if (glow) {
			meta.addEnchant(Enchantment.DURABILITY, 0, true);
			meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
		}
		meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
		item.setItemMeta(meta);
		return item;
	}
}