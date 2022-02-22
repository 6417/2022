package frc.robot.subsystems.climber;

import java.util.Comparator;
import java.util.Stack;

public class AngleFilter {
    public static class HistoryElement {
        public double velocity;
        public double time;
        public double value;

        public HistoryElement(double val, double vel, double time) {
            this.value = val;
            this.velocity = vel;
            this.time = time;
        }
    }

    int historySize;
    Stack<HistoryElement> history;
    long timeOfConstruction;

    public AngleFilter(int historySize) {
        this.historySize = historySize;
        history = new Stack<>();
        timeOfConstruction = System.currentTimeMillis();
    }

    public void update(double angle) {
        if (history.size() >= historySize) {
            history.remove(history.size() - 1);
        }

        long now = System.currentTimeMillis();
        if (history.size() == 0) {
            history.push(new HistoryElement(angle, angle / (now - timeOfConstruction), now));
        } else {
            HistoryElement previousDataPoint = history.peek();
            history.push(new HistoryElement(angle, (previousDataPoint.value - angle) / (now - previousDataPoint.time), now));
        }
    }

    private double map(double x, double in_min, double in_max, double out_min, double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public double get() {
        if (history.size() == 0)
            return 0.0;

        double maxVelocity = history.stream().map((h) -> h.velocity).max(Double::compareTo).get();
        double historyTimeSpan = history.peek().time - history.get(history.size() - 1).time;
        Stack<HistoryElement> weightedHistory = new Stack<>();
        double summedWeights = 0.0;
        for (int i = 0; i < history.size(); i++) {
            HistoryElement weighted = history.get(i);
            double time = history.peek().time - history.get(i).time;
            weighted.value = map(time / historyTimeSpan, 0, 1, 1, 0);
            weighted.value *= weighted.velocity / maxVelocity;
            summedWeights += (map(time / historyTimeSpan, 0, 1, 1,0)) * weighted.velocity / maxVelocity;
            weightedHistory.add(weighted);
        }

        return weightedHistory.stream().map((e) -> e.value).reduce(0.0, Double::sum) / summedWeights;
    }
}
