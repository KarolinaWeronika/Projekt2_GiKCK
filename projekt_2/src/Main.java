import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.util.Scanner;
import javax.swing.JPanel;
import javax.swing.JFrame;

class Okno {
    public void wywolanie() {
        JFrame frame = new JFrame();
        frame.add(new Wykres());
        frame.setDefaultCloseOperation(3);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }
}


 class Maska {
    void maskowanie() throws Exception {
        int[][] maska = new int[3][3];

        try {
            File input = new File("src/ratusz.jpg");
            BufferedImage image = ImageIO.read(input);
            int width = image.getWidth();
            int height = image.getHeight();
            maska[0][0] = 0;
            maska[0][1] = 0;
            maska[0][2] = 0;
            maska[1][0] = 0;
            maska[1][1] = 1;
            maska[1][2] = -1;
            maska[2][0] = 0;
            maska[2][1] = 0;
            maska[2][2] = 0;

            for(int i = 1; i < height - 1; ++i) {
                for(int j = 1; j < width - 1; ++j) {
                    int pomoc_r = 0;
                    int pomoc_g = 0;
                    int pomoc_b = 0;

                    for(int k = -1; k <= 1; ++k) {
                        for(int l = -1; l <= 1; ++l) {
                            Color c = new Color(image.getRGB(j + k, i + l));
                            int red = c.getRed();
                            int green = c.getGreen();
                            int blue = c.getBlue();
                            pomoc_r += red * maska[k + 1][l + 1];
                            pomoc_g += green * maska[k + 1][l + 1];
                            pomoc_b += blue * maska[k + 1][l + 1];
                        }
                    }

                    int red_wy;
                    if (pomoc_r >= 0 && pomoc_r <= 255) {
                        red_wy = pomoc_r;
                    } else {
                        red_wy = 0;
                    }

                    int green_wy;
                    if (pomoc_g >= 0 && pomoc_g <= 255) {
                        green_wy = pomoc_g;
                    } else {
                        green_wy = 0;
                    }

                    int blue_wy;
                    if (pomoc_b >= 0 && pomoc_b <= 255) {
                        blue_wy = pomoc_b;
                    } else {
                        blue_wy = 0;
                    }

                    Color newColor = new Color(red_wy, green_wy, blue_wy);
                    image.setRGB(j, i, newColor.getRGB());
                }
            }

            File ouptut = new File("src/nowyratusz.jpg");
            ImageIO.write(image, "jpg", ouptut);
        } catch (Exception var20) {
            System.out.println("blad " + var20);
        }

    }
}




class Wykres extends JPanel {
    Dimension d;
    Image img;
    int iw;
    int ih;
    int[] pixels;
    int w;
    int h;
    int[] hist = new int[256];
    int max_hist = 0;

    public void paint(Graphics gg) {
        this.d = this.getSize();
        this.w = this.d.width;
        this.h = this.d.height;
        gg.setColor(Color.white);
        gg.fillRect(0, 0, this.getWidth(), this.getHeight());

        try {
            this.img = Toolkit.getDefaultToolkit().getImage("src/ratusz.jpg");
            MediaTracker t = new MediaTracker(this);
            t.addImage(this.img, 0);
            t.waitForID(0);
            this.iw = this.img.getWidth((ImageObserver)null);
            this.ih = this.img.getHeight((ImageObserver)null);
            this.pixels = new int[this.iw * this.ih];
            PixelGrabber pg = new PixelGrabber(this.img, 0, 0, this.iw, this.ih, this.pixels, 0, this.iw);
            pg.grabPixels();
        } catch (InterruptedException var8) {
            System.out.println("Interrupted");
            return;
        }

        int i;
        int y;
        int x;
        int lasty;
        for(x = 0; x < this.iw * this.ih; ++x) {
            lasty = this.pixels[x];
            i = 255 & lasty >> 16;
            y = 255 & lasty >> 8;
            int b = 255 & lasty;
            int z = (int)(0.33 * (double)i + 0.56 * (double)y + 0.11 * (double)b);
            int var10002 = this.hist[z]++;
        }

        for(x = 0; x < 256; ++x) {
            if (this.hist[x] > this.max_hist) {
                this.max_hist = this.hist[x];
            }
        }

        x = (this.w - 256) / 2;
        lasty = this.h - this.h * this.hist[0] / this.max_hist;

        for(i = 0; i < 256; ++x) {
            y = this.h - this.h * this.hist[i] / this.max_hist;
            gg.setColor(new Color(i, i, i));
            gg.fillRect(x, y, 1, this.h);
            gg.setColor(Color.red);
            gg.drawLine(x - 1, lasty, x, y);
            lasty = y;
            ++i;
        }

    }
}




public class Main {
    BufferedImage image;
    int width;
    int height;

    public Main(int x, int y, int z, String filePath, String nazwa) {
        try {
            File input = new File(filePath + ".jpg");
            this.image = ImageIO.read(input);
            this.width = this.image.getWidth();
            this.height = this.image.getHeight();

            for(int i = 0; i < this.height; ++i) {
                for(int j = 0; j < this.width; ++j) {
                    Color c = new Color(this.image.getRGB(j, i));
                    int r = c.getRed();
                    int g = c.getGreen();
                    int b = c.getBlue();
                    if (r + x >= 0 && r + x <= 255) {
                        r -= x;
                    }

                    if (g + y >= 0 && g + y <= 255) {
                        g -= y;
                    }

                    if (b + z >= 0 && b + z <= 255) {
                        b -= z;
                    }

                    Color newColor = new Color(r, g, b);
                    this.image.setRGB(j, i, newColor.getRGB());
                }
            }

            File output = new File(filePath + nazwa + ".jpg");
            ImageIO.write(this.image, "jpg", output);
        } catch (Exception var14) {
            System.out.println(var14.getMessage());
        }

    }

    public Main(String filePath, String nazwa) {
        try {
            File input = new File(filePath + ".jpg");
            this.image = ImageIO.read(input);
            this.width = this.image.getWidth();
            this.height = this.image.getHeight();

            for(int i = 0; i < this.height; ++i) {
                for(int j = 0; j < this.width; ++j) {
                    Color c = new Color(this.image.getRGB(j, i));
                    int r = c.getRed();
                    int g = c.getGreen();
                    int b = c.getBlue();
                    r = 255 - r;
                    g = 255 - g;
                    b = 255 - b;
                    Color newColor = new Color(r, g, b);
                    this.image.setRGB(j, i, newColor.getRGB());
                }
            }

            File output = new File(filePath + nazwa + ".jpg");
            ImageIO.write(this.image, "jpg", output);
        } catch (Exception var11) {
            System.out.println(var11.getMessage());
        }

    }

    public Main(int dc, String filePath, String nazwa) {
        try {
            File input = new File(filePath + ".jpg");
            this.image = ImageIO.read(input);
            this.width = this.image.getWidth();
            this.height = this.image.getHeight();

            for(int i = 0; i < this.height; ++i) {
                for(int j = 0; j < this.width; ++j) {
                    Color c = new Color(this.image.getRGB(j, i));
                    int r = c.getRed();
                    int g = c.getGreen();
                    int b = c.getBlue();
                    int x = 127 / (127 - dc) * (r - dc);
                    int y = 127 / (127 - dc) * (g - dc);
                    int z = 127 / (127 - dc) * (b - dc);
                    if (r + x >= 0 && r + x <= 255) {
                        r += x;
                    }

                    if (g + y >= 0 && g + y <= 255) {
                        g += y;
                    }

                    if (b + z >= 0 && b + z <= 255) {
                        b += z;
                    }

                    Color newColor = new Color(r, g, b);
                    this.image.setRGB(j, i, newColor.getRGB());
                }
            }

            File output = new File(filePath + nazwa + ".jpg");
            ImageIO.write(this.image, "jpg", output);
        } catch (Exception var15) {
            System.out.println(var15.getMessage());
        }

    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("sciezka do pliku: ");
        String path = sc.nextLine();
        System.out.println("wybierz:");
        System.out.println("1 <- rozjasnienie");
        System.out.println("2 <- przyciemnienie");
        System.out.println("3 <- negatyw");
        System.out.println("4 <- zmiana kontrastu");
        System.out.println("5 <- histogram");
        System.out.println("6 <- maska");
        System.out.println();
        int tryb = sc.nextInt();
        System.out.println();
        int dc;
        if (tryb != 1 && tryb != 2) {
            if (tryb == 3) {
                new Main(path, "_3");
            } else if (tryb == 4) {
                System.out.println("wartosc kontrastu C:  -128  -  127");
                dc = sc.nextInt();
                new Main(dc, path, "_4");
            } else if (tryb == 5) {
                Okno histogram = new Okno();
                histogram.wywolanie();
            } else if (tryb == 6) {
                Maska mask = new Maska();
                mask.maskowanie();
            } else {
                System.out.println("blad!");
            }
        } else {
            System.out.println("wartosc transformacji: 0 - 255");
            dc = sc.nextInt();
            System.out.println();
            if (tryb == 1) {
                new Main(dc, dc, dc, path, "_1");
            } else {
                new Main(dc * -1, dc * -1, dc * -1, path, "_2");
            }
        }

    }
}