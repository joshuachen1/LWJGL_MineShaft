/**
 * @author Joshua Chen, Bryan Lee, Saul Galaviz, Camille Nibungco
 * Date Created: Mar 11, 2019
 */
import org.newdawn.slick.opengl.Texture;

public interface Flyweight {

    public void setTexture(Texture texture, int textureCubeSize);

    public void draw();

}
