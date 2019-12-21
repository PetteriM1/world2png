package world2png;

import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.AsyncTask;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main extends PluginBase {

    private int X;
    private int Y;
    private String world;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        X = getConfig().getInt("x_size");
        Y = getConfig().getInt("y_size");
        world = getConfig().getString("world");
        getServer().getScheduler().scheduleAsyncTask(this, new AsyncTask() {
            @Override
            public void onRun() {
                getLogger().info("\u00A7aGenerating picture of world \"" + world + "\"...");
                BufferedImage image = new BufferedImage(X, Y, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = image.createGraphics();
                Level level = getServer().getLevelByName(world);
                if (level == null) {
                    getLogger().error("\u00A7cWorld cannot be null!");
                    return;
                }
                Position spawn = level.getSpawnLocation();
                for (int x = 0; x != X; x++) {
                    for (int y = 0; y != Y; y++) {
                        graphics.setColor(new Color(level.getMapColorAt(spawn.getFloorX() - (X >> 1) + x, spawn.getFloorZ() - (Y >> 1) + y).getRGB()));
                        graphics.fillRect(x, y, x, y);
                    }
                    getLogger().info("\u00A7a" + (x * 100 / X) + "% done");
                }
                try {
                    File file = new File(getDataFolder() + File.separator + world + ".png");
                    ImageIO.write(image, "png", file);
                    getLogger().info("\u00A7a" + getDataFolder() + File.separator + world + ".png" + " saved!");
                } catch (IOException ignored) {}
            }
        });
    }
}