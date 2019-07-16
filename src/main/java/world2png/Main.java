package world2png;

import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.AsyncTask;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main extends PluginBase {

    int X;
    int Y;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        X = getConfig().getInt("x_size");
        Y = getConfig().getInt("y_size");
        getServer().getScheduler().scheduleAsyncTask(new AsyncTask() {
            @Override
            public void onRun() {
                getLogger().info("\u00A7aGenerating world.png...");
                BufferedImage image;
                image = new BufferedImage(X, Y, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = image.createGraphics();
                Position spawn = getServer().getDefaultLevel().getSpawnLocation();
                for (int x = 0; x != X; x++) {
                    for (int y = 0; y != Y; y++) {
                        graphics.setColor(new Color(getServer().getDefaultLevel().getMapColorAt(spawn.getFloorX() - (X / 2) + x, spawn.getFloorZ() - (Y / 2) + y).getRGB()));
                        graphics.fillRect(x, y, x, y);
                    }
                    getLogger().info("\u00A7a" + (x * 100 / X) + "% done");
                }
                try {
                    File file = new File(getDataFolder() + "/world.png");
                    ImageIO.write(image, "png", file);
                    getLogger().info("\u00A7aworld.png saved!");
                } catch (IOException e) {}
            }
        });
    }
}