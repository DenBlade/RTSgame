package com.example.rtsgame.units;

import com.example.rtsgame.Config;
import com.example.rtsgame.Damageable;
import com.example.rtsgame.map.MapManager;
import com.example.rtsgame.statemachine.*;
import com.example.rtsgame.units.pathfinding.Pathfinding;

public class AttackUnit extends Unit {
    private Damageable target;
    private int attackDamage;
    private double attackRange;
    private double attackCooldown;
    public AttackUnit(String fileName, AnimationData[] animationData, MapManager mapManager, int health, double speed, double x, double y, boolean ownByAI, int spriteWidth, int spriteHeight, double scale, int attackDamage, double attackRange, double attackCooldown) {
        super(fileName, animationData, mapManager, health, speed, x, y, ownByAI, spriteWidth, spriteHeight, scale);
        this.attackDamage = attackDamage;
        this.attackRange = attackRange;
        this.attackCooldown = attackCooldown;
    }
    public static AttackUnit createSwordsman(MapManager mapManager, boolean ownByAI, double x, double y) {
        return new AttackUnit("/units/swordman/MiniSwordMan.png", new AnimationData[]{Config.SWORDSMAN_IDLE_ANIM, Config.SWORDSMAN_WALK_ANIM, Config.SWORDSMAN_DEATH_ANIM, Config.SWORDSMAN_ATTACK_ANIM}, mapManager, Config.SWORDSMAN_MAX_HEALTH, Config.SWORDSMAN_MOVEMENT_SPEED, x, y, ownByAI, 32, 32, 1.5, Config.SWORDSMAN_ATTACK_DAMAGE, Config.SWORDSMAN_ATTACK_RANGE, Config.SWORDSMAN_ATTACK_COOLDOWN);
    }
    @Override
    protected void setupStateMachine() { // <--- ДОБАВЛЕНО: настройка FSM для AttackUnit
        State<AttackUnit> idleState = new IdleState<>();
        State<AttackUnit> movingState = new MovingState<>();
        State<AttackUnit> attackState = new AttackState();
        State<AttackUnit> deadState = new DeadState<>();

        stateMachine = new StateMachine<>(this, idleState);

        Transition<AttackUnit> dead = new Transition<>((unit) -> unit.health <= 0, deadState);

        Transition<AttackUnit> movingFromIdle = new Transition<>(Unit::hasPath, movingState);
        Transition<AttackUnit> attackFromIdle = new Transition<>((unit) -> unit.hasTarget() && unit.isTargetInAttackRange(), attackState);
        idleState.addTransition(dead);
        idleState.addTransition(attackFromIdle);
        idleState.addTransition(movingFromIdle);

        Transition<AttackUnit> attackFromMoving = new Transition<>((unit) -> unit.hasTarget() && unit.isTargetInAttackRange(), attackState);
        Transition<AttackUnit> idleFromMoving = new Transition<>(Unit::isFinishedMoving, idleState);
        movingState.addTransition(dead);
        movingState.addTransition(attackFromMoving);
        movingState.addTransition(idleFromMoving);

        Transition<AttackUnit> idleFromAttack = new Transition<>((unit) -> !unit.hasTarget() || unit.getTarget().isDestroyed(), idleState);
        Transition<AttackUnit> movingFromAttack = new Transition<>((unit) -> unit.hasPath() || (unit.hasTarget() && !unit.isTargetInAttackRange()), movingState);
        attackState.addTransition(dead);
        attackState.addTransition(idleFromAttack);
        attackState.addTransition(movingFromAttack);
    }

    @Override
    public void update(double delta) {
        if (hasTarget() && !isTargetInAttackRange() && !hasPath()) {
            super.setupPath(target.getCenterX(), target.getCenterY());
        }
        super.update(delta);
    }

    @Override
    public boolean setupPath(double targetX, double targetY) {
        clearTarget();
        return super.setupPath(targetX, targetY);
    }

    public void attackTarget(Damageable target) {
        if (target == null || target == this) return;
        if (this.ownByAI != target.ownByAi()) {
            this.target = target;
            if (!isTargetInAttackRange()) {
                super.setupPath(target.getCenterX(), target.getCenterY());
            } else {
                path.clear();
            }
        }
    }

    public void performAttack(Damageable target) {
        if (target != null && !target.isDestroyed()) {
            target.takeDamage(attackDamage);
        }
    }

    public boolean isTargetInAttackRange() {
        if (target == null || target.isDestroyed()) return false;
        return target.getDistanceTo(getCenterX(), getBottomY()) <= attackRange;
    }

    public Damageable getTarget() { return target; }
    public boolean hasTarget() { return target != null && !target.isDestroyed(); }
    public void clearTarget() { this.target = null; }
    public double getAttackCooldown() { return attackCooldown; }
}
