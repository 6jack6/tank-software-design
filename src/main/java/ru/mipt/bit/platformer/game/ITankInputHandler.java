package ru.mipt.bit.platformer.game;

public interface ITankInputHandler {

    void handleInput(Iterable<? extends ITreeModel> obstacles);

    void registerAction(TankInputAction action);

    interface TankInputAction {
        boolean isPressed();

        void execute(ITankModel tank, Iterable<? extends ITreeModel> obstacles);
    }
}
