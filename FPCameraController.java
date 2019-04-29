import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
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

    private Vector3f position = null;
    private Vector3f lPosition = null;
    private Vector3f newPosition = null;
    private float yaw = 0.0f;
    private float pitch = 0.0f;
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
        position = new Vector3f(x, y, z);
        newPosition = new Vector3f(x, y, z);
        lPosition = new Vector3f(x, y, z);
        lPosition.x = 2f;
        lPosition.y = 15f;
        lPosition.z = 2f;
        chunk = new Chunk();
        //Instantiates our Chunk Class
        //chunk = new Chunk();
        heightMap = chunk.getHeightMap();
    }

    // method: yaw
    // purpose: increment the camera's current yaw rotation
    public void yaw(float amount) {
        //increment the yaw by the amount param
        yaw += amount;
    }

    // method: pitch
    // purpose: increment the camera's current yaw rotation
    public void pitch(float amount) {
        //increment the pitch by the amount param
        pitch -= amount;
    }

    // method: walkForward
    // purpose: moves the camera forward relative to its current rotation (yaw)
    public void walkForward(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw));
        newPosition.x = position.x - xOffset;
        newPosition.z = position.z + zOffset;

        if (isInboundsXZ(newPosition.x, newPosition.z) && noCollisionXZ(newPosition.x, newPosition.z)) {
            position.x -= xOffset;
            position.z += zOffset;
        }

        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x -= xOffset).put(lPosition.y).put(lPosition.z += zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }

    // method: walkBackwards
    // purpose: moves the camera backward relative to its current rotation (yaw)
    public void walkBackwards(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw));
        newPosition.x = position.x + xOffset;
        newPosition.z = position.z - zOffset;

        if (isInboundsXZ(newPosition.x, newPosition.z) && noCollisionXZ(newPosition.x, newPosition.z)) {
            position.x += xOffset;
            position.z -= zOffset;
        }

        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x += xOffset).put(lPosition.y).put(lPosition.z -= zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }

    // method: strafeLeft
    // purpose: strafes the camera left relative to its current rotation (yaw)
    public void strafeLeft(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw - 90));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw - 90));
        newPosition.x = position.x - xOffset;
        newPosition.z = position.z + zOffset;

        if (isInboundsXZ(newPosition.x, newPosition.z) && noCollisionXZ(newPosition.x, newPosition.z)) {
            position.x -= xOffset;
            position.z += zOffset;
        }

        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x -= xOffset).put(lPosition.y).put(lPosition.z += zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }

    // method: strafeRight
    // purpose: strafes the camera right relative to its current rotation (yaw)
    public void strafeRight(float distance) {
        float xOffset = distance * (float) Math.sin(Math.toRadians(yaw + 90));
        float zOffset = distance * (float) Math.cos(Math.toRadians(yaw + 90));
        newPosition.x = position.x - xOffset;
        newPosition.z = position.z + zOffset;

        if (isInboundsXZ(newPosition.x, newPosition.z) && noCollisionXZ(newPosition.x, newPosition.z)) {
            position.x -= xOffset;
            position.z += zOffset;
        }

        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x -= xOffset).put(lPosition.y).put(lPosition.z += zOffset).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }

    // method: moveUp
    // purpose: moves the camera up relative to its current rotation (yaw)
    public void moveUp(float distance) {
        float newY = position.y - distance;
        if (isInboundsY(newY) && noCollisionYUp(newY)) {
            position.y -= distance;
        }
    }

    // method: moveDown
    // purpose: moves the camera down
    public void moveDown(float distance) {
        float newY = position.y + distance;
        if (isInboundsY(newY) && noCollisionYDown(newY)) {
            position.y += distance;
        }
    }

    // method: lookThrough
    // purpose: translates and rotate the matrix so that it looks through the camera
    //this does basically what gluLookAt() does
    public void lookThrough() {
        //roatate the pitch around the X axis
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //roatate the yaw around the Y axis
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //translate to the position vector's location
        glTranslatef(position.x, position.y, position.z);

        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x).put(lPosition.y).put(lPosition.z).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);
    }

    // method: gameLoop
    // purpose: graphic render loop
    public void gameLoop() {
        //FPCameraController camera = new FPCameraController(0, 0, 0);
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f;
        float lastTime = 0.0f;
        long time = 0;
        float mouseSensitivity = 0.09f;
        float movementSpeed = 0.05f;

        Mouse.setGrabbed(true);
        
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {time = Sys.getTime();
            lastTime = time;

            dx = Mouse.getDX();

            dy = Mouse.getDY();

            this.yaw(dx * mouseSensitivity);

            this.pitch(dy * mouseSensitivity);

            if (Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                this.walkForward(movementSpeed);
            }
            //move backwards
            if (Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                this.walkBackwards(movementSpeed);
            }
            //strafe left
            if (Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                this.strafeLeft(movementSpeed);
            }
            //strafe right
            if (Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                this.strafeRight(movementSpeed);
            }
            //move up
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                this.moveUp(movementSpeed);
            }
            //move down
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                this.moveDown(movementSpeed);
            }
            
            if(Keyboard.isKeyDown(Keyboard.KEY_R))
            {
                chunk = new Chunk();
            }
            //set the modelview matrix back to the identity
            glLoadIdentity();
            //look through the camera before you draw anything
            this.lookThrough();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            //you would draw your scene here.
            render();
            //draw the buffer to the screen
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

    // method: render
    // purpose: graphic render
    private void render() {
        try {
            //Cube cube = new Cube(0,0,0);
            //cube.renderChunk(30);
            glBegin(GL_QUADS);
            chunk.draw();
            glEnd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
