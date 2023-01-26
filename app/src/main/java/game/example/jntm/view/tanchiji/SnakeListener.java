package game.example.jntm.view.tanchiji;


import java.util.List;


public interface SnakeListener {

    void onSnakeLongAdd(List<Pointer> snake, int size);

    void onSnakeDie(List<Pointer> snake);

}
