import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameOverTest {

    GameOver gameOver;

    @BeforeEach
    public void init(){
        gameOver = new GameOver(new TetrisModel(), new JButton(), new JButton());
    }

    @Test
    public void setUpTest(){
        assertNotNull(gameOver.getQuitButton());
        assertNotNull(gameOver.getRefreshButton());
        assertFalse(gameOver.getRefreshButton().isVisible());
        assertFalse(gameOver.getQuitButton().isVisible());
    }

    @Test
    public void testOver(){
        gameOver.over(new TetrisModel());
        assertTrue(gameOver.getRefreshButton().isVisible());
        assertTrue(gameOver.getQuitButton().isVisible());
    }
}
