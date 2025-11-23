package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;
import java.util.Objects;
import ru.mipt.bit.platformer.game.level.LevelModel;
import ru.mipt.bit.platformer.game.model.BulletModel;
import ru.mipt.bit.platformer.game.model.TankModel;
import ru.mipt.bit.platformer.util.Direction;

public class ShootTankCommand implements ITankCommand {

    private final LevelModel levelModel;
    private final TankModel shooter;

    public ShootTankCommand(LevelModel levelModel, TankModel shooter) {
        this.levelModel = Objects.requireNonNull(levelModel);
        this.shooter = Objects.requireNonNull(shooter);
    }

    @Override
    public void execute() {
        Direction direction = shooter.getDirection();
        GridPoint2 spawn = direction.destinationFrom(shooter.getCoordinates(), new GridPoint2());
        if (!levelModel.isInsideBounds(spawn)) {
            return;
        }
        if (levelModel.findTree(spawn) != null) {
            return;
        }
        TankModel tankAtSpawn = levelModel.findTank(spawn);
        if (tankAtSpawn != null && tankAtSpawn != shooter) {
            return;
        }
        BulletModel bullet = new BulletModel(levelModel, shooter, spawn, direction);
        levelModel.addBullet(bullet);
    }
}
