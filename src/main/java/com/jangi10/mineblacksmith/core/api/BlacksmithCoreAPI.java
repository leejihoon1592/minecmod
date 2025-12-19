package com.jangi10.mineblacksmith.core.api;

import com.jangi10.mineblacksmith.core.data.*;
import com.jangi10.mineblacksmith.core.logic.*;
import com.jangi10.mineblacksmith.core.registry.MaterialRegistry;

import java.util.List;
import java.util.Optional;

public class BlacksmithCoreAPI {

    // --- 노 상태 요약(UI/디버그용) ---
    public static FurnaceStatus getFurnaceStatus(FurnaceSession session, double tMax) {
        return FurnaceStatusLogic.build(session, tMax);
    }


    // --- 온도 × 제작 난이도(코어) ---
    public static TemperatureColor getTemperatureColor(double currentTemp, double tMax) {
        return TemperatureLogic.getColor(currentTemp, tMax);
    }

    public static TemperatureWindowResult getForgeTemperatureWindow(double currentTemp, double baseHeatTemp, int difficulty) {
        return TemperatureLogic.getForgeWindow(currentTemp, baseHeatTemp, difficulty);
    }

    public static double getNaturalHoldTimeSeconds(double baseHeatTemp, int difficulty) {
        return TemperatureLogic.getNaturalHoldTimeSeconds(baseHeatTemp, difficulty);
    }
// ==========================================
    // 2.5 노(Furnace) 세션 (Refining/Furnace Core)
    // ==========================================

    /** 노 1틱 처리(온도+정제 진행) */
    public static void tickFurnace(FurnaceSession session) {
        FurnaceLogic.tick(session);
    }

    /** 풀무 펄스(우클릭) */
    public static void pulseBellows(FurnaceSession session) {
        if (session != null) session.setBellowsPulse(true);
    }

    /** 주괴틀 추출 */
    public static IngotExtractionResult extractIngot(FurnaceSession session) {
        return FurnaceLogic.extractIngot(session);
    }

    // --- 전투 : 결과 검증(디버그/개발용) ---
    public static void validateCombatSnapshot(CombatSnapshot snapshot) {
        CombatSanityChecker.validate(snapshot);
    }

    // --- 전투 : 스냅샷 변환 ---
    public static CombatSnapshot snapshotFromWeaponResult(
            double baseDamage,
            WeaponCombatResult result
    ) {
        return CombatSnapshotMapper.fromWeaponResult(baseDamage, result);
    }

    public static CombatSnapshot snapshotFromRangedResult(
            double baseDamage,
            RangedCombatResult result
    ) {
        return CombatSnapshotMapper.fromRangedResult(baseDamage, result);
    }

    // --- 방어구 : 디버그 요약 ---
    public static String debugArmorSummary(
            List<ForgingStats> plates,
            List<ForgingStats> chains,
            List<ForgingStats> linings,
            int usageCountForProtection
    ) {
        return ArmorDebugHelper.summarize(plates, chains, linings, usageCountForProtection);
    }

    // --- 방어구 : 내구 파이프라인 ---
    public static int calculateArmorPlateWear(
            WeaponCombatResult result,
            List<ForgingStats> plates
    ) {
        return ArmorDurabilityPipeline.calculatePlateWear(result, plates);
    }

    public static int calculateArmorChainWear(
            WeaponCombatResult result,
            List<ForgingStats> chains
    ) {
        return ArmorDurabilityPipeline.calculateChainWear(result, chains);
    }

    public static int calculateArmorLiningWear(
            List<ForgingStats> linings
    ) {
        return ArmorDurabilityPipeline.calculateLiningWear(linings);
    }

    // --- 방어구 : 방어 총합 파이프라인 ---
    public static double calculateArmorTotalStrength(
            List<ForgingStats> plates,
            List<ForgingStats> chains,
            List<ForgingStats> linings
    ) {
        return ArmorPipeline.calculateTotalStrength(plates, chains, linings);
    }

    public static double calculateArmorTotalElasticity(
            List<ForgingStats> plates,
            List<ForgingStats> chains,
            List<ForgingStats> linings
    ) {
        return ArmorPipeline.calculateTotalElasticity(plates, chains, linings);
    }

    // --- 원거리(활) : 디버그 요약 ---
    public static String debugRangedSummary(
            double baseDamage,
            double baseCritPercent,
            ForgingStats limbStats,
            ForgingStats stringStats,
            double targetStrength,
            double targetElasticity,
            int targetProtection
    ) {
        return RangedDebugHelper.summarize(
                baseDamage,
                baseCritPercent,
                limbStats,
                stringStats,
                targetStrength,
                targetElasticity,
                targetProtection
        );
    }

    // --- 원거리(활) : 내구 파이프라인 ---
    public static int calculateRangedLimbWearFromResult(
            RangedCombatResult result,
            ForgingStats limbStats
    ) {
        return RangedDurabilityPipeline.calcLimbWear(result, limbStats);
    }

    public static int calculateRangedStringWearFromResult(
            RangedCombatResult result,
            ForgingStats stringStats
    ) {
        return RangedDurabilityPipeline.calcStringWear(result, stringStats);
    }

    // --- 원거리(활) : 원샷 파이프라인(치명타 굴림 + 결과 산출) ---
    public static RangedCombatResult runRangedPipeline(
            double baseDamage,
            double baseCritPercent,
            ForgingStats limbStats,
            ForgingStats stringStats,
            double targetTotalStrength,
            double targetTotalElasticity,
            int targetProtection
    ) {
        return RangedPipeline.run(
                baseDamage,
                baseCritPercent,
                limbStats,
                stringStats,
                targetTotalStrength,
                targetTotalElasticity,
                targetProtection
        );
    }

    // --- 원거리(활) : 활대/활시위 스탯 합산 ---
    public static ForgingStats sumRangedLimbs(List<ForgingStats> limbParts) {
        return RangedAssemblyLogic.sumLimbs(limbParts);
    }

    public static ForgingStats sumRangedStrings(List<ForgingStats> stringParts) {
        return RangedAssemblyLogic.sumStrings(stringParts);
    }

    // --- 원거리(활) : 부위별 내구 소모 ---
    public static int calculateRangedLimbWear(
            double projectilePowerMultiplier,
            ForgingStats limbStats
    ) {
        return RangedDurabilityLogic.calculateLimbWear(projectilePowerMultiplier, limbStats);
    }

    // --- 원거리(활) : 활시위 내구 소모 ---
    public static int calculateRangedStringWear(
            double chargeSpeedMultiplier,
            boolean critical,
            ForgingStats stringStats
    ) {
        return RangedDurabilityLogic.calculateStringWear(chargeSpeedMultiplier, critical, stringStats);
    }

    // --- 원거리(활) : 결과 분리형(발사력/차징속도) ---
    public static RangedCombatResult calculateRangedCombatResult(
            double baseDamage,
            ForgingStats limbStats,
            ForgingStats stringStats,
            double totalStrength,
            double totalElasticity,
            int protection,
            boolean critical
    ) {
        return CombatLogic.calculateRangedCombatResult(
                baseDamage,
                limbStats,
                stringStats,
                totalStrength,
                totalElasticity,
                protection,
                critical
        );
    }

    public static double calculateRangedCriticalChance(
            double baseCritPercent,
            ForgingStats limbStats,
            ForgingStats stringStats
    ) {
        double bonus = RangedLogic.getRangedCritBonusPercent(limbStats, stringStats);
        double out = baseCritPercent + bonus;
        if (out < 0) out = 0;
        if (out > 100) out = 100;
        return out;
    }

    public static WeaponCombatResult calculateRangedCombat(
            double baseDamage,
            ForgingStats limbStats,
            ForgingStats stringStats,
            double totalStrength,
            double totalElasticity,
            int protection,
            double currentWeightKg,
            boolean critical
    ) {
        return CombatLogic.calculateRangedCombat(
                baseDamage,
                limbStats,
                stringStats,
                totalStrength,
                totalElasticity,
                protection,
                currentWeightKg,
                critical
        );
    }

    public static WeaponCombatResult calculateCombatWithHandle(
            double baseDamage,
            ForgingStats coreStats,
            ForgingStats handleStats,
            WeaponDamageType damageType,
            double totalStrength,
            double totalElasticity,
            int protection,
            double currentWeightKg,
            boolean critical
    ) {
        return CombatLogic.calculateCombatWithHandle(
                baseDamage,
                coreStats,
                handleStats,
                damageType,
                totalStrength,
                totalElasticity,
                protection,
                currentWeightKg,
                critical
        );
    }

    // --- 치명타 확률 ---
    public static double calculateCriticalChance(double baseCritPercent, ForgingStats stats) {
        return CriticalChanceLogic.calculateChance(baseCritPercent, stats);
    }

    // --- 치명타 ---
    public static boolean rollCritical(double chancePercent) {
        return CriticalLogic.rollCritical(chancePercent);
    }

    // --- 재료 ---
    public static Optional<IngotData> getIngotData(String id) {
        return MaterialRegistry.getMaterial(id);
    }

    public static void registerIngot(IngotData data) {
        MaterialRegistry.register(data);
    }

    // --- 온도 / 정련 ---
    public static double calculateFurnaceTemperature(double currentTemp, FuelData fuel, boolean isBellowsActive) {
        return HeatPhysics.calculateNextTemperature(currentTemp, fuel, isBellowsActive);
    }

    public static RefiningResult processRefining(double currentTemp, ImpurityData impurity, int currentProgress) {
        return RefiningLogic.processTick(currentTemp, impurity, currentProgress);
    }

    // --- 리듬 ---
    public static float getRhythmCursorPosition(long gameTimeMs, int difficulty) {
        return RhythmGameLogic.getCursorPosition(gameTimeMs, difficulty);
    }

    public static RhythmJudgement judgeRhythmHit(float cursorPosition) {
        return RhythmGameLogic.judgeHit(cursorPosition);
    }

    // --- 등급 ---
    public static MaterialGrade determineItemGrade(int difficulty, double finalScore) {
        return GradeDeterminer.determineGrade(difficulty, finalScore);
    }

    // --- 조립 ---
    public static ForgingStats calculateWeaponStats(List<ForgingStats> parts) {
        return AssemblyLogic.sumStats(parts);
    }

    // --- 전투(통합 결과) ---
    public static WeaponCombatResult calculateCombat(
            double baseDamage,
            ForgingStats stats,
            WeaponDamageType damageType,
            double totalStrength,
            double totalElasticity,
            int protection,
            double currentWeightKg,
            boolean critical
    ) {
        return CombatLogic.calculateCombat(
                baseDamage,
                stats,
                damageType,
                totalStrength,
                totalElasticity,
                protection,
                currentWeightKg,
                critical
        );
    }

    private BlacksmithCoreAPI() {}
}
