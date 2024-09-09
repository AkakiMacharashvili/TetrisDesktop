import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class TetrisModelTest {
    private TetrisModel model;

    @BeforeEach
    public void setUp() {
        model = new TetrisModel(TetrisModel.DEFAULT_WIDTH, TetrisModel.DEFAULT_HEIGHT, TetrisModel.DEFAULT_COLORS_NUMBER);
    }

    @Test
    public void shouldInitializeModelWithDefaultValues() {
        Pair size = model.size();
        assertEquals(10, size.x());
        assertEquals(20, size.y());
        assertNotNull(model.state.field);
        assertEquals(size.y(), model.state.field.length);
    }

    @Test
    public void shouldHaveDefaultNumberOfColors() {
        assertEquals(TetrisModel.DEFAULT_COLORS_NUMBER, model.maxColors);
    }

    @Test
    public void shouldInitializeFigure() {
        assertNotNull(model.state.figure);
    }

    @Test
    public void shouldInitializePositionCorrectly() {
        Pair position = model.state.position;
        assertNotNull(position);
        assertEquals(model.size().x() / 2 - 2, position.x());
        assertEquals(0, position.y());
    }

    @Test
    public void shouldSlideDownCorrectly() {
        Pair oldPosition = model.state.position;
        model.slideDown();
        assertEquals(new Pair(oldPosition.x(), oldPosition.y() + 1), model.state.position);
    }

    @Test
    public void shouldNotOverlapFieldCellsAfterSlideDown() {
        model.state.field[2][model.size().x() / 2] = 1;
        Pair oldPosition = model.state.position;
        model.slideDown();

        assertTrue(model.isNewFigurePositionValid(model.state.position));
        assertEquals(new Pair(oldPosition.x(), oldPosition.y() + 1), model.state.position);
    }

    @Test
    public void shouldRotateFigureCorrectly() {
        model.state.figure = FigureFactory.J();
        model.rotate();
        int[][] rotated = FigureFactory.rotatedJ();
        assertTrue(Arrays.deepEquals(model.state.figure, rotated));
    }

    @Test
    public void shouldMoveLeftCorrectly() {
        Pair oldPosition = model.state.position;
        model.moveLeft();
        assertEquals(oldPosition.x() - 1, model.state.position.x());
    }

    @Test
    public void shouldMoveRightCorrectly() {
        Pair oldPosition = model.state.position;
        model.moveRight();
        assertEquals(oldPosition.x() + 1, model.state.position.x());
    }

    @Test
    public void shouldDetectFullRowsOfOnesAndZeros() {
        assertFalse(model.isRowFullOfOnes(1));
        assertTrue(model.isRowFullOfZeros(1));
    }

    @Test
    public void shouldShiftRowsDownCorrectly() {
        int[][] field = {
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 1, 1, 0},
                {0, 0, 0, 0}
        };

        field = model.shiftRowsDown(field);

        int[][] expected = {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 1, 1, 0}
        };
        Arrays.deepEquals(expected, field);
    }

    @Test
    public void shouldAddAndRemoveListenersCorrectly() {
        ModelListener listener = new ModelListener() {
            @Override
            public void onChange(TetrisModel tetrisModel) { }
            @Override
            public void over(TetrisModel tetrisModel) { }
        };

        model.addListener(listener);
        assertEquals(1, model.listeners.size());

        model.removeListener(listener);
        assertEquals(0, model.listeners.size());
    }

    @Test
    public void shouldUpdateScoreCorrectly() {
        int currentScore = model.score;
        model.updateScore(100);
        assertEquals(currentScore + 100, model.score);
    }

    @Test
    public void shouldSetInfiniteModeCorrectly() {
        model.infinite();
        assertFalse(model.FirstTry);
    }

    @Test
    public void shouldLevelUpCorrectly() {
        model.levelUp();
        assertEquals(TetrisModel.DEFAULT_MAX_LEVEL - 100, model.level);
        for (int i = 0; i < 10; i++) model.levelUp();
        assertEquals(100, model.level);
    }

    @Test
    public void shouldLevelDownCorrectly() {
        model.levelUp();
        for (int i = 0; i < 10; i++) model.levelUp();
        assertEquals(100, model.level);
        model.levelDown();
        assertEquals(200, model.level);
    }

    @Test
    public void shouldResetGameCorrectly() {
        model.finished = true;
        model.levelUp();
        model.score += 100;
        model.reset();
        assertEquals(TetrisModel.DEFAULT_HEIGHT, model.state.field.length);
        assertEquals(TetrisModel.DEFAULT_WIDTH, model.state.field[0].length);
        assertFalse(model.finished);
        assertEquals(0, model.score);
    }

    @Test
    public void shouldPauseGameCorrectly() {
        assertFalse(model.paused);
        model.pause();
        assertTrue(model.paused);
    }

    @Test
    public void shouldProgressToNextLevelCorrectly() {
        model.score = 300;
        model.nextLevel();
        assertEquals(700, model.level);
    }

    @Test
    public void shouldInitializeNewFigureCorrectly() {
        model.initFigure();
        assertEquals(new Pair(TetrisModel.DEFAULT_WIDTH / 2 - 2, 0), model.state.position);
    }

    @Test
    public void shouldDetectGameOverCorrectly() {
        assertFalse(model.finished);
        model.gameOver();
        assertTrue(model.finished);
    }

    @Test
    public void shouldShiftRowsDownAndDeleteFullRows() {
        for (int i = 0; i < model.state.field[0].length; i++) model.state.field[0][i] = 1;
        model.deleteFullRows();
        assertTrue(Arrays.stream(model.state.field[0]).allMatch(val -> val == 0));
    }

    @Test
    public void shouldCheckAbsolutePositionCorrectly() {
        model.state.field[10][9] = 1;
        assertFalse(model.checkAbsPos(new Pair(10, 0)));
        assertFalse(model.checkAbsPos(new Pair(0, 20)));
        assertFalse(model.checkAbsPos(new Pair(-1, 0)));
        assertFalse(model.checkAbsPos(new Pair(0, -1)));
        assertTrue(model.checkAbsPos(new Pair(3, 3)));
        assertFalse(model.checkAbsPos(new Pair(10, 9)));
    }

    @Test
    public void shouldDetectFullRowsOfOnes() {
        Arrays.fill(model.state.field[0], 1);
        assertTrue(model.isRowFullOfOnes(0));
        assertFalse(model.isRowFullOfOnes(10));
    }

    @Test
    public void shouldDetectFullRowsOfZeros() {
        assertTrue(model.isRowFullOfZeros(0));
        model.state.field[0][0] = 1;
        assertFalse(model.isRowFullOfZeros(0));
    }
}
