import org.lwjgl.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;
import org.lwjgl.*;
import org.lwjgl.util.vector.Vector3f;

import java.util.Random;

/**
 * @author Joshua Chen
 * Date Created: Mar 10, 2019
 */
public class MineShaftDriver {
    public static void main(String[] args) {
        final float WIDTH = 640;
        final float HEIGHT = 480;

        new MineShaftDriver().start(WIDTH, HEIGHT);
    }

    /**
     * Take in the specifications of the window size and list of polygons to render.
     * Also ensures that if an Exception arises, the stack is printed.
     * @param width
     * @param height
     */
    public void start(float width, float height) {
        try {
            createWindow(width, height);
            initGL(width, height);
            render();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a non-fullscreen window of the specified size.
     * @param width
     * @param height
     * @throws Exception
     */
    private void createWindow(float width, float height) throws Exception {
        Display.setFullscreen(false);

        Display.setDisplayMode(new DisplayMode((int) width, (int) height));
        Display.setTitle("MineShaft");
        Display.create();
    }

    /**
     * Set background color to black, RGB = (0, 0, 0).
     * Set orthographic matrix with the specified size.
     * @param width
     * @param height
     */
    private void initGL(float width, float height) {
        //background color (R, G, B, alpha)
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        //load camera to view screen
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        // Field of View: 30° angle
        // Aspect Ratio (width/height): 640 / 480
        // zNear: how near an object can be to be rendered
        // zFar: how far an object can be to be rendered
        // +x is to the right
        // +y is to the top
        // +z is to the camera
        gluPerspective((float) 30, width / height, 0.001f, 100);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }

    private void render() {
        // Camera Travel Speed
        float cameraSpeed  = 0.0f;

        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            // Render

            // Clear the screen of contents
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Push the screen forward
            glTranslatef(0, 0, cameraSpeed);

            // Front Face
            glColor3f(1, 0 ,0);
            glBegin(GL_QUADS);
                glVertex3f(-5, 5, -100);
                glVertex3f(5, 5, -100);
                glVertex3f(5, -5, -100);
                glVertex3f(-5, -5, -100);
            glEnd();

            // Back Face
            glColor3f(0, 1 , 0);
            glBegin(GL_QUADS);
                glVertex3f(-5, 5, -105);
                glVertex3f(5, 5, -105);
                glVertex3f(5, -5, -105);
                glVertex3f(-5, -5, -105);
            glEnd();

            // Top Face
            glColor3f(0, 0, 1);
            glBegin(GL_QUADS);
                glVertex3f(-5, 5, -105);
                glVertex3f(5, 5, -105);
                glVertex3f(5, 5, -100);
                glVertex3f(-5, 5, -100);
            glEnd();

            // Bottom Face
            glColor3f(1, 1, 0);
            glBegin(GL_QUADS);
                glVertex3f(-5, -5, -105);
                glVertex3f(5, -5, -105);
                glVertex3f(5, -5, -100);
                glVertex3f(-5, -5, -100);
            glEnd();

            // Left Face
            glColor3f(1, 0, 1);
            glBegin(GL_QUADS);
                glVertex3f(-5, 5, -100);
                glVertex3f(-5, 5, -105);
                glVertex3f(-5, -5, -105);
                glVertex3f(-5, -5, -100);
            glEnd();

            // Right Face
            glColor3f(1, 1, 1);
            glBegin(GL_QUADS);
                glVertex3f(5, 5, -100);
                glVertex3f(5, 5, -105);
                glVertex3f(5, -5, -105);
                glVertex3f(5, -5, -100);
            glEnd();



            // Increase Speed
            if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                cameraSpeed += 0.01f;
            }
            // Decrease Speed
            if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                cameraSpeed -= 0.01f;
            }

            while (Keyboard.next()) {
                // Reset speed to 0
                if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                    cameraSpeed = 0.0f;
                }
                // Reset speed to 0 and reset position
                if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
                    cameraSpeed = 0.0f;
                    glLoadIdentity();   // Wipes everything acted on the matrices (the translations)
                }
            }

            Display.update();
            Display.sync(60);
        }
        Display.destroy();
    }

}
