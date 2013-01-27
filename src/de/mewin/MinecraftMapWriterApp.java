/*
 * MinecraftMapWriterApp.java
 */

package de.mewin;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class MinecraftMapWriterApp extends SingleFrameApplication {

    private byte[] colors = new byte[128 * 128];
    private MinecraftMapWriterView view;
    private int historyPosition = -1;

    private ArrayList<byte[]> history;

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        //show(new MinecraftMapWriterView(this));
        view = new MinecraftMapWriterView(this);
        view.getFrame().setTitle("Minecraft Map Writer");
        history = new ArrayList<byte[]>();
        setHistoryPoint();
        show(view);
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
        
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of MinecraftMapWriterApp
     */
    public static MinecraftMapWriterApp getApplication() {
        return Application.getInstance(MinecraftMapWriterApp.class);
    }

    public void showPicture(Image img)
    {
        int wh = Math.min(view.canvasPic.getWidth(), view.canvasPic.getHeight());
        Image tmpImg = img.getScaledInstance(wh, wh, Image.SCALE_DEFAULT);
        //view.canvasPic.getGraphics().drawImage(tmpImg, 0, 0, null);

        view.setImage(tmpImg);
    }

    public void setColor(int x, int y, byte color)
    {
        colors[x + y * 128] = color;
        showPicture();
    }

    public byte getColor(int x, int y)
    {
        return colors[x + y * 128];
    }

    public void showPicture()
    {
        BufferedImage img = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);

        for(int x = 0; x < 128; x++)
        {
            for(int y = 0; y < 128; y++)
            {
                Color col = MapColor.colorFromData(colors[x + 128 * y]);

                img.setRGB(x, y, col.getRGB());
            }
        }

        showPicture(img);
    }

    public void loadImage(File file)
    {
        try {
            loadImage(ImageIO.read(new FileInputStream(file)));

        } catch (IOException ex) {
            Logger.getLogger(MinecraftMapWriterApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadImage(BufferedImage img)
    {
        //img = img.getScaledInstance(128, 128, Image.SCALE_DEFAULT);

        double multiX, multiY;
        
        multiX = img.getWidth() / 128.;
        multiY = img.getHeight() / 128.;

        for(int x = 0; x < 128; x++)
        {
            for(int y = 0; y < 128; y++)
            {
                colors[x + 128 * y] = MapColor.getBestColorData(new Color(img.getRGB((int) Math.floor(x * multiX), (int) Math.floor(multiY * y))));
            }
        }

        eraseHistory();
        
        setHistoryPoint();

        showPicture();
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(MinecraftMapWriterApp.class, args);
    }

    public void loadFile(File f)
    {
        try {
            FileInputStream in = new FileInputStream(f);

            for(int i = 0; i < 128 * 128; i++)
            {
                int r = in.read();

                if(r == -1)
                {
                    throw new Exception("MapData missing");
                }
                else
                {
                    colors[i] = (byte) r;
                }
            }
            in.close();
        } catch (Exception ex) {
            Logger.getLogger(MinecraftMapWriterApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeFile(File f)
    {
        try {
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f);

            for(int i = 0; i < 128 * 128; i++)
            {
                out.write(colors[i]);
            }

            out.close();
        } catch (Exception ex) {
            Logger.getLogger(MinecraftMapWriterApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void newPicture() {
        for(int i = 0; i < 128 * 128; i++)
        {
            colors[i] = 0x00;
        }

        eraseHistory();

        setHistoryPoint();

        showPicture();
    }

    public void setHistoryPoint()
    {

        for(int i = ++historyPosition; i < history.size(); i++)
        {
            history.remove(i);
        }
        
        history.add(colors.clone());
    }

    public void goHistory(int steps)
    {
        historyPosition += steps;
        if(historyPosition < 0)
        {
            historyPosition = 0;
        }

        if(historyPosition > history.size() - 1)
        {
            historyPosition = history.size() - 1;
        }

        colors = history.get(historyPosition);

        showPicture();
    }

    public void eraseHistory()
    {
        history.clear();
        historyPosition = -1;
    }
}
