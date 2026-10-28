package com.example.rtsgame.statemachine;

import com.example.rtsgame.Damageable;
import com.example.rtsgame.units.AnimationType;
import com.example.rtsgame.units.AttackUnit;

public class AttackState extends State<AttackUnit> {

    private double cooldownTimer;

    @Override
    public void enter(AttackUnit unit) {
        System.out.println("AttackState: enter");
        unit.setCurrentAnimationLoop(AnimationType.ATTACK);
        cooldownTimer = unit.getAttackCooldown();
    }

    @Override
    public State<AttackUnit> update(AttackUnit unit, double deltaTime) {

        State<AttackUnit> next = transition(unit);
        if (next != null) {
            return next;
        }

        Damageable target = unit.getTarget();

        if (target == null || target.isDestroyed()) {
            unit.clearTarget();
        }
        if (target != null && !target.isDestroyed()) {
            unit.facePosition(target.getCenterX());

            cooldownTimer += deltaTime;
            if (cooldownTimer >= unit.getAttackCooldown()) {
                unit.performAttack(target);
                cooldownTimer = 0;
            }
        }
        return this;
    }

    @Override
    public void exit(AttackUnit unit) {
        System.out.println("AttackState: exit");
    }
}