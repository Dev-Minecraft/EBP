package com.duongdev.effortlessbuilding.listeners;

import com.duongdev.effortlessbuilding.EffortlessBuilding;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

public class BuildingListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        EffortlessBuilding.PlayerBuildingState state = EffortlessBuilding.getInstance().getPlayerState(player.getUniqueId());
        
        Block block = event.getBlock();
        Material material = block.getType();
        
        java.util.List<org.bukkit.block.BlockState> changes = new java.util.ArrayList<>();
        
        // Record the state BEFORE it was placed? 
        // In BlockPlaceEvent, the block in the world is ALREADY the new block.
        // We can use event.getBlockReplacedState() to get what was there before.
        changes.add(event.getBlockReplacedState());
        
        if (state.isMirrorEnabled() && state.getMirrorCenter() != null) {
            placeMirroredBlock(player, block, state, material, changes);
        }
        
        if (state.getArrayCount() > 0) {
            placeArrayBlocks(player, block, state, material, changes);
        }

        if (state.getRadialCount() > 0 && state.getRadialCenter() != null) {
            placeRadialBlocks(player, block, state, material, changes);
        }

        if (state.getGridX() > 0 || state.getGridZ() > 0) {
            placeGridBlocks(player, block, state, material, changes);
        }
        
        if (changes.size() > 1) { // Only record if it actually did something effortless (or at least our mirroring/array worked)
            state.getUndoHistory().push(changes);
        } else {
            // If only one block was placed (the original), we might still want to undo it?
            // "Effortless" mod usually allows undoing even single blocks.
            state.getUndoHistory().push(changes);
        }
    }

    private void placeMirroredBlock(Player player, Block original, EffortlessBuilding.PlayerBuildingState state, Material material, java.util.List<org.bukkit.block.BlockState> changes) {
        Location center = state.getMirrorCenter();
        Location loc = original.getLocation();
        String axis = state.getMirrorAxis();
        
        double diff;
        Location mirroredLoc = loc.clone();
        
        switch (axis) {
            case "x":
                diff = loc.getX() - center.getX();
                mirroredLoc.setX(center.getX() - diff);
                break;
            case "y":
                diff = loc.getY() - center.getY();
                mirroredLoc.setY(center.getY() - diff);
                break;
            case "z":
                diff = loc.getZ() - center.getZ();
                mirroredLoc.setZ(center.getZ() - diff);
                break;
        }
        
        if (!mirroredLoc.equals(loc)) {
            Block mirroredBlock = mirroredLoc.getBlock();
            changes.add(mirroredBlock.getState());
            mirroredBlock.setType(material);
        }
    }

    private void placeArrayBlocks(Player player, Block original, EffortlessBuilding.PlayerBuildingState state, Material material, java.util.List<org.bukkit.block.BlockState> changes) {
        Vector direction;
        switch (state.getArrayDirection()) {
            case "up": direction = new Vector(0, 1, 0); break;
            case "down": direction = new Vector(0, -1, 0); break;
            case "north": direction = new Vector(0, 0, -1); break;
            case "south": direction = new Vector(0, 0, 1); break;
            case "east": direction = new Vector(1, 0, 0); break;
            case "west": direction = new Vector(-1, 0, 0); break;
            default: return;
        }
        
        Location loc = original.getLocation();
        for (int i = 1; i <= state.getArrayCount(); i++) {
            Block arrayBlock = loc.add(direction).getBlock();
            changes.add(arrayBlock.getState());
            arrayBlock.setType(material);
        }
    }

    private void placeRadialBlocks(Player player, Block original, EffortlessBuilding.PlayerBuildingState state, Material material, java.util.List<org.bukkit.block.BlockState> changes) {
        Location center = state.getRadialCenter();
        Location loc = original.getLocation();
        int count = state.getRadialCount();
        
        double cx = center.getX();
        double cz = center.getZ();
        double x = loc.getX();
        double z = loc.getZ();
        
        double angleStep = 2 * Math.PI / count;
        
        for (int i = 1; i < count; i++) {
            double angle = angleStep * i;
            double dx = x - cx;
            double dz = z - cz;
            
            double nx = cx + dx * Math.cos(angle) - dz * Math.sin(angle);
            double nz = cz + dx * Math.sin(angle) + dz * Math.cos(angle);
            
            Location radialLoc = new Location(loc.getWorld(), Math.round(nx), loc.getY(), Math.round(nz));
            if (!radialLoc.equals(loc)) {
                Block radialBlock = radialLoc.getBlock();
                changes.add(radialBlock.getState());
                radialBlock.setType(material);
            }
        }
    }

    private void placeGridBlocks(Player player, Block original, EffortlessBuilding.PlayerBuildingState state, Material material, java.util.List<org.bukkit.block.BlockState> changes) {
        Location loc = original.getLocation();
        int gx = state.getGridX();
        int gz = state.getGridZ();
        
        for (int i = 0; i <= gx; i++) {
            for (int j = 0; j <= gz; j++) {
                if (i == 0 && j == 0) continue;
                
                Location gridLoc = loc.clone().add(i, 0, j);
                Block gridBlock = gridLoc.getBlock();
                changes.add(gridBlock.getState());
                gridBlock.setType(material);
            }
        }
    }
}
