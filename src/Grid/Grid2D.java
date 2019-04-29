/**
 * @author Joshua Chen, Bryan Lee, Saul Galaviz, Camille Nibungco
 * Date Created: Mar 11, 2019
 */
package Grid;

public class Grid2D {

    private float x;
    private float y;

    public Grid2D() {
    }

    public Grid2D(Grid2D coord) {
        this.x = coord.x;
        this.y = coord.y;
    }

    public Grid2D(float x, float y) {
        this.x = x;
        this.y = y;
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
    
}
