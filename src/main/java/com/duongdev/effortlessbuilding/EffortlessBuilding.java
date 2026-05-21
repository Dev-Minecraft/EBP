package com.duongdev.effortlessbuilding;

import com.duongdev.effortlessbuilding.commands.EBCommand;
import com.duongdev.effortlessbuilding.listeners.BuildingListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EffortlessBuilding extends JavaPlugin {

    private static EffortlessBuilding instance;
    private final Map<UUID, PlayerBuildingState> playerStates = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        
        String version = getServer().getBukkitVersion();
        getLogger().info("Detected server version: " + version);
        
        getCommand("eb").setExecutor(new EBCommand());
        getServer().getPluginManager().registerEvents(new BuildingListener(), this);
        
        getLogger().info("EffortlessBuildingPlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("EffortlessBuildingPlugin has been disabled!");
    }

    public static EffortlessBuilding getInstance() {
        return instance;
    }

    public PlayerBuildingState getPlayerState(UUID uuid) {
        return playerStates.computeIfAbsent(uuid, k -> new PlayerBuildingState());
    }

    public static class PlayerBuildingState {
        private boolean mirrorEnabled = false;
        private String mirrorAxis = "x";
        private org.bukkit.Location mirrorCenter = null;
        private int arrayCount = 0;
        private String arrayDirection = "up";
        private int radialCount = 0;
        private org.bukkit.Location radialCenter = null;
        private int gridX = 0;
        private int gridZ = 0;
        private final java.util.Stack<java.util.List<org.bukkit.block.BlockState>> undoHistory = new java.util.Stack<>();

        public boolean isMirrorEnabled() { return mirrorEnabled; }
        public void setMirrorEnabled(boolean mirrorEnabled) { this.mirrorEnabled = mirrorEnabled; }

        public String getMirrorAxis() { return mirrorAxis; }
        public void setMirrorAxis(String mirrorAxis) { this.mirrorAxis = mirrorAxis; }

        public org.bukkit.Location getMirrorCenter() { return mirrorCenter; }
        public void setMirrorCenter(org.bukkit.Location mirrorCenter) { this.mirrorCenter = mirrorCenter; }

        public int getRadialCount() { return radialCount; }
        public void setRadialCount(int radialCount) { this.radialCount = radialCount; }

        public org.bukkit.Location getRadialCenter() { return radialCenter; }
        public void setRadialCenter(org.bukkit.Location radialCenter) { this.radialCenter = radialCenter; }

        public int getGridX() { return gridX; }
        public void setGridX(int gridX) { this.gridX = gridX; }

        public int getGridZ() { return gridZ; }
        public void setGridZ(int gridZ) { this.gridZ = gridZ; }

        public int getArrayCount() { return arrayCount; }
        public void setArrayCount(int arrayCount) { this.arrayCount = arrayCount; }

        public String getArrayDirection() { return arrayDirection; }
        public void setArrayDirection(String arrayDirection) { this.arrayDirection = arrayDirection; }

        public java.util.Stack<java.util.List<org.bukkit.block.BlockState>> getUndoHistory() { return undoHistory; }
    }
}
