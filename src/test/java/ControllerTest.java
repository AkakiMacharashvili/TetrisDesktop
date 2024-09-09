import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;

public class ControllerTest {
    Controller controller;

    @BeforeEach
    public void setUp(){
        controller = new Controller(new TetrisModel(TetrisModel.DEFAULT_WIDTH, TetrisModel.DEFAULT_HEIGHT, TetrisModel.DEFAULT_COLORS_NUMBER),
                                    new View( new JPanel(), new TetrisModel(TetrisModel.DEFAULT_WIDTH, TetrisModel.DEFAULT_HEIGHT, TetrisModel.DEFAULT_COLORS_NUMBER)),
                                    new JPanel());
    }

    @Test
    public void initTest(){
        assertNotNull(controller.model);
        assertNotNull(controller.view);
        assertNotNull(controller.gamePanel);
    }

}
