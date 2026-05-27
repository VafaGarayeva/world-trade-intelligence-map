package com.worldtrade;

import com.worldtrade.data.TradeDataLoader;
import com.worldtrade.display.ConsoleRenderer;
import com.worldtrade.engine.ShockSimulator;
import com.worldtrade.engine.SimulationReport;
import com.worldtrade.engine.TradeNetwork;
import com.worldtrade.export.ReportExporter;
import com.worldtrade.model.*;

import java.io.IOException;
import java.util.*;

public class Main {

    private static final ConsoleRenderer renderer = new ConsoleRenderer();
    private static final ShockSimulator simulator = new ShockSimulator();
    private static TradeNetwork network;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        network = TradeDataLoader.loadGlobalTradeNetwork();

        if (Arrays.asList(args).contains("--batch")) {
            runBatchMode();
            return;
        }

        renderer.printBanner();
        renderer.printSuccess("Loaded " + network.getAllCountries().size() + " economies.");

        boolean running = true;

        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> runCustomShock();
                case "2" -> runPreset();
                case "3" -> renderer.printCountryList(network);
                case "6" -> runComparison();
                case "0" -> running = false;
                default -> renderer.printError("Invalid choice");
            }
        }
    }

    // ================= CUSTOM SHOCK =================
    private static void runCustomShock() {
        TradeShock shock = buildShock();
        if (shock == null) return;

        SimulationReport report = simulator.simulate(network, shock);
        renderer.startCapture();
        renderer.printSimulationReport(report);
        String out = renderer.stopCapture();

        save(out, shock.getType().name());
    }

    // ================= PRESET =================
    private static void runPreset() {
        TradeShock shock = new TradeShock(
                ShockType.TRADE_WAR,
                "USA",
                "CHN",
                0.9,
                "US-China Trade War"
        );

        SimulationReport report = simulator.simulate(network, shock);
        renderer.startCapture();
        renderer.printSimulationReport(report);
        String out = renderer.stopCapture();

        save(out, "preset");
    }

    // ================= COMPARISON =================
    private static void runComparison() {

        TradeShock a = new TradeShock(ShockType.TRADE_WAR, "USA", "CHN", 0.9, "A");
        TradeShock b = new TradeShock(ShockType.ECONOMIC_SANCTIONS, "DEU", "RUS", 0.85, "B");

        SimulationReport r1 = simulator.simulate(network, a);
        SimulationReport r2 = simulator.simulate(network, b);

        renderer.startCapture();
        renderer.printComparisonReport(r1, r2);
        String out = renderer.stopCapture();

        save(out, "comparison");
    }

    // ================= SHOCK BUILDER =================
    private static TradeShock buildShock() {

        return new TradeShock(
                ShockType.TRADE_WAR,
                "USA",
                "CHN",
                0.8,
                "Custom Shock"
        );
    }

    // ================= SAVE =================
    private static void save(String content, String name) {
        try {
            String file = ReportExporter.save(content, name);
            renderer.printSuccess("Saved: " + file);
        } catch (IOException e) {
            renderer.printError("Save failed");
        }
    }

    // ================= MENU =================
    private static void printMainMenu() {
        System.out.println("\n1.Custom  2.Preset  3.Countries  6.Compare  0.Exit");
        System.out.print("Choice: ");
    }

    // ================= BATCH MODE =================
    private static void runBatchMode() {

        List<TradeShock> shocks = List.of(
                new TradeShock(ShockType.TRADE_WAR, "USA", "CHN", 0.9, "S1"),
                new TradeShock(ShockType.ECONOMIC_SANCTIONS, "DEU", "RUS", 0.85, "S2"),
                new TradeShock(ShockType.TECH_EXPORT_CONTROL, "CHN", "TWN", 0.8, "S3")
        );

        for (TradeShock s : shocks) {
            SimulationReport r = simulator.simulate(network, s);
            renderer.startCapture();
            renderer.printSimulationReport(r);
            save(renderer.stopCapture(), s.getType().name());
        }

        System.out.println("Batch complete");
    }
}
