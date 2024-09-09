import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TetrisStateTest {
    TetrisState tetrisState;

    @Before
    public void init(){
        TetrisModel tetrisModel = new TetrisModel();
        tetrisState = tetrisModel.state;
    }

    @Test
    public void testState(){
        assertNotNull(tetrisState.position);
        assertNotNull(tetrisState.figure);
        assertNotNull(tetrisState.field);
        assertNotNull(tetrisState.height);
        assertNotNull(tetrisState.width);
    }
}
