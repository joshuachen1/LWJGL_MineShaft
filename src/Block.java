/**
 * @author Joshua Chen, Bryan Lee, Saul Galaviz, Camille Nibungco
 * Date Created: Mar 26, 2019
 */

import Grid.Grid3D;
import Models.CubeModel;
import Models.CubeType;
import org.newdawn.slick.opengl.Texture;

public class Block implements Flyweight {

    private final float CUBE_SIZE = .1f;
    CubeType cubeType;
    CubeModel cubemodel;

    public Block(CubeType cubeType) {
        this.cubeType = cubeType;
        cubemodel = new CubeModel(new Grid3D(), CUBE_SIZE, cubeType);
    }

    public Block(CubeType cubeType, float x, float y, float z) {
        this.cubeType = cubeType;
        cubemodel = new CubeModel(new Grid3D(x, y, z), CUBE_SIZE, cubeType);
    }

    @Override
    public void setTexture(Texture texture, int textureCubeSize) {
        cubemodel.setTexture(texture, textureCubeSize);
    }

    @Override
    public void draw() {
        cubemodel.draw();
    }
}