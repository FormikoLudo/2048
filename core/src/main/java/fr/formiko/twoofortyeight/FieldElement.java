package fr.formiko.twoofortyeight;

import java.util.Map;
import java.util.logging.FileHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class FieldElement extends Actor {

    private int value;
    private Label label;
    public FieldElement(float x, float y, int value) {
        super();
        this.setX(x);
        this.setY(y);
        this.value = value;
        LabelStyle style = new LabelStyle(Fonts.getFont(30), Color.BLACK);
        this.label = new Label(String.valueOf(value), style);      
    }

    public FieldElement collide(FieldElement other) {
        if (this.value == other.value) {
            return new FieldElement(0, 0, this.value + other.value);
        }
        return this;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        ShapeDrawer shape = Main.getShapeDrawer(batch);
        shape.filledRectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight(), getColorFromValue(value));
        label.setPosition(this.getX() + this.getWidth() / 2 - label.getWidth() / 2, this.getY() + this.getHeight() / 2 - label.getHeight() / 2);
        if(this.value != 0) label.draw(batch, parentAlpha);
    }

    private static Map<Integer, Color> bgColors;
    static {
        bgColors = Map.of(0,Color.LIGHT_GRAY,2, Color.GRAY, 4, Color.GRAY);
    }
    public void setValue(int value) {
        this.value = value;
        label.setText(String.valueOf(value));
        label.pack();
    }
    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return String.format("x : %f, y : %f\n", getX(), getY());
    }
    
    private Color getColorFromHexString(String hex) {
        return new Color(Integer.valueOf(hex.substring(1, 3), 16) / 255f, Integer.valueOf(hex.substring(3, 5), 16) / 255f, Integer.valueOf(hex.substring(5, 7), 16) / 255f, 1);
    }

    private Color getColorFromValue(int value) {
        FileHandle f = Gdx.files.internal("bgcolors.yml");
        String content = f.readString();
        String[] lines = content.split("\n");
        for (String line : lines) {
            String[] parts = line.split(":");
            if (Integer.parseInt(parts[0].trim()) == value) {
                return getColorFromHexString(parts[1].trim());
            }
        }
        return Color.WHITE;
    }
}
