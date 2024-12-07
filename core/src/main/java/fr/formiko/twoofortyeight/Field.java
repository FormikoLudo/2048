package fr.formiko.twoofortyeight;

import java.util.stream.Stream;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;

import space.earlygrey.shapedrawer.ShapeDrawer;


public class Field extends Group {

    private FieldElement[][] elements;
    public static final int ROWS = 4;
    public static final int COLS = 4;
    public static final int SQUARE_SIZE = Gdx.graphics.getWidth() / 5 - 50;
    private int score = 0;
    private Label scoreLabel;

    public Field(float x, float y) {
        this.setPosition(x, y);
        this.elements = new FieldElement[COLS][ROWS];
        for (int i = 0; i < COLS; i++) {
            for (int j = 0; j < ROWS; j++) {
                FieldElement fe = new FieldElement(getX() + i * (SQUARE_SIZE + 10), getY() + j * (SQUARE_SIZE + 10), 0);
                fe.setSize(SQUARE_SIZE, SQUARE_SIZE);
                this.addActor(fe);

                this.elements[i][j] = fe;
            }
        }
        this.setSize((SQUARE_SIZE + 10) * COLS, (SQUARE_SIZE + 10) * ROWS);
        generateNewElement();
        generateNewElement();
        this.scoreLabel = new Label("Score : " + score, new Label.LabelStyle(Fonts.getFont(30), Color.BLACK));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        boolean hasMoved = false;
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            hasMoved = moveright();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            hasMoved = moveleft();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            hasMoved = moveUp();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            hasMoved = moveDown();
        }
        if (hasMoved) {
            generateNewElement();
            checkGameOver();
            resetAllMerged();
        }
    }

    private void resetAllMerged() {
        for (int i = 0; i < COLS; i++) {
            for (int j = 0; j < ROWS; j++) {
                this.elements[i][j].resetMerged();
            }
        }
    }

    private boolean sameValueAndNotMerged(int x1, int y1, int x2, int y2) {
        return this.elements[x1][y1].getValue() == this.elements[x2][y2].getValue()
        && !this.elements[x1][y1].isMerged()
        && !this.elements[x2][y2].isMerged();
    }

    private boolean moveright() {
        boolean flag = false;
        for (int i = COLS - 1; i >= 0; i--) {
            for (int j = 0; j < ROWS; j++) {
                FieldElement actor = this.elements[i][j];
                if (actor.getValue() == 0) {
                    continue;
                }
                int howManyColumnsCanIGoRight = howManyColumnsCanIGoRight(i, j);
                if(howManyColumnsCanIGoRight != 0) {
                    flag = true;
                }
                switchActorAt(i + howManyColumnsCanIGoRight, j, i, j);
                if (i + howManyColumnsCanIGoRight < COLS - 1) {
                    if(sameValueAndNotMerged(i + howManyColumnsCanIGoRight, j, i + howManyColumnsCanIGoRight + 1, j)){
                        this.elements[i + howManyColumnsCanIGoRight + 1][j].doubleValue();
                        this.elements[i + howManyColumnsCanIGoRight][j].setValue(0);
                        this.score += this.elements[i + howManyColumnsCanIGoRight + 1][j].getValue();
                        flag = true;
                    }

                }
            }
        }
        return flag;
    }

    public boolean moveleft() {
        boolean flag = false;
        for (int i = 0; i < COLS; i++) {
            for (int j = 0; j < ROWS; j++) {
                FieldElement actor = this.elements[i][j];
                if (actor.getValue() == 0) {
                    continue;
                }
                int howManyColumnsCanIGoLeft = howManyColumnsCanIGoLeft(i, j);
                if(howManyColumnsCanIGoLeft != 0) {
                    flag = true;
                }
                switchActorAt(i - howManyColumnsCanIGoLeft, j, i, j);
                if (i - howManyColumnsCanIGoLeft > 0) {
                    if(sameValueAndNotMerged(i - howManyColumnsCanIGoLeft, j, i - howManyColumnsCanIGoLeft - 1, j)){
                        this.elements[i - howManyColumnsCanIGoLeft - 1][j].doubleValue();
                        this.elements[i - howManyColumnsCanIGoLeft][j].setValue(0);
                        flag = true;
                        score += this.elements[i - howManyColumnsCanIGoLeft - 1][j].getValue();
                    }
                }
            }
        }
        return flag;
    }

    private boolean moveUp() {
        boolean flag = false;
        for (int i = ROWS - 1; i >= 0; i--) {
            for (int j = 0; j < COLS; j++) {
                FieldElement actor = this.elements[j][i];
                if (actor.getValue() == 0) {
                    continue;
                }
                int howManyRowsCanIGoUp = howManyRowsCanIGoUp(j, i);
                if(howManyRowsCanIGoUp != 0) {
                    flag = true;
                }
                switchActorAt(j, i + howManyRowsCanIGoUp, j, i);
                if (i + howManyRowsCanIGoUp < ROWS - 1) {
                    if(sameValueAndNotMerged(j, i + howManyRowsCanIGoUp, j, i + howManyRowsCanIGoUp + 1)){
                        this.elements[j][i + howManyRowsCanIGoUp + 1].doubleValue();
                        this.elements[j][i + howManyRowsCanIGoUp].setValue(0);
                        flag = true;
                        score += this.elements[j][i + howManyRowsCanIGoUp + 1].getValue();
                    }
                }
            }
        }
        return flag;
    }

    private boolean moveDown() {
        boolean flag = false;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                FieldElement actor = this.elements[j][i];
                if (actor.getValue() == 0) {
                    continue;
                }
                int howManyRowsCanIGoDown = howManyRowsCanIGoDown(j, i);
                if(howManyRowsCanIGoDown != 0) {
                    flag = true;
                }
                switchActorAt(j, i - howManyRowsCanIGoDown, j, i);
                if (i - howManyRowsCanIGoDown > 0) {
                    if (sameValueAndNotMerged(j, i - howManyRowsCanIGoDown, j, i - howManyRowsCanIGoDown - 1)) {
                        this.elements[j][i - howManyRowsCanIGoDown - 1].doubleValue();
                        this.elements[j][i - howManyRowsCanIGoDown].setValue(0);
                        score += this.elements[j][i - howManyRowsCanIGoDown - 1].getValue();
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }
    private void switchActorAt(int targetX, int targetY, int originX, int originY) {
        assert targetX >= 0 && targetX < COLS;
        assert targetY >= 0 && targetY < ROWS;
        assert originX >= 0 && originX < COLS;
        assert originY >= 0 && originY < ROWS;

        FieldElement target = this.elements[targetX][targetY];
        FieldElement origin = this.elements[originX][originY];
        this.elements[targetX][targetY] = origin;
        this.elements[originX][originY] = target;
        origin.setPosition(getX() + targetX * (SQUARE_SIZE + 10), getY() + targetY * (SQUARE_SIZE + 10));
        target.setPosition(getX() + originX * (SQUARE_SIZE + 10), getY() + originY * (SQUARE_SIZE + 10));
    }

    private int howManyColumnsCanIGoRight(int x, int y) {
        int count = 0;
        for (int i = x + 1; i < COLS; i++) {
            if (this.elements[i][y].getValue() == 0) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    private int howManyColumnsCanIGoLeft(int x, int y) {
        int count = 0;
        for (int i = x - 1; i >= 0; i--) {
            if (this.elements[i][y].getValue() == 0) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    private int howManyRowsCanIGoUp(int x, int y) {
        int count = 0;
        for (int i = y + 1; i < ROWS; i++) {
            if (this.elements[x][i].getValue() == 0) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    private int howManyRowsCanIGoDown(int x, int y) {
        int count = 0;
        for (int i = y - 1; i >= 0; i--) {
            if (this.elements[x][i].getValue() == 0) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Field other = (Field) obj;
        if (COLS != other.COLS) {
            return false;
        }
        if (ROWS != other.ROWS) {
            return false;
        }
        if (elements == null) {
            if (other.elements != null) {
                return false;
            }
        } else if (!Stream.of(elements).equals(Stream.of(other.elements))) {
            return false;
        }
        return true;
    }

    private void generateNewElement() {
        int x = (int) (Math.random() * COLS);
        int y = (int) (Math.random() * ROWS);
        FieldElement fe = this.elements[x][y];
        System.out.println(fe);
        boolean four = Math.random() > 0.9;
        boolean isAllFilled = Stream.of(elements).allMatch(row -> Stream.of(row).allMatch(e -> e.getValue() != 0));
        if (isAllFilled) {
            return;
        }
        if (fe.getValue() == 0) {
            fe.setValue(four ? 4 : 2);
        } else {
            generateNewElement();
        }
    }

    private void checkGameOver() {
        if (Stream.of(elements).allMatch(row -> Stream.of(row).allMatch(e -> e.getValue() != 0))) {
            System.out.println("Game Over");
            
        }
    }

    @Override
    public void draw(com.badlogic.gdx.graphics.g2d.Batch batch, float parentAlpha) {
        ScreenUtils.clear(1.0f, 1.0f, 1.0f, 1.0f);
        super.draw(batch, parentAlpha);
        ShapeDrawer shapeDrawer = Main.getShapeDrawer(batch);
        shapeDrawer.filledRectangle(getX() - 10, getY() - 10, getWidth() + 10, getHeight() + 10, Color.LIGHT_GRAY);
        for (int i = 0; i < COLS; i++) {
            for (int j = 0; j < ROWS; j++) {
                FieldElement fe = this.elements[i][j];
                fe.draw(batch, parentAlpha);
            }
        }
        this.scoreLabel.setText("Score : " + score);
        this.scoreLabel.setPosition(0, Gdx.graphics.getHeight() - 50);
        this.scoreLabel.draw(batch, parentAlpha);
    }
}
