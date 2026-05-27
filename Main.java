package com.worldtrade;

import com.worldtrade.data.TradeDataLoader;
import com.worldtrade.display.ConsoleRenderer;
import com.worldtrade.engine.ShockSimulator;
import com.worldtrade.engine.SimulationReport;
import com.worldtrade.engine.TradeNetwork;
import com.worldtrade.export.ReportExporter;
import com.worldtrade.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final ConsoleRenderer renderer = new ConsoleRenderer();
    private static final ShockSimulator simulator = new ShockSimulator();
    private static TradeNetwork network;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        network = TradeDataLoader.loadGlobalTradeNetwork();
        
        // --batch bayrağı yoxlanılır
        if (Arrays.asList(args).contains("--batch")) {
            runBatchMode();
            return;
        }

        renderer.printBanner();
        renderer.printSuccess("Loaded " + network.getAllCountries().size() + " economies and " + network.getAllFlows().size() + " bilateral trade flows.");

        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> runCustomShock();
                case "2" -> runPresetScenarios();
                case "3" -> renderer.printCountryList(network);
                case "4" -> viewCountryProfile();
                case "5" -> renderer.printShockTypeList();
                case "6" -> runComparison();
                case "0" -> running = false;
                default  -> renderer.printError("Invalid choice. Enter a number from the menu.");
            }
        }
        renderer.println("\n Goodbye. Stay globally informed.\n");
    }

    // ─── BATCH MODE ──────────────────────────────────────────────────────────────
    private static void runBatchMode() {
        renderer.printBanner();
        renderer.println("\033[1;33m══════════════════ BATCH MODE ══════════════════\033[0m");
        renderer.println(" Running all 7 preset scenarios and exporting reports.");
        renderer.println(" No interaction required — sit back and let it run.\n");
        renderer.printSuccess("Loaded " + network.getAllCountries().size() + " economies and " + network.getAllFlows().size() + " bilateral trade flows.");
        renderer.println();

        List<TradeShock> presets = allPresets();
        List<BatchResult> results = new ArrayList<>();

        for (int i = 0; i < presets.size(); i++) {
            TradeShock shock = presets.get(i);
            String label = String.format(" [%d/%d] %s: %s → %s (%.0f%%)", 
                i + 1, presets.size(), 
                shock.getType().getDisplayName(), 
                shock.getInitiatorCode(), 
                shock.getTargetCode(), 
                shock.getIntensity() * 100);
            
            System.out.print("\033[1m" + label + "\033[0m … ");
            
            SimulationReport report = simulator.simulate(network, shock);
            
            renderer.startCapture();
            renderer.printSimulationReport(report);
            String captured = renderer.stopCapture();
            
            String filename = ReportExporter.buildSingleFilename(shock);
            String savedPath = null;
            String error = null;
            
            try {
                savedPath = ReportExporter.save(captured, filename);
            } catch (IOException e) {
                error = e.getMessage();
            }
            
            if (error == null) {
                System.out.println("\033[32msaved\033[0m");
            } else {
                System.out.println("\033[31mFAILED: " + error + "\033[0m");
            }
            results.add(new BatchResult(shock, report, filename + ".txt", savedPath, error));
        }
        printBatchSummary(results);
    }

    private static List<TradeShock> allPresets() {
        List<TradeShock> list = new ArrayList<>();
        for (String i : new String[]{"1","2","3","4","5","6","7"}) {
            TradeShock t = presetFromChoice(i);
            if (t != null) list.add(t);
        }
        return list;
    }

    private static void printBatchSummary(List<BatchResult> results) {
        renderer.println("\n");
        renderer.println("\033[1;97m╔══════════════════════════════════════════════════════════════════════════════╗");
        renderer.println(" ║                         BATCH RUN — SUMMARY TABLE                            ║");
        renderer.println("╚══════════════════════════════════════════════════════════════════════════════╝\033[0m");
        renderer.println();
        renderer.println(String.format(" %-3s %-28s %-8s %-14s %-14s %-8s", "#", "SCENARIO", "INT.", "GDP AT RISK", "TRADE @ RISK", "HIGH RISK"));
        renderer.println("\033[2m " + "─".repeat(80) + "\033[0m");
        
        for (int i = 0; i < results.size(); i++) {
            BatchResult br = results.get(i);
            SimulationReport r = br.report;
            String intStr = String.format("%.0f%%", br.shock.getIntensity() * 100);
            String gdpStr = String.format("$%.0fB", r.getTotalGdpAtRisk());
            String tradeStr = String.format("$%.0fB", r.getTotalTradeAtRisk());
            String highStr = r.getCountriesAtHighRisk() + " nations";
            String label = br.shock.getType().getDisplayName() + " (" + br.shock.getInitiatorCode() + "→" + br.shock.getTargetCode() + ")";
            
            renderer.println(String.format(" %-3d %-28s %-8s %-14s %-14s %-8s", i + 1, truncate(label, 28), intStr, gdpStr, tradeStr, highStr));
        }
        
        renderer.println("\033[2m " + "─".repeat(80) + "\033[0m");
        double totalGdp = results.stream().mapToDouble(r -> r.report.getTotalGdpAtRisk()).sum();
        double totalTrade = results.stream().mapToDouble(r -> r.report.getTotalTradeAtRisk()).sum();
        
        renderer.println(String.format(" %-3s %-28s %-8s %-14s %-14s", "", "TOTALS (across all 7 scenarios)", "", String.format("\033[1;31m$%.0fB\033[0m", totalGdp), String.format("\033[1;33m$%.0fB\033[0m", totalTrade)));
        renderer.println();
        renderer.println("\033[1m Hardest-hit scenario:\033[0m " + hardest(results));
        renderer.println();
        renderer.println("\033[1m Exported files:\033[0m");
        
        for (BatchResult br : results) {
            if (br.savedPath != null) {
                renderer.println("\033[32m ✓\033[0m " + br.filename);
            } else {
                renderer.println("\033[31m ✗\033[0m " + br.filename + " (error: " + br.error + ")");
            }
        }
        
        long saved = results.stream().filter(r -> r.savedPath != null).count();
        long failed = results.size() - saved;
        renderer.println();
        if (failed == 0) {
            renderer.printSuccess(saved + "/" + results.size() + " reports saved to reports/");
        } else {
            renderer.printError(failed + " report(s) failed to save.");
        }
        renderer.println();
    }

    private static String hardest(List<BatchResult> results) {
        return results.stream()
                .max((a, b) -> Double.compare(a.report.getTotalGdpAtRisk(), b.report.getTotalGdpAtRisk()))
                .map(br -> "\033[1m" + br.shock.getType().getDisplayName() + " (" + br.shock.getInitiatorCode() + " → " + br.shock.getTargetCode() + ")\033[0m" + String.format(" — $%.0fB GDP at risk", br.report.getTotalGdpAtRisk()))
                .orElse("N/A");
    }

    private record BatchResult(TradeShock shock, SimulationReport report, String filename, String savedPath, String error) {}

    // ─── MAIN MENU ───────────────────────────────────────────────────────────────
    private static void printMainMenu() {
        renderer.println("\n");
        renderer.println("\033[1m ═══════════════ MAIN MENU ═══════════════\033[0m");
        renderer.println(" 1. Run a Custom Trade Shock Simulation");
        renderer.println(" 2. Run a Preset Scenario");
        renderer.println(" 3. View All Countries & Economies");
        renderer.println(" 4. View Country Trade Profile");
        renderer.println(" 5. View Shock Type Catalogue");
        renderer.println(" 6. Historical Comparison — Side-by-Side Shock Diff");
        renderer.println(" 0. Exit");
        renderer.printPrompt("Your choice:");
    }

    // ─── 1. CUSTOM SHOCK ────────────────────────────────────────────────────────
    private static void runCustomShock() {
        renderer.println("\n");
        renderer.printSectionHeader("Custom Shock Configuration");
        TradeShock shock = buildCustomShock();
        if (shock == null) return;
        
        renderer.println("\n\033[1m Simulating...\033[0m");
        SimulationReport report = simulator.simulate(network, shock);
        renderer.startCapture();
        renderer.printSimulationReport(report);
        offerSingleExport(renderer.stopCapture(), shock);
    }

    private static TradeShock buildCustomShock() {
        renderer.printShockTypeList();
        ShockType[] types = ShockType.values();
        renderer.printPrompt("Select shock type (1–" + types.length + "):");
        int typeIdx = readInt(1, types.length) - 1;
        ShockType shockType = types[typeIdx];
        
        renderer.printCountryList(network);
        renderer.printPrompt("Enter INITIATOR country code (e.g. USA):");
        String initiatorCode = scanner.nextLine().trim().toUpperCase();
        if (!network.hasCountry(initiatorCode)) {
            renderer.printError("Unknown country code: " + initiatorCode);
            return null;
        }
        
        renderer.printPrompt("Enter TARGET country code (e.g. CHN):");
        String targetCode = scanner.nextLine().trim().toUpperCase();
        if (!network.hasCountry(targetCode)) {
            renderer.printError("Unknown country code: " + targetCode);
            return null;
        }
        
        if (initiatorCode.equals(targetCode)) {
            renderer.printError("Initiator and target must be different countries.");
            return null;
        }
        
        renderer.printPrompt("Shock intensity 1–100% (e.g. 75 for severe):");
        int intensityPct = readInt(1, 100);
        double intensity = intensityPct / 100.0;
        
        renderer.printPrompt("Brief description (or press Enter for default):");
        String desc = scanner.nextLine().trim();
        if (desc.isEmpty()) {
            desc = shockType.getDisplayName() + " by " + initiatorCode + " against " + targetCode;
        }
        return new TradeShock(shockType, initiatorCode, targetCode, intensity, desc);
    }

    // ─── 2. PRESET SCENARIOS ────────────────────────────────────────────────────
    private static void runPresetScenarios() {
        renderer.println("\n");
        TradeShock shock = selectPreset();
        if (shock != null) {
            renderer.println("\n\033[1m Running preset scenario...\033[0m");
            SimulationReport report = simulator.simulate(network, shock);
            renderer.startCapture();
            renderer.printSimulationReport(report);
            offerSingleExport(renderer.stopCapture(), shock);
        }
    }

    private static TradeShock selectPreset() {
        renderer.printSectionHeader("Preset Scenarios");
        printPresetMenu();
        renderer.printPrompt("Select scenario:");
        String choice = scanner.nextLine().trim();
        return presetFromChoice(choice);
    }

    private static void printPresetMenu() {
        renderer.println(" 1. US–China Trade War (Full Escalation)");
        renderer.println(" 2. Russia Energy Sanctions (EU)");
        renderer.println(" 3. Taiwan Semiconductor Export Ban (China-Initiated)");
        renderer.println(" 4. Saudi Arabia Oil Export Embargo (Global)");
        renderer.println(" 5. India–China Economic Decoupling");
        renderer.println(" 6. South Korea Technology Export Controls (vs China)");
        renderer.println(" 7. Brazil Soy & Iron Ore Supply Disruption (to China)");
        renderer.println(" 0. Back to Main Menu");
    }

    private static TradeShock presetFromChoice(String choice) {
        return switch (choice) {
            case "1" -> new TradeShock(ShockType.TRADE_WAR, "USA", "CHN", 0.90, "Full-scale US–China trade war with sweeping tariffs and retaliatory bans");
            case "2" -> new TradeShock(ShockType.ECONOMIC_SANCTIONS, "DEU", "RUS", 0.85, "EU-wide sanctions on Russian energy exports following geopolitical crisis");
            case "3" -> new TradeShock(ShockType.TECH_EXPORT_CONTROL, "CHN", "TWN", 0.80, "China blocks semiconductor and advanced tech exports to/from Taiwan");
            case "4" -> new TradeShock(ShockType.EXPORT_BAN, "SAU", "JPN", 0.75, "Saudi Arabia suspends crude oil exports to East Asia amid OPEC+ dispute");
            case "5" -> new TradeShock(ShockType.ECONOMIC_SANCTIONS, "IND", "CHN", 0.70, "India implements broad economic decoupling measures against Chinese imports");
            case "6" -> new TradeShock(ShockType.TECH_EXPORT_CONTROL, "KOR", "CHN", 0.65, "South Korea aligns with US chip controls, restricting semiconductor exports to China");
            case "7" -> new TradeShock(ShockType.SUPPLY_CHAIN_DISRUPTION, "BRA", "CHN", 0.78, "Severe drought and logistics crisis disrupts Brazil's commodity exports to China");
            case "0" -> null;
            default  -> { renderer.printError("Invalid choice."); yield null; }
        };
    }

    // ─── 4. COUNTRY PROFILE ─────────────────────────────────────────────────────
    private static void viewCountryProfile() {
        renderer.printCountryList(network);
        renderer.printPrompt("Enter country code to inspect (e.g. DEU):");
        String code = scanner.nextLine().trim().toUpperCase();
        if (!network.hasCountry(code)) {
            renderer.printError("Unknown country code: " + code);
            return;
        }
        renderer.printTradeRelationships(network, code);
    }

    // ─── 6. HISTORICAL COMPARISON ───────────────────────────────────────────────
    private static void runComparison() {
        renderer.println("\n");
        renderer.println("\033[1m╔══════════════════════════════════════════════════════════════════════════════╗");
        renderer.println(" ║               HISTORICAL COMPARISON — SIDE-BY-SIDE SHOCK DIFF               ║");
        renderer.println("╚══════════════════════════════════════════════════════════════════════════════╝\033[0m");
        renderer.println("\033[2m Run two independent shock scenarios through the full contagion engine,");
        renderer.println(" then compare their country-level GDP impacts side by side.\033[0m\n");
        
        renderer.println("\033[1;36m ┌─ SHOCK A ─────────────────────────────────────────────────────────┐\033[0m");
        TradeShock shockA = pickShockForComparison("A");
        if (shockA == null) { renderer.printError("Comparison cancelled."); return; }
        
        renderer.println("\033[1;35m ┌─ SHOCK B ─────────────────────────────────────────────────────────┐\033[0m");
        TradeShock shockB = pickShockForComparison("B");
        if (shockB == null) { renderer.printError("Comparison cancelled."); return; }
        
        renderer.println("\n\033[1m Simulating Shock A...\033[0m");
        SimulationReport reportA = simulator.simulate(network, shockA);
        
        renderer.println("\033[1m Simulating Shock B...\033[0m");
        SimulationReport reportB = simulator.simulate(network, shockB);
        
        renderer.startCapture();
        renderer.printComparisonReport(reportA, reportB);
        offerComparisonExport(renderer.stopCapture(), shockA, shockB);
    }

    private static TradeShock pickShockForComparison(String label) {
        renderer.println(" How would you like to define Shock " + label + "?");
        renderer.println(" P — Choose from preset scenarios");
        renderer.println(" C — Configure a custom shock");
        renderer.println(" 0 — Cancel");
        renderer.printPrompt("Choice [P / C / 0]:");
        String mode = scanner.nextLine().trim().toUpperCase();
        
        return switch (mode) {
            case "P" -> {
                renderer.println("\n");
                renderer.printSectionHeader("Preset Scenarios — Shock " + label);
                printPresetMenu();
                renderer.printPrompt("Select scenario:");
                String choice = scanner.nextLine().trim();
                TradeShock t = presetFromChoice(choice);
                if (t == null) renderer.printError("No preset selected.");
                yield t;
            }
            case "C" -> {
                renderer.println("\n");
                renderer.printSectionHeader("Custom Shock — Shock " + label);
                yield buildCustomShock();
            }
            case "0" -> null;
            default  -> { renderer.printError("Invalid option."); yield null; }
        };
    }

    // ─── EXPORT HELPERS ──────────────────────────────────────────────────────────
    private static void offerSingleExport(String captured, TradeShock shock) {
        renderer.printPrompt("Save this report to file? [Y / N]:");
        String ans = scanner.nextLine().trim().toUpperCase();
        if (!ans.equals("Y")) return;
        
        String filename = ReportExporter.buildSingleFilename(shock);
        try {
            String path = ReportExporter.save(captured, filename);
            renderer.printSuccess("Report saved → " + path);
        } catch (IOException e) {
            renderer.printError("Could not save file: " + e.getMessage());
        }
    }

    private static void offerComparisonExport(String captured, TradeShock shockA, TradeShock shockB) {
        renderer.printPrompt("Save this comparison report to file? [Y / N]:");
        String ans = scanner.nextLine().trim().toUpperCase();
        if (!ans.equals("Y")) return;
        
        String filename = ReportExporter.buildComparisonFilename(shockA, shockB);
        try {
            String path = ReportExporter.save(captured, filename);
            renderer.printSuccess("Comparison report saved → " + path);
        } catch (IOException e) {
            renderer.printError("Could not save file: " + e.getMessage());
        }
    }

    // ─── GENERAL HELPERS ─────────────────────────────────────────────────────────
    private static int readInt(int min, int max) {
        while (true) {
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val >= min && val <= max) return val;
                renderer.printError("Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                renderer.printError("Invalid number. Try again.");
            }
            renderer.printPrompt("Enter value (" + min + "–" + max + "):");
        }
    }

    private static String truncate(String s, int max) {
        return s.length() > max ? s.substring(0, max - 1) + "…" : s;
    }
}
