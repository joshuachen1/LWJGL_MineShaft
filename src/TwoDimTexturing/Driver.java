package TwoDimTexturing;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

import java.security.Key;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Joshua Chen
 * Date Created: Mar 05, 2019
 */
public class Driver {
    private boolean mouseEnabled = true;
    private final static int WIDTH = 640;
    private final static int HEIGHT = 480;
    private BlockGrid grid;

    private BlockType selection = BlockType.STONE;
    private int selectorX = 0;
    private int selectorY = 0;

    public Driver () {
        try {
            createWindow();
            initGL();
        } catch (Exception e) {
            e.printStackTrace();
        }

        grid = new BlockGrid();

        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            input();
            grid.draw();
            drawSelectionBox();

            Display.update();
            Display.sync(60);
        }

        Display.destroy();
    }

    private void drawSelectionBox() {
        int x = selectorX * World.BLOCK_SIZE;
        int x2 = x + World.BLOCK_SIZE;
        int y = selectorY * World.BLOCK_SIZE;
        int y2 = y + World.BLOCK_SIZE;

        if (grid.getAt(selectorX, selectorY).getType() != BlockType.DIRT || selection == BlockType.DIRT) {
            // Get rid of any bound textures
            glBindTexture(GL_TEXTURE_2D, 0);

            // White 50% Transparency
            glColor4f(1.0f, 1.0f, 1.0f, 0.5f);

            glBegin(GL_QUADS);
            glVertex2i(x, y); // Upper Left
            glVertex2i(x2, y); // Upper Right
            glVertex2i(x2, y2); // Bottom Right
            glVertex2i(x, y2); // Bottom Left
            glEnd();

            // Reset Transparency: White, 100% Transparency
            glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        else {
            // White 50% Transparency
            glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
            new Block(selection, selectorX * World.BLOCK_SIZE, selectorY * World.BLOCK_SIZE).draw();

            // Reset Transparency: White, 100% Transparency
            glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    private void createWindow() throws Exception {
        Display.setFullscreen(false);

        Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
        Display.setTitle("Minecraft 2D");
        Display.create();
    }

    private void initGL() {
        //background color (R, G, B, alpha)
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        //load camera to view screen
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        //setup orthographic matrix
        glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);

        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

        // Textures for quads
        glEnable(GL_TEXTURE_2D);

        // Transparency
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    private void input() {
        if (mouseEnabled || Mouse.isButtonDown(0)) {
            mouseEnabled = true;

            int mousex = Mouse.getX();
            int mousey = 480 - Mouse.getY() - 1;
            boolean mouseClicked = Mouse.isButtonDown(0);

            selectorX = Math.round(mousex / World.BLOCK_SIZE);
            selectorY = Math.round(mousey / World.BLOCK_SIZE);

            if (mouseClicked) {
                grid.setAt(selectorX, selectorY, selection);
            }
        }

        while (Keyboard.next()) {
            // Within bounds of right border
            if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT
                    && Keyboard.getEventKeyState()) {
                mouseEnabled = false;
                if (!(selectorX + 1 > World.BLOCKS_WIDTH - 2)) {
                    selectorX += 1;
                }
            }
            // Within bounds of left border
            if (Keyboard.getEventKey() == Keyboard.KEY_LEFT
                    && Keyboard.getEventKeyState()) {
                mouseEnabled = false;
                if (!(selectorX - 1 < 0)) {
                    selectorX -= 1;
                }
            }

            // Within bounds of right border
            if (Keyboard.getEventKey() == Keyboard.KEY_UP
                    && Keyboard.getEventKeyState()) {
                mouseEnabled = false;
                if (!(selectorY - 1 < 0)) {
                    selectorY -= 1;
                }
            }
            // Within bounds of left border
            if (Keyboard.getEventKey() == Keyboard.KEY_DOWN
                    && Keyboard.getEventKeyState()) {
                mouseEnabled = false;
                if (!(selectorY + 1 > World.BLOCKS_HEIGHT - 2)) {
                    selectorY += 1;
                }
            }

            if (Keyboard.getEventKey() == Keyboard.KEY_1) {
                selection = BlockType.STONE;
            }
            if (Keyboard.getEventKey() == Keyboard.KEY_2) {
                selection = BlockType.DIRT;
            }
            if (Keyboard.getEventKey() == Keyboard.KEY_3) {
                selection = BlockType.GRASS;
            }
            if (Keyboard.getEventKey() == Keyboard.KEY_4) {
                selection = BlockType.DIAMOND;
            }

            if (Keyboard.getEventKey() == Keyboard.KEY_C) {
                grid.clear();
            }
            if (Keyboard.getEventKey() == Keyboard.KEY_4) {
                selection = BlockType.DIAMOND;
            }
        }
    }

    public static void main(String[] args) {
        new Driver();
    }
}
