package fr.formiko.twoofortyeight;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;

import space.earlygrey.shapedrawer.ShapeDrawer;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    //private Texture image;
    private static ShapeDrawer schdraw;
    private Field field;
    private Stage stage; 
    
    public static ShapeDrawer getShapeDrawer(Batch batch){
        if (schdraw == null) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(Color.WHITE);
            pixmap.drawPixel(0, 0);
            Texture texture = new Texture(pixmap); // remember to dispose of later
            pixmap.dispose();
            TextureRegion region = new TextureRegion(texture, 0, 0, 1, 1);
            schdraw = new ShapeDrawer(batch, region);
        }
        return schdraw;
    }
    
    @Override
    public void create() {
        this.field = new Field(0, 0);
        // this.field = new Field(Gdx.graphics.getWidth() / 2 - Field.SQUARE_SIZE, Gdx.graphics.getHeight() / 2 - 2 * Field.SQUARE_SIZE );
        // this.field.setX(0);
        // this.field.setY(0);
        batch = new SpriteBatch();
        this.stage = new Stage();
        stage.addActor(field);
    }

    @Override
    public void render() {
        ScreenUtils.clear(1.0f, 1.0f, 1.0f, 1.0f);
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        //image.dispose();
    }
}
