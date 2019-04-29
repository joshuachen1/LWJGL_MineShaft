import javafx.scene.Camera;
import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

/**
 * @author Joshua Chen, Bryan Lee, Saul Galaviz, Camille Nibungco
 * Date Created: Mar 10, 2019
 */
public class MineShaftDriver {
    private FPCameraController fp;
    private DisplayMode displayMode;

    private FloatBuffer lightPosition;
    private FloatBuffer whiteLight;
    private FloatBuffer darkLight;

    // method: start
    // purpose: method to create the window, initalize the GL and render the
    //graphics in this order
    public void start() {
        try {
            createWindow();
            initGL();

        } catch (Exception e) {
            e.printStackTrace();
        }
        fp = new FPCameraController(-2f, -2f, -2f);
        try {
            fp.gameLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // method: createWindow
    // purpose:  method to set the display window not fullscreen, set dimensions
    // and set the title of the window as well as creating it
    private void createWindow() throws Exception {
        Display.setFullscreen(false);
        DisplayMode d[] = Display.getAvailableDisplayModes();
        for (int i = 0; i < d.length; i++) {
            if (d[i].getWidth() == 640 && d[i].getHeight() == 480 && d[i].getBitsPerPixel() == 32) {
                displayMode = d[i];
                break;
            }
        }
        Display.setDisplayMode(displayMode);
        Display.setTitle("Mineshaft");
        Display.create();
    }

    // method: initGL
    // purpose:  method to initialize the GL options
    private void initGL() {
        glEnable(GL_TEXTURE_2D);
        glClearColor(0.8f, 0.95f, 1.0f, 0.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(100.0f, (float) displayMode.getWidth() / (float) displayMode.getHeight(), 0.1f, 300.0f);
        glEnable(GL_DEPTH_TEST);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        glDepthFunc(GL_LESS);

        initLightArrays();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
        glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);
        glLight(GL_LIGHT0, GL_DIFFUSE, whiteLight);
        glLight(GL_LIGHT0, GL_AMBIENT, darkLight);

        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
    }

    private void initLightArrays() {
        lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(0.0f).put(0.0f).put(0.0f).put(1.0f).flip();

        whiteLight = BufferUtils.createFloatBuffer(4);
        whiteLight.put(7.0f).put(7.0f).put(7.0f).put(0.0f).flip();

        darkLight = BufferUtils.createFloatBuffer(4);
        darkLight.put(0.3f).put(0.3f).put(0.3f).put(0.0f).flip();
    }

    // method: main
    // purpose: takes in user input from text file and renders in openGL window
    public static void main(String[] args) {
        MineShaftDriver driver = new MineShaftDriver();
        driver.start();
    }

}
