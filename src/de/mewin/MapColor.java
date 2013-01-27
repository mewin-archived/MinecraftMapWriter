/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.mewin;

import java.awt.Color;

/**
 *
 * @author Admin
 */
public class MapColor
{
    private static final Color AIR_COLOR = new Color(210, 210, 255);
    /**
     * Holds all the 16 colors used on maps, very similar of a pallete system.
     */
    public static final MapColor[] mapColorArray = new MapColor[16];

    /** The map color for Air blocks */
    public static final MapColor airColor = new MapColor(0, 0);

    /** this is the grass color in html format */
    public static final MapColor grassColor = new MapColor(1, 8368696);

    /** This is the color of the sand */
    public static final MapColor sandColor = new MapColor(2, 16247203);

    /** The map color for Cloth and Sponge blocks */
    public static final MapColor clothColor = new MapColor(3, 10987431);

    /** The map color for TNT blocks */
    public static final MapColor tntColor = new MapColor(4, 16711680);

    /** The map color for Ice blocks */
    public static final MapColor iceColor = new MapColor(5, 10526975);

    /** The map color for Iron blocks */
    public static final MapColor ironColor = new MapColor(6, 10987431);

    /** The map color for Leaf, Plant, Cactus, and Pumpkin blocks. */
    public static final MapColor foliageColor = new MapColor(7, 31744);

    /** The map color for Snow Cover and Snow blocks */
    public static final MapColor snowColor = new MapColor(8, 16777215);

    /** The map color for Clay blocks */
    public static final MapColor clayColor = new MapColor(9, 10791096);

    /** The map color for Dirt blocks */
    public static final MapColor dirtColor = new MapColor(10, 12020271);

    /** The map color for Stone blocks */
    public static final MapColor stoneColor = new MapColor(11, 7368816);

    /** The map color for Water blocks */
    public static final MapColor waterColor = new MapColor(12, 4210943);

    /** The map color for Wood blocks */
    public static final MapColor woodColor = new MapColor(13, 6837042);

    /** Holds the color in RGB value that will be rendered on maps. */
    public final int colorValue;

    /** Holds the index of the color used on map. */
    public final int colorIndex;

    private MapColor(int par1, int par2)
    {
        this.colorIndex = par1;
        this.colorValue = par2;
        mapColorArray[par1] = this;
    }

    public static MapColor mapColorFromData(byte data)
    {
        int index = data / 4;

        return mapColorArray[index];
    }

    public static Color colorFromIndexAndBrightness(int i, int data)
    {
        if(i == 0) return AIR_COLOR;

        int colorValue = mapColorArray[i].colorValue;
        short brightness = 220;

        if (data == 2)
        {
            brightness = 255;
        }

        if (data == 0)
        {
            brightness = 180;
        }

        int r = (colorValue >> 16 & 255) * brightness / 255;
        int g = (colorValue >> 8 & 255) * brightness / 255;
        int b = (colorValue & 255) * brightness / 255;

        return new Color(r, g, b);
    }

    public static byte dataFromIndexAndBrightness(int i, int b)
    {
        return (byte) ((i * 4) | b);
    }

    public static Color colorFromData(byte data)
    {
        if(data == 0)
        {
            return AIR_COLOR;
        }
        int colorValue = mapColorFromData(data).colorValue;
        int bitData = data & 3;
        short brightness = 220;

        if (bitData == 2)
        {
            brightness = 255;
        }

        if (bitData == 0)
        {
            brightness = 180;
        }

        int r = (colorValue >> 16 & 255) * brightness / 255;
        int g = (colorValue >> 8 & 255) * brightness / 255;
        int b = (colorValue & 255) * brightness / 255;

        return new Color(r, g, b);

    }

    public static String colorNameFromData(byte data)
    {
        return colorNameFromIndexAndBrightness(data / 4, data & 3);
    }

    public static String colorNameFromIndexAndBrightness(int i, int b)
    {
        String name = "";
        switch(i)
        {
            case 0:
                return "Air (None)";
            case 1:
                name = "Grass";
                break;
            case 2:
                name = "Sand";
                break;
            case 3:
                name = "Cloth";
                break;
            case 4:
                name = "TNT";
                break;
            case 5:
                name = "Ice";
                break;
            case 6:
                name = "Iron";
                break;
            case 7:
                name = "Foliag";
                break;
            case 8:
                name = "Snow";
                break;
            case 9:
                name = "Clay";
                break;
            case 10:
                name = "Dirt";
                break;
            case 11:
                name = "Stone";
                break;
            case 12:
                name = "Water";
                break;
            case 13:
                name = "Wood";
                break;
            default:
                name = "Unknown";
        }
        if(b == 0)
        {
            name += " I";
        }
        else if(b == 1)
        {
            name += " II";
        }
        else if(b == 2)
        {
            name += " III";
        }

        return name;
    }

    public static byte getBestColorData(Color c)
    {
        int bestIndex, bestDifference, bestBright;
        bestIndex = 0;
        bestBright = 0;
        bestDifference = c.getAlpha();
        
        for(int i = 1; i < 13; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                int difference = colorDifference(c, colorFromIndexAndBrightness(i, j));
                if(bestDifference > difference)
                {
                    bestIndex = i;
                    bestBright = j;
                    bestDifference = difference;
                }
            }
        }

        return (byte) (bestIndex * 4 | bestBright);
    }

    public static int colorDifference(Color c1, Color c2)
    {
        int diffR, diffG, diffB;

        diffR = Math.abs(c1.getRed() - c2.getRed());
        diffG = Math.abs(c1.getGreen() - c2.getGreen());
        diffB = Math.abs(c1.getBlue() - c2.getBlue());

        return (diffR + diffG + diffB) / 3;
    }

    public static void main(String[] args)
    {
        System.err.println(getBestColorData(Color.RED));
    }
}