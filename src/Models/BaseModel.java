/**
 * @author Joshua Chen, Bryan Lee, Saul Galaviz, Camille Nibungco
 * Date Created: Mar 26, 2019
 */
package Models;

import Grid.Grid3D;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseModel implements DrawModelInterface {

    public enum MeshType {
        Triangle(3),
        Quad(4);

        private int verticeCount;

        private MeshType(int verticeCount) {
            this.verticeCount = verticeCount;
        }

        public int getVerticeCount() {
            return verticeCount;
        }
    };

    protected MeshType type;

    protected ArrayList<Grid3D> vertList;

    public BaseModel(Grid3D[] vertices, MeshType type) {
        this.type = type;

        this.vertList = new ArrayList<Grid3D>(vertices.length);
        for (Grid3D c : vertices) {
            this.vertList.add(c);
        }
    }

    public BaseModel(List<Grid3D> vertices, MeshType type) {
        this.type = type;

        this.vertList = new ArrayList<Grid3D>(vertices.size());
        for (Grid3D c : vertices) {
            this.vertList.add(c);
        }
    }

    public BaseModel(MeshType type) {
        this.type = type;

        this.vertList = new ArrayList<Grid3D>(type.getVerticeCount() * 6);
    }

    // method: draw
    // purpose: draw base model
    public void draw() {
        for (Grid3D d : this.vertList) {
            d.draw();
        }
    }
    
}
