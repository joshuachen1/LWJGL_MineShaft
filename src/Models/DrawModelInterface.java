/**
 * @author Joshua Chen, Bryan Lee, Saul Galaviz, Camille Nibungco
 * Date Created: Mar 26, 2019
 */
package Models;

import org.newdawn.slick.opengl.Texture;

public interface DrawModelInterface {

    public void draw();

    public void setTexture(Texture texture, int cubeSize);
    
}
