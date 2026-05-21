# EffortlessBuildingPlugin

A Spigot/Paper plugin inspired by the popular **Effortless Building** mod. This plugin brings core "effortless" building features to a server environment with broad version compatibility (1.8.8 - 1.21+).

## ✨ Features

- **Mirror Mode**: Place blocks mirrored across X, Y, or Z axes.
- **Array Mode**: Place multiple blocks in a line with one click.
- **Radial Clones**: Place blocks in a circular pattern (Clones).
- **Grid Clones**: Place blocks in a 2D grid area (Clones).
- **Undo System**: Easily revert your building actions with `/eb undo`.
- **Cross-Version Support**: Works on Spigot/Paper from 1.8.8 to 1.21.

## 📜 Commands

| Command | Description |
|---------|-------------|
| `/eb mirror <x\|y\|z\|off>` | Toggle mirroring on a specific axis |
| `/eb array <count> <dir\|off>` | Toggle linear array placement |
| `/eb clones <count\|off>` | Toggle radial mirroring (Clones) |
| `/eb grid <x> <z>\|off` | Toggle grid placement (Clones) |
| `/eb undo` | Revert the last building action |

## 🚀 Installation

1. Download the latest `EffortlessBuildingPlugin.jar`.
2. Place it in your server's `plugins` folder.
3. Restart the server.
4. Enjoy building effortlessly!

## 🛠️ Build

Built using Maven:
```bash
mvn clean package
```

---
Developed by **DuongDev**
