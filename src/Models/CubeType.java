/**
 * @author Joshua Chen, Bryan Lee, Saul Galaviz, Camille Nibungco
 * Date Created: Mar 26, 2019
 */
package Models;

public enum CubeType {
    Grass(0),
    Dirt(1),
    Sand(2),
    Water(3),
    Stone(4),
    Bedrock(5);

    private int textureYIndex;

    private CubeType(int textureYIndex) {
        this.textureYIndex = textureYIndex;
    }

    // method: getTextureYIndex
    // purpose: get texture y index 
    int getTextureYIndex() {
        return this.textureYIndex;
    }

}
