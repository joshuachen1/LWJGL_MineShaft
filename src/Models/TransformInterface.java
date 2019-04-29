/**
 * @author Joshua Chen, Bryan Lee, Saul Galaviz, Camille Nibungco
 * Date Created: Mar 11, 2019
 */
package Models;

import Grid.Grid3D;

public interface TransformInterface {

    public void setRotation(float angle, Grid3D pivot);

    public void setTranslation(Grid3D translation);

    public void setScale(Grid3D scale);

    public Float getRotationAngle();

    public Grid3D getRotationPivot();

    public Grid3D getTranslation();

    public Grid3D getScale();
    
}
