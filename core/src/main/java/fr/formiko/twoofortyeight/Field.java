package fr.formiko.twoofortyeight;

import java.util.stream.Stream;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;


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
        // generateNewElement();
        this.elements[0][0].setValue(2);
        this.elements[0][3].setValue(4);
        //this.elements[3][0].setValue(8);
        //this.elements[3][3].setValue(1);
        this.scoreLabel = new Label("Score : " + score, new Label.LabelStyle(Fonts.getFont(30), null));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        boolean hasMoved = false;
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            moveright();
            hasMoved = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            moveleft();
            hasMoved = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            moveUp();
            hasMoved = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            moveDown();
            hasMoved = true;
        }
        if (hasMoved) {
            generateNewElement();
            gameOver();
        }
    }

    private void moveright() {
        for (int i = COLS - 1; i >= 0; i--) {
            for (int j = 0; j < ROWS; j++) {
                FieldElement actor = this.elements[i][j];
                if (actor.getValue() == 0) {
                    continue;
                }
                int howManyColumnsCanIGoRight = howManyColumnsCanIGoRight(i, j);
                switchActorAt(i + howManyColumnsCanIGoRight, j, i, j);
                if (i + howManyColumnsCanIGoRight < COLS - 1) {
                    if (this.elements[i + howManyColumnsCanIGoRight][j]
                            .getValue() == this.elements[i + howManyColumnsCanIGoRight + 1][j].getValue()) {
                        this.elements[i + howManyColumnsCanIGoRight + 1][j]
                                .setValue(this.elements[i + howManyColumnsCanIGoRight][j].getValue() * 2);
                        this.elements[i + howManyColumnsCanIGoRight][j].setValue(0);
                    }

                }
            }
        }
    }

    public void moveleft() {
        for (int i = 0; i < COLS; i++) {
            for (int j = 0; j < ROWS; j++) {
                FieldElement actor = this.elements[i][j];
                if (actor.getValue() == 0) {
                    continue;
                }
                int howManyColumnsCanIGoLeft = howManyColumnsCanIGoLeft(i, j);
                switchActorAt(i - howManyColumnsCanIGoLeft, j, i, j);
                if (i - howManyColumnsCanIGoLeft > 0) {
                    if (this.elements[i - howManyColumnsCanIGoLeft][j]
                            .getValue() == this.elements[i - howManyColumnsCanIGoLeft - 1][j].getValue()) {
                        this.elements[i - howManyColumnsCanIGoLeft - 1][j]
                                .setValue(this.elements[i - howManyColumnsCanIGoLeft][j].getValue() * 2);
                        this.elements[i - howManyColumnsCanIGoLeft][j].setValue(0);
                    }
                }
            }
        }
    }

    private void moveUp() {
        for (int i = ROWS - 1; i >= 0; i--) {
            for (int j = 0; j < COLS; j++) {
                System.out.println("i : " + i + " j : " + j);
                FieldElement actor = this.elements[j][i];
                if (actor.getValue() == 0) {
                    continue;
                }
                int howManyRowsCanIGoUp = howManyRowsCanIGoUp(j, i);
                switchActorAt(j, i + howManyRowsCanIGoUp, j, i);
                if (i + howManyRowsCanIGoUp < ROWS - 1) {
                    if (this.elements[j][i + howManyRowsCanIGoUp]
                            .getValue() == this.elements[j][i + howManyRowsCanIGoUp + 1].getValue()) {
                        this.elements[j][i + howManyRowsCanIGoUp + 1]
                                .setValue(this.elements[j][i + howManyRowsCanIGoUp].getValue() * 2);
                        this.elements[j][i + howManyRowsCanIGoUp].setValue(0);
                    }
                }
            }
        }
    }

    private void moveDown() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                FieldElement actor = this.elements[j][i];
                if (actor.getValue() == 0) {
                    continue;
                }
                int howManyRowsCanIGoDown = howManyRowsCanIGoDown(j, i);
                switchActorAt(j, i - howManyRowsCanIGoDown, j, i);
                if (i - howManyRowsCanIGoDown > 0) {
                    if (this.elements[j][i - howManyRowsCanIGoDown]
                            .getValue() == this.elements[j][i - howManyRowsCanIGoDown - 1].getValue()) {
                        this.elements[j][i - howManyRowsCanIGoDown - 1]
                                .setValue(this.elements[j][i - howManyRowsCanIGoDown].getValue() * 2);
                        this.elements[j][i - howManyRowsCanIGoDown].setValue(0);
                    }
                }
            }
        }
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

    private void gameOver() {
        if (Stream.of(elements).allMatch(row -> Stream.of(row).allMatch(e -> e.getValue() != 0))) {
            System.out.println("Game Over");
            
        }
    }
}
