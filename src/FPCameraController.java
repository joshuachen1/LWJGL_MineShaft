import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;
import java.util.logging.Logger;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author Joshua Chen, Bryan Lee, Saul Galaviz, Camille Nibungco
 * Date Created: Mar 11, 2019
 */
public class FPCameraController {
    private Vector3f position = null;    //3d vector to store the camera's position in
    private Vector3f lPosition = null;
    private Vector3f newPosition = null;
    private float yaw = 0.0f;   //the rotation around the Y axis of the camera
    private float pitch = 0.0f; //the rotation around the X axis of the camera
    private Vector3f me;
    private Chunk chunk;
    private final double[][] heightMap;
    private final int UPPER_X_BOUND = 2;
    private final int LOWER_X_BOUND = -5;
    private final int UPPER_Y_BOUND = -3;
    private final int LOWER_Y_BOUND = 2;
    private final int UPPER_Z_BOUND = 2;
    private final int LOWER_Z_BOUND = -5;
    private int xGrid;
    private int zGrid;

    public FPCameraController(float x, float y, float z) {
        //instantiate position Vector3f to the x y z params.
        position = new Vector3f(x, y, z);
        lPosition = new Vector3f(x, y, z);
        lPosition.x = 0f;
        lPosition.y = 15f;
        lPosition.z = 0f;
        chunk = new Chunk((int)x+5,(int)y-40,(int)z +5 );
        heightMap = chunk.getHeightMap();

    }

    //increment the camera's current yaw rotation
    public void yaw(float amount) {
        //increment the yaw by the amount param
        yaw += amount;
    }

    //increment the camera's current yaw rotation
    public void pitch(float amount) {
        //increment the pitch by the amount param
        pitch -= amount;
    }

    //moves the camera forward relative to its current rotation (yaw)
    public void walkForward(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw));
        position.x -= xOffset*3;
        position.z += zOffset*3;

        if (isInboundsXZ(newPosition.x, newPosition.z) && noCollisionXZ(newPosition.x, newPosition.z)) {
            position.x -= xOffset;
            position.z += zOffset;
        }

        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x -= xOffset).put(lPosition.y).put(lPosition.z += zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }

    //moves the camera backward relative to its current rotation (yaw)
    public void walkBackwards(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw));
        position.x += xOffset*3;
        position.z -= zOffset*3;

        if (isInboundsXZ(newPosition.x, newPosition.z) && noCollisionXZ(newPosition.x, newPosition.z)) {
            position.x += xOffset;
            position.z -= zOffset;
        }

        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x += xOffset).put(lPosition.y).put(lPosition.z -= zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }

    //strafes the camera left relative to its current rotation (yaw)
    public void strafeLeft(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw - 90));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw - 90));
        position.x -= xOffset*3;
        position.z += zOffset*3;

        if (isInboundsXZ(newPosition.x, newPosition.z) && noCollisionXZ(newPosition.x, newPosition.z)) {
            position.x -= xOffset;
            position.z += zOffset;
        }

        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x -= xOffset).put(lPosition.y).put(lPosition.z += zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }

    //strafes the camera right relative to its current rotation (yaw)
    public void strafeRight(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw + 90));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw + 90));
        position.x -= xOffset*3;
        position.z += zOffset*3;

        if (isInboundsXZ(newPosition.x, newPosition.z) && noCollisionXZ(newPosition.x, newPosition.z)) {
            position.x -= xOffset;
            position.z += zOffset;
        }

        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x -= xOffset).put(lPosition.y).put(lPosition.z += zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }

    //moves the camera up relative to its current rotation (yaw)
    public void moveUp(float distance) {
        float newY = position.y - distance;
        if (isInboundsY(newY) && noCollisionYUp(newY)) {
            position.y -= distance;
        }
    }

    //moves the camera down
    public void moveDown(float distance) {
        float newY = position.y + distance;
        if (isInboundsY(newY) && noCollisionYDown(newY)) {
            position.y += distance;
        }
    }

    //translates and rotate the matrix so that it looks through the camera
    //this does basically what gluLookAt() does
    public void lookThrough() {
        //rotate the pitch around the X axis
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //rotate the yaw around the Y axis
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //translate to the position vector's location
        glTranslatef(position.x, position.y, position.z);

        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x).put(lPosition.y).put(lPosition.z).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }

    public void gameLoop() throws InterruptedException{

        FPCameraController camera = new FPCameraController(5, 5, 5);
        float dx;
        float dy;
        //float dt = 0.0f; //length of frame
        //float lastTime = 0.0f; // when the last frame was
        //long time = 0;
        float mouseSensitivity = 0.09f;
        float movementSpeed = .35f;


        Mouse.setGrabbed(true); //Hide mouse in program


        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
            dx = Mouse.getDX(); //distance in mouse movement
            dy = Mouse.getDY();//distance in mouse movement
            camera.yaw(dx * mouseSensitivity);
            camera.pitch(dy * mouseSensitivity);

            if (Keyboard.isKeyDown(Keyboard.KEY_W)){
                camera.walkForward(movementSpeed);
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_S)){
                camera.walkBackwards(movementSpeed);
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_A)){
                camera.strafeLeft(movementSpeed);
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_D)){
                camera.strafeRight(movementSpeed);
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                camera.moveUp(movementSpeed);
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                camera.moveDown(movementSpeed);
            }

            if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
                chunk = new Chunk(0,0,0);
            }

            glLoadIdentity();//set the modelview matrix back to the identity
            camera.lookThrough(); //look through the camera before you draw anything
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            chunk.render();

            //renderCube();
            Display.update();
            Display.sync(60);
        }
        Display.destroy();
    }

    private boolean isInboundsY(float newY) {
        if (newY > UPPER_Y_BOUND && newY < LOWER_Y_BOUND) {
            return true;
        }
        return false;
    }

    private boolean noCollisionYUp(float newY) {
        xGrid = (int) (position.x / .1);
        zGrid = (int) (position.z / .1);

        if (xGrid > -30 && xGrid < 1 && zGrid > -30 && zGrid < 1) {
            xGrid = Math.abs(xGrid);
            zGrid = Math.abs(zGrid);
            if (position.y > 0 && newY < 0) {
                return false;
            }
        }
        return true;
    }

    private boolean noCollisionYDown(float newY) {
        xGrid = (int) (position.x / .1);
        zGrid = (int) (position.z / .1);

        if (xGrid > -30 && xGrid < 1 && zGrid > -30 && zGrid < 1) {
            xGrid = Math.abs(xGrid);
            zGrid = Math.abs(zGrid);
            if (position.y < heightMap[xGrid][zGrid] && newY > heightMap[xGrid][zGrid]) {
                return false;
            }
        }
        return true;
    }

    private boolean isInboundsXZ(float newX, float newZ) {
        if (newX < UPPER_X_BOUND && newX > LOWER_X_BOUND
                && newZ < UPPER_Z_BOUND && newZ > LOWER_Z_BOUND) {
            return true;
        }
        return false;
    }

    private boolean noCollisionXZ(float newX, float newZ) {
        xGrid = (int) (newX / .1);
        zGrid = (int) (newZ / .1);

        if (xGrid > -30 && xGrid < 1 && zGrid > -30 && zGrid < 1) {
            xGrid = Math.abs(xGrid);
            zGrid = Math.abs(zGrid);
            if (position.y < 0 && position.y > heightMap[xGrid][zGrid]) {
                return false;
            }
        }
        return true;
    }

    //method: render
    //purpose: renders a square
    private void render() {
        try{
            glBegin(GL_QUADS);
            glColor3f(2.0f,0.0f,2.0f);
            glVertex3f( 1.0f,-2.0f,-1.0f);
            glVertex3f(-1.0f,-2.0f,-1.0f);
            glVertex3f(-1.0f, 2.0f,-1.0f);
            glVertex3f( 1.0f, 2.0f,-1.0f);
            glEnd();
        }
        catch(Exception e){}
    }
    //method: renderCube
    //purpose: renders a cube
    private void renderCube() {
        try {
            //Top Side
            glBegin(GL_QUADS);
            glColor3f(0.098f, 0.098f, 0.439f);
            glVertex3f(1.0f, 1.0f, -1.0f);
            glVertex3f(-1.0f, 1.0f, -1.0f);
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(1.0f, 1.0f, 1.0f);

            //Bottom Side
            glColor3f(0.373f, 0.620f, 0.627f);
            glVertex3f(1.0f, -1.0f, 1.0f);
            glVertex3f(-1.0f, -1.0f, 1.0f);
            glVertex3f(-1.0f, -1.0f, -1.0f);
            glVertex3f(1.0f, -1.0f, -1.0f);

            //Front Side
            glColor3f(0.000f, 0.502f, 0.502f);
            glVertex3f(1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f, -1.0f, 1.0f);
            glVertex3f(1.0f, -1.0f, 1.0f);

            //Back Side
            glColor3f(0.400f, 0.804f, 0.667f);
            glVertex3f(1.0f, -1.0f, -1.0f);
            glVertex3f(-1.0f, -1.0f, -1.0f);
            glVertex3f(-1.0f, 1.0f, -1.0f);
            glVertex3f(1.0f, 1.0f, -1.0f);

            //Left Side
            glColor3f(1.000f, 0.000f, 1.000f);
            glVertex3f(-1.0f, 1.0f, 1.0f);
            glVertex3f(-1.0f, 1.0f, -1.0f);
            glVertex3f(-1.0f, -1.0f, -1.0f);
            glVertex3f(-1.0f, -1.0f, 1.0f);

            //Right Side
            glColor3f(1.000f, 0.647f, 0.000f);
            glVertex3f(1.0f, 1.0f, -1.0f);
            glVertex3f(1.0f, 1.0f, 1.0f);
            glVertex3f(1.0f, -1.0f, 1.0f);
            glVertex3f(1.0f, -1.0f, -1.0f);
            glEnd();


        } catch (Exception e) {

        }
    }
    private static final Logger LOG = Logger.getLogger(FPCameraController.class.getName());
    public Chunk getChunk() {
        return chunk;
    }
}
