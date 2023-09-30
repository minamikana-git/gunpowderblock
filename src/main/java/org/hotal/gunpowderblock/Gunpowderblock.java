package org.hotal.gunpowderblock;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class Gunpowderblock extends JavaPlugin implements Listener {

    private NamespacedKey gunpowderBlockKey;
    private NamespacedKey reverseGunpowderBlockKey;

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        gunpowderBlockKey = new NamespacedKey(this, "gunpowder_block");
        reverseGunpowderBlockKey = new NamespacedKey(this, "reverse_gunpowder_block");
        registerGunpowderBlockRecipe();
        registerReverseGunpowderBlockRecipe();
    }

    private ItemStack getCustomGunpowderBlock() {
        ItemStack gunpowderBlock = new ItemStack(Material.GUNPOWDER);
        ItemMeta meta = gunpowderBlock.getItemMeta();
        meta.setDisplayName("圧縮された火薬");
        meta.getPersistentDataContainer().set(new NamespacedKey(this, "gunpowderblock"), PersistentDataType.STRING, "gunpowder");
        gunpowderBlock.setItemMeta(meta);
        return gunpowderBlock;
    }

    private void registerGunpowderBlockRecipe() {
        ItemStack gunpowderBlock = getCustomGunpowderBlock();

        ShapedRecipe recipe = new ShapedRecipe(gunpowderBlockKey, gunpowderBlock);
        recipe.shape("GGG", "GGG", "GGG");
        recipe.setIngredient('G', Material.GUNPOWDER);

        this.getServer().addRecipe(recipe);
    }

    private void registerReverseGunpowderBlockRecipe() {
        ItemStack gunpowder = new ItemStack(Material.GUNPOWDER, 9);

        ShapedRecipe reverseRecipe = new ShapedRecipe(reverseGunpowderBlockKey, gunpowder);
        reverseRecipe.shape("E");
        reverseRecipe.setIngredient('E', new RecipeChoice.ExactChoice(getCustomGunpowderBlock()));

        this.getServer().addRecipe(reverseRecipe);
    }


    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        if (event.getRecipe() != null && event.getRecipe().getResult().getType() == Material.GUNPOWDER && event.getInventory().getSize() == 5) {
            event.getInventory().setResult(new ItemStack(Material.GUNPOWDER, 9));
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (event.getRecipe() != null && event.getRecipe() instanceof ShapedRecipe) {
            ShapedRecipe recipe = (ShapedRecipe) event.getRecipe();
            if (recipe.getKey().equals(reverseGunpowderBlockKey)) {
                event.setCurrentItem(new ItemStack(Material.GUNPOWDER, 9));
            }
        }
    }
}






