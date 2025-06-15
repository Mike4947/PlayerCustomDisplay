# PlayerCustomDisplay

A simple, lightweight Spigot/Paper plugin that allows server owners to display a dynamic, custom player count in the Minecraft server list.

<img src="https://img.shields.io/github/downloads/Mike4947/playercustomdisplay/total?style=for-the-badge&color=2196F3" alt="Total Downloads"/>

## 📖 Description

PlayerCustomDisplay goes beyond just setting a static fake player count. It allows you to set a "base" number, and the plugin will dynamically add the current number of online players to it. This creates a realistic and lively player count that changes as players join and leave your server.
![image](https://github.com/user-attachments/assets/7e494577-4c02-4516-9d18-493b1570faa3)


## ✨ Features

  - **Dynamic Player Count:** The count shown in the server list is the sum of your base number and the actual online player count.
  - **Easy Configuration:** Set the base player count instantly with a simple in-game command.
  - **Live Stats:** Use a command to see exactly how the current player count is being calculated.
  - **Lightweight:** The plugin is designed to be simple and have a minimal impact on server performance.
  - **Permission-Based:** Control who can change the settings with a single permission node.

## ⚙️ Installation

1.  Download the latest release from the [Releases page](https://github.com/Mike4947/PlayerCustomDisplay/releases).
2.  Place the `PlayerCustomDisplay v.X.X.X.jar` file into your server's `plugins` folder.
3.  Restart your server.

## ⌨️ Commands & Permissions

The plugin uses a single permission node to control access to all commands.

| Permission                  | Description                             | Default |
| --------------------------- | --------------------------------------- | ------- |
| `playercustomdisplay.admin` | Grants access to all `/pcd` commands. | OP      |

| Command                | Description                                                                                             | Usage                 |
| ---------------------- | ------------------------------------------------------------------------------------------------------- | --------------------- |
| `/pcd set <number>`    | Sets the base number for the player count. Use `-1` to disable the feature and show the real player count. | `/pcd set 100`        |
| `/pcd stat`            | Shows the current status and calculation of the displayed player count.                                 | `/pcd stat`           |

## 🔧 Configuration

The `config.yml` file is simple and allows you to set the base player count manually. The value set by the `/pcd set` command will be saved here automatically.

**File: `config.yml`**

```yaml
# PlayerCustomDisplay Configuration

# Set the base number to be added to the real player count for the server list.
# For example, if this is set to 200 and 5 players are online, the list will show 205.
# Set to -1 to disable this feature and show the real player count.
custom-player-count: -1
```

## 🏗️ Building from Source

If you'd like to build the plugin yourself, you'll need:

  - Java 21 JDK
  - Git
  - Apache Maven

<!-- end list -->

```bash
# Clone the repository
git clone https://github.com/Mike4947/PlayerCustomDisplay.git
cd PlayerCustomDisplay

# Build the project with Maven
mvn clean package
```

The compiled JAR file will be located in the `target` directory.

## 📄 License

This project is open-source and licensed under the [MIT License](https://opensource.org/licenses/MIT).
