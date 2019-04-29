/**
 * @author Joshua Chen, Bryan Lee, Saul Galaviz, Camille Nibungco
 * Date Created: Mar 26, 2019
 */
package Models;

import Grid.Grid2D;
import Grid.Grid3D;
import org.newdawn.slick.opengl.Texture;

import static org.lwjgl.opengl.GL11.glTexCoord2f;

public class CubeModel extends BaseModel implements TransformInterface {

    private static int[] textureGrid;

    static {
        textureGrid = new int[6];
        textureGrid[0] = 0;
        textureGrid[1] = 1;
        textureGrid[2] = 0;
        textureGrid[3] = 2;
        textureGrid[4] = 0;
        textureGrid[5] = 0;
    }

    private Texture texture;
    private int textureCubeSize; // T
    private Grid2D[][] textureVertices;

    private CubeType type;

    private Grid3D scale;
    private Grid3D translation;
    private Float rotationAngle;
    private Grid3D rotationPivot;

    private Grid3D[] colors;

    public CubeModel(Grid3D[] corner, CubeType type) {
        super(corner, MeshType.Quad);

        this.type = type;

        //1 for each face
        colors = new Grid3D[8];
        for (int i = 0; i < 8; i++) {
            float r = (i & 4);
            float g = (i & 2);
            float b = (i & 1);

            colors[i] = new Grid3D(r, g, b);
        }
    }

    // method: computeVertices
    // purpose: compute vertices from a 3d grid
    private static Grid3D[] computeVertices(Grid3D grid, float len, float height, float depth) {
        Grid3D[] corner = new Grid3D[8];
        corner[0] = grid;
        corner[1] = new Grid3D(grid.getX() + len, grid.getY(), grid.getZ());
        corner[2] = new Grid3D(grid.getX(), grid.getY() + height, grid.getZ());
        corner[3] = new Grid3D(grid.getX() + len, grid.getY() + height, grid.getZ());
        corner[4] = new Grid3D(grid.getX(), grid.getY() + height, grid.getZ() + depth);
        corner[5] = new Grid3D(grid.getX() + len, grid.getY() + height, grid.getZ() + depth);
        corner[6] = new Grid3D(grid.getX(), grid.getY(), grid.getZ() + depth);
        corner[7] = new Grid3D(grid.getX() + len, grid.getY(), grid.getZ() + depth);
        return corner;
    }

    public CubeModel(Grid3D leftBottomFront, float length, float height, float depth, CubeType type) {
        this(computeVertices(leftBottomFront, length, height, depth), type);
    }

    public CubeModel(Grid3D leftBottomFront, float size, CubeType type) {
        this(computeVertices(leftBottomFront, size, size, size), type);
    }

    // method: computeTextrueVertices
    // purpose: compute the texture of vertices 
    private void computeTextureVertices() {
        int T = this.textureCubeSize;
        int y = this.type.getTextureYIndex();

        int bY = T * y;
        int tY = bY + T;

        float width = texture.getTextureWidth();
        float height = texture.getTextureHeight();
        this.textureVertices = new Grid2D[6][4];

        for (int i = 0; i < 4; ++i) {
            int lX = textureGrid[i] * T;
            int rX = lX + T;

            textureVertices[i][0] = new Grid2D(lX / width, bY / height);
            textureVertices[i][1] = new Grid2D(rX / width, bY / height);
            textureVertices[i][2] = new Grid2D(lX / width, tY / height);
            textureVertices[i][3] = new Grid2D(rX / width, tY / height);
        }

        for (int i = 4; i < 6; ++i) {
            int lX = textureGrid[i] * T;
            int rX = lX + T;

            textureVertices[i][0] = new Grid2D(lX / width, tY / height);
            textureVertices[i][1] = new Grid2D(lX / width, bY / height);
            textureVertices[i][2] = new Grid2D(rX / width, bY / height);
            textureVertices[i][3] = new Grid2D(rX / width, tY / height);
        }
    }

    // method: draw
    // purpose: draw objects 
    @Override
    public void draw() {
        int c = 1;
        this.texture.bind();
        for (int b = 0; b < 8; b += 2) {

            glTexCoord2f(textureVertices[c - 1][0].getX(), textureVertices[c - 1][0].getY());
            vertList.get(b).draw();

            glTexCoord2f(textureVertices[c - 1][1].getX(), textureVertices[c - 1][1].getY());
            vertList.get(b + 1).draw();

            glTexCoord2f(textureVertices[c - 1][2].getX(), textureVertices[c - 1][2].getY());
            vertList.get((b + 3) % vertList.size()).draw();

            glTexCoord2f(textureVertices[c - 1][3].getX(), textureVertices[c - 1][3].getY());
            vertList.get((b + 2) % vertList.size()).draw();
            c++;
        }

        for (int b = 0; b < 2; b++) {
            glTexCoord2f(textureVertices[c - 1][0].getX(), textureVertices[c - 1][0].getY());
            vertList.get(b).draw();

            glTexCoord2f(textureVertices[c - 1][1].getX(), textureVertices[c - 1][1].getY());
            vertList.get(b + 2).draw();

            glTexCoord2f(textureVertices[c - 1][2].getX(), textureVertices[c - 1][2].getY());
            vertList.get(b + 4).draw();

            glTexCoord2f(textureVertices[c - 1][3].getX(), textureVertices[c - 1][3].getY());
            vertList.get(b + 6).draw();
            c++;
        }
    }

    // method: setRotation
    // purpose: set rotation 
    @Override
    public void setRotation(float angle, Grid3D pivot) {
        this.rotationAngle = angle;
        this.rotationPivot = pivot;
    }

    // method: setTranslation
    // purpose: set translation 
    @Override
    public void setTranslation(Grid3D translation) {
        this.translation = translation;
    }

    // method: setScale
    // purpose: set scale 
    @Override
    public void setScale(Grid3D scale) {
        this.scale = scale;
    }

    // method: getRotationAngle
    // purpose: get rotation angle 
    @Override
    public Float getRotationAngle() {
        return this.rotationAngle;
    }

    // method: getRotationPivot
    // purpose: get rotation pivot 
    @Override
    public Grid3D getRotationPivot() {
        return this.rotationPivot;
    }

    // method: getTranslation
    // purpose: get translation 
    @Override
    public Grid3D getTranslation() {
        return this.getTranslation();
    }

    // method: getScale
    // purpose: get scale 
    @Override
    public Grid3D getScale() {
        return this.scale;
    }

    // method: setTexture
    // purpose: set texture
    @Override
    public void setTexture(Texture texture, int textureCubeSize) {
        this.texture = texture;
        this.textureCubeSize = textureCubeSize;
        computeTextureVertices();
    }

}
