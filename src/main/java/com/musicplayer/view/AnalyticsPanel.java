package com.musicplayer.view;

import com.musicplayer.controller.MusicPlayerController;
import com.musicplayer.repository.AnalyticsDAO;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class AnalyticsPanel extends JPanel {
    private MusicPlayerController controller;
    private AnalyticsDAO analyticsDAO;
    private JPanel chartsPanel;

    private static final Color BG_PRIMARY = new Color(18, 18, 18);
    private static final Color BG_SECONDARY = new Color(30, 30, 30);
    private static final Color BG_CHART = new Color(40, 40, 40);
    private static final Color GRID_COLOR = new Color(60, 60, 60);
    private static final Color CHART_BLUE = new Color(100, 180, 255);

    public AnalyticsPanel(MusicPlayerController controller) {
        this.controller = controller;
        this.analyticsDAO = controller.getAnalyticsDAO();

        setLayout(new BorderLayout());
        setBackground(BG_PRIMARY);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        chartsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        chartsPanel.setBackground(BG_PRIMARY);
        add(chartsPanel, BorderLayout.CENTER);

        analyticsDAO.debugPrintData();
        loadAnalytics();
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("Analytics Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setBackground(new Color(30, 215, 96));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshBtn.setPreferredSize(new Dimension(120, 35));

        refreshBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                refreshBtn.setBackground(new Color(30, 215, 96).brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                refreshBtn.setBackground(new Color(30, 215, 96));
            }
        });

        refreshBtn.addActionListener(e -> {
            System.out.println("\nManual refresh triggered...");
            analyticsDAO.debugPrintData();
            loadAnalytics();
        });

        header.add(title, BorderLayout.WEST);
        header.add(refreshBtn, BorderLayout.EAST);

        return header;
    }

    public void loadAnalytics() {
        System.out.println("\nLoading Analytics Dashboard...");
        chartsPanel.removeAll();

        System.out.println("\nLoading Top Tracks...");
        JFreeChart topTracksChart = createTopTracksChart();
        chartsPanel.add(new ChartPanel(topTracksChart));

        System.out.println("\nLoading Top Artists...");
        JFreeChart topArtistsChart = createTopArtistsChart();
        chartsPanel.add(new ChartPanel(topArtistsChart));

        System.out.println("\nLoading Plays Per Day...");
        JFreeChart dailyPlaysChart = createDailyPlaysChart();
        chartsPanel.add(new ChartPanel(dailyPlaysChart));

        System.out.println("\nLoading Summary Statistics...");
        JPanel summary = createSummaryPanel();
        chartsPanel.add(summary);

        chartsPanel.revalidate();
        chartsPanel.repaint();

        System.out.println("\nAnalytics dashboard loaded successfully!\n");
    }

    private JFreeChart createTopTracksChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Map<String, Object>> topTracks = analyticsDAO.getTopTracks(5);

        if (topTracks.isEmpty()) {
            System.out.println("No top tracks data found");
            dataset.addValue(0, "Plays", "No data yet");
        } else {
            System.out.println("Adding " + topTracks.size() + " tracks to chart");
            for (Map<String, Object> track : topTracks) {
                String title = (String) track.get("title");
                String artist = (String) track.get("artist");
                int count = (int) track.get("count");

                String label = artist + " - " + title;
                if (label.length() > 30) {
                    label = label.substring(0, 27) + "...";
                }
                dataset.addValue(count, "Plays", label);
                System.out.println("   Added: " + label + " (" + count + " plays)");
            }
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Top 5 Most Played Tracks",
                "Track",
                "Plays",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);

        styleChart(chart);

        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, CHART_BLUE);
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());

        return chart;
    }

    private JFreeChart createTopArtistsChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Map<String, Object>> topArtists = analyticsDAO.getTopArtists(5);

        if (topArtists.isEmpty()) {
            System.out.println("No artist data found");
            dataset.addValue(0, "Plays", "No data yet");
        } else {
            System.out.println("Adding " + topArtists.size() + " artists to chart");
            for (Map<String, Object> artist : topArtists) {
                String name = (String) artist.get("name");
                int count = (int) artist.get("count");

                String label = name;
                if (label.length() > 25) {
                    label = label.substring(0, 22) + "...";
                }
                dataset.addValue(count, "Plays", label);
                System.out.println("   " + name + ": " + count + " plays");
            }
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Top 5 Artists",
                "Artist",
                "Plays",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);

        styleChart(chart);

        CategoryPlot plot = chart.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, CHART_BLUE);
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());

        return chart;
    }

    private JFreeChart createDailyPlaysChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Integer> dailyPlays = analyticsDAO.getPlaysPerDay(7);

        if (dailyPlays.isEmpty()) {
            System.out.println("No daily plays data found");
            dataset.addValue(0, "Plays", "No recent activity");
        } else {
            System.out.println("Adding " + dailyPlays.size() + " days to chart");
            dailyPlays.forEach((day, count) -> {
                String[] parts = day.split("-");
                String shortDate = parts.length == 3 ? parts[1] + "/" + parts[2] : day;
                dataset.addValue(count, "Plays", shortDate);
                System.out.println("   " + shortDate + ": " + count + " plays");
            });
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Plays Per Day (Last 7 Days)",
                "Date",
                "Plays",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);

        styleChart(chart);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.getRenderer().setSeriesPaint(0, CHART_BLUE);

        return chart;
    }

    private void styleChart(JFreeChart chart) {
        chart.setBackgroundPaint(BG_SECONDARY);
        chart.getTitle().setPaint(Color.WHITE);
        chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 16));

        Plot plot = chart.getPlot();
        plot.setBackgroundPaint(BG_CHART);
        plot.setOutlinePaint(GRID_COLOR);

        if (plot instanceof CategoryPlot) {
            CategoryPlot categoryPlot = (CategoryPlot) plot;
            categoryPlot.setDomainGridlinePaint(GRID_COLOR);
            categoryPlot.setRangeGridlinePaint(GRID_COLOR);
            categoryPlot.getDomainAxis().setLabelPaint(Color.WHITE);
            categoryPlot.getDomainAxis().setTickLabelPaint(Color.LIGHT_GRAY);
            categoryPlot.getDomainAxis().setLabelFont(new Font("Segoe UI", Font.PLAIN, 12));
            categoryPlot.getDomainAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));
            categoryPlot.getRangeAxis().setLabelPaint(Color.WHITE);
            categoryPlot.getRangeAxis().setTickLabelPaint(Color.LIGHT_GRAY);
            categoryPlot.getRangeAxis().setLabelFont(new Font("Segoe UI", Font.PLAIN, 12));
            categoryPlot.getRangeAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 10));

            categoryPlot.getDomainAxis().setCategoryLabelPositions(
                    org.jfree.chart.axis.CategoryLabelPositions.UP_45);
        }
    }

    private JPanel createSummaryPanel() {
        JPanel summary = new JPanel();
        summary.setLayout(new BoxLayout(summary, BoxLayout.Y_AXIS));
        summary.setBackground(BG_SECONDARY);
        summary.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GRID_COLOR, 1),
                BorderFactory.createEmptyBorder(25, 20, 25, 20)));

        JLabel titleLabel = new JLabel("Summary Statistics");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        summary.add(titleLabel);

        summary.add(Box.createVerticalStrut(20));

        JSeparator separator = new JSeparator();
        separator.setForeground(GRID_COLOR);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        summary.add(separator);

        summary.add(Box.createVerticalStrut(15));

        int totalTracks = analyticsDAO.getTotalPlayCount();
        System.out.println("   Total Plays: " + totalTracks);

        String mostActiveDay = analyticsDAO.getMostActiveDay();
        System.out.println("   Most Active: " + mostActiveDay);

        summary.add(createStatRow("Total Plays", String.valueOf(totalTracks)));
        summary.add(Box.createVerticalStrut(12));

        summary.add(createStatRow("Most Active", mostActiveDay));

        return summary;
    }

    private JPanel createStatRow(String label, String value) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
        row.setOpaque(false);
        row.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelComponent.setForeground(new Color(180, 180, 180));
        labelComponent.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        row.add(labelComponent);
        row.add(Box.createVerticalStrut(3));
        row.add(valueLabel);

        return row;
    }
}