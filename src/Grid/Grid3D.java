/**
 * @author Joshua Chen, Bryan Lee, Saul Galaviz, Camille Nibungco
 * Date Created: Mar 10, 2019
 */
package Grid;

import Models.DrawModelInterface;
import org.newdawn.slick.opengl.Texture;

import static org.lwjgl.opengl.GL11.glVertex3f;

public class Grid3D implements DrawModelInterface {

    private float x;
    private float y;
    private float z;

    public Grid3D() {
    }

    public Grid3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // method: getX
    // purpose: get x 
    public float getX() {
        return x;
    }

    // method: getY
    // purpose: get y 
    public float getY() {
        return y;
    }

    // method: getZ
    // purpose: get z 
    public float getZ() {
        return z;
    }

    // method: setX
    // purpose: set x 
    public void setX(float x) {
        this.x = x;
    }

    // method: setY
    // purpose: set y 
    public void setY(float y) {
        this.y = y;
    }

    // method: setZ
    // purpose: set z 
    public void setZ(float z) {
        this.z = z;
    }

    // method: draw
    // purpose: draw 3d
    @Override
    public void draw() {
        glVertex3f(x, y, z);
    }

    @Override
    public void setTexture(Texture texture, int textureCubeSize) {

    }
    
}
