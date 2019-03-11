import org.lwjgl.*;
import org.lwjgl.input.Mouse;
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
     *
     * @param width
     * @param height
     */
    public void start(float width, float height) {
        try {
            createWindow(width, height);
            initGL(width, height);
            gameLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a non-fullscreen window of the specified size.
     *
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
     *
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

    public void gameLoop() {
        FPCameraController camera = new FPCameraController(0, 0, 0);
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f; //length of frame
        float lastTime = 0.0f; // when the last frame was
        long time = 0;
        float mouseSensitivity = 0.09f;
        float movementSpeed = .35f;
        //hide the mouse
        Mouse.setGrabbed(true);

        // keep looping till the display window is closed the ESC key is down
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            time = Sys.getTime();
            lastTime = time;
            //distance in mouse movement
            //from the last getDX() call.
            dx = Mouse.getDX();
            //distance in mouse movement
            //from the last getDY() call.
            dy = Mouse.getDY();
            //control camera yaw from x movement fromt the mouse
            camera.yaw(dx * mouseSensitivity);
            //control camera pitch from y movement fromt the mouse
            camera.pitch(dy * mouseSensitivity);

            //when passing in the distance to move
            //we times the movementSpeed with dt this is a time scale
            //so if its a slow frame u move more then a fast frame
            //so on a slow computer you move just as fast as on a fast computer
            if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
                // move forward
                camera.walkForward(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
                // moves backwards
                camera.walkBackwards(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
                camera.strafeLeft(movementSpeed);
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
                camera.strafeRight(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                camera.moveUp(movementSpeed);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
                camera.moveDown(movementSpeed);
            }
            //set the modelview matrix back to the identity
            glLoadIdentity();
            //look through the camera before you draw anything
            camera.lookThrough();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            //you would draw your scene here.
            render();
            //draw the buffer to the screen
            Display.update();
            Display.sync(60);
        }
        Display.destroy();
    }

    private void render() {
        try{
            glBegin(GL_QUADS);
                drawCube();
            glEnd();

            // Draw Black Lines (Edges of Cube)
            glColor3f(0.0f,0.0f,0.0f);

            //Top
            glBegin(GL_LINE_LOOP);
                glVertex3f( 1.0f, 1.0f,-1.0f);
                glVertex3f(-1.0f, 1.0f,-1.0f);
                glVertex3f(-1.0f, 1.0f, 1.0f);
                glVertex3f( 1.0f, 1.0f, 1.0f);
            glEnd();

            //Bottom
            glBegin(GL_LINE_LOOP);
                glVertex3f( 1.0f,-1.0f, 1.0f);
                glVertex3f(-1.0f,-1.0f, 1.0f);
                glVertex3f(-1.0f,-1.0f,-1.0f);
                glVertex3f( 1.0f,-1.0f,-1.0f);
            glEnd();

            //Front
            glBegin(GL_LINE_LOOP);
                glVertex3f( 1.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f,-1.0f, 1.0f);
                glVertex3f( 1.0f,-1.0f, 1.0f);
            glEnd();

            //Back
            glBegin(GL_LINE_LOOP);
                glVertex3f( 1.0f,-1.0f,-1.0f);
                glVertex3f(-1.0f,-1.0f,-1.0f);
                glVertex3f(-1.0f, 1.0f,-1.0f);
                glVertex3f( 1.0f, 1.0f,-1.0f);
            glEnd();

            //Left
            glBegin(GL_LINE_LOOP);
                glVertex3f(-1.0f, 1.0f, 1.0f);
                glVertex3f(-1.0f, 1.0f,-1.0f);
                glVertex3f(-1.0f,-1.0f,-1.0f);
                glVertex3f(-1.0f,-1.0f, 1.0f);
            glEnd();

            //Right
            glBegin(GL_LINE_LOOP);
                glVertex3f( 1.0f, 1.0f,-1.0f);
                glVertex3f( 1.0f, 1.0f, 1.0f);
                glVertex3f( 1.0f,-1.0f, 1.0f);
                glVertex3f( 1.0f,-1.0f,-1.0f);
            glEnd();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void drawCube() {
        //Top
        glColor3f(0.0f,0.0f,1.0f);
        glVertex3f( 1.0f, 1.0f,-1.0f);
        glVertex3f(-1.0f, 1.0f,-1.0f);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        glVertex3f( 1.0f, 1.0f, 1.0f);

        //Bottom
        glColor3f(0.0f,1.0f,0.0f);
        glVertex3f( 1.0f,-1.0f, 1.0f);
        glVertex3f(-1.0f,-1.0f, 1.0f);
        glVertex3f(-1.0f,-1.0f,-1.0f);
        glVertex3f( 1.0f,-1.0f,-1.0f);

        //Front
        glColor3f(0.0f,1.0f,1.0f);
        glVertex3f( 1.0f, 1.0f, 1.0f);
        glVertex3f(-1.0f, 1.0f, 1.0f);
        glVertex3f(-1.0f,-1.0f, 1.0f);
        glVertex3f( 1.0f,-1.0f, 1.0f);

        //Back
        glColor3f(1.0f,0.0f,0.0f);
        glVertex3f( 1.0f,-1.0f,-1.0f);
        glVertex3f(-1.0f,-1.0f,-1.0f);
        glVertex3f(-1.0f, 1.0f,-1.0f);
        glVertex3f( 1.0f, 1.0f,-1.0f);

        //Left
        glColor3f(1.0f,0.0f,1.0f);
        glVertex3f(-1.0f, 1.0f,1.0f);
        glVertex3f(-1.0f, 1.0f,-1.0f);
        glVertex3f(-1.0f,-1.0f,-1.0f);
        glVertex3f(-1.0f,-1.0f, 1.0f);

        //Right
        glColor3f(1.0f,1.0f,0.0f);
        glVertex3f( 1.0f, 1.0f,-1.0f);
        glVertex3f( 1.0f, 1.0f, 1.0f);
        glVertex3f( 1.0f,-1.0f, 1.0f);
        glVertex3f( 1.0f,-1.0f,-1.0f);
    }
}
