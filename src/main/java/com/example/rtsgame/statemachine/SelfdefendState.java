package com.example.rtsgame.statemachine;

import com.example.rtsgame.Damageable;
import com.example.rtsgame.units.AnimationType;
import com.example.rtsgame.units.AttackUnit;

public class SelfdefendState extends State<AttackUnit>{
    private double cooldownTimer;

    @Override
    void enter(AttackUnit unit) {
        System.out.println("SelfdefendState: enter");
        unit.setCurrentAnimationLoop(AnimationType.ATTACK);
        cooldownTimer = unit.getAttackCooldown();
    }

    @Override
    State<AttackUnit> update(AttackUnit unit, double deltaTime) {
        State<AttackUnit> next = transition(unit);
        if (next != null) {
            return next;
        }

        Damageable target = unit.getRetaliationTarget();

        if (target == null || target.isDestroyed() || !unit.isRetaliationTargetInRange()) {
            unit.clearRetaliationTarget();
            return this;
        }

        unit.facePosition(target.getCenterX());

        cooldownTimer += deltaTime;
        if (cooldownTimer >= unit.getAttackCooldown()) {
            unit.performAttack(target);
            cooldownTimer -= unit.getAttackCooldown();
        }

        return this;
    }

    @Override
    void exit(AttackUnit unit) {
        System.out.println("SelfdefendState: exit");
        unit.clearRetaliationTarget();
    }
}
