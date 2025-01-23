package LAB_B.Common.Cittadino.Panels;

import java.awt.Font;
import java.rmi.RemoteException;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import LAB_B.Common.Interface.Coordinate;
import LAB_B.Common.Interface.TipiPlot;
import LAB_B.Database.Database;

public class Plot1 extends JPanel {

    private TipiPlot tipo;
    private Database db;

    public Plot1(TipiPlot t, Database db, Coordinate city) {
        this.db = db;
        this.tipo = t;

        this.add(getPlot1(city));
    }

    // metodo di test per avere un plot
    private ChartPanel getPlot1(Coordinate city) {

        DefaultCategoryDataset dataset;
        try {
            dataset = db.getParametri(city, tipo);
            JFreeChart chart = ChartFactory.createLineChart(
                    tipo.getName(),
                    "Date",
                    tipo.getName().toLowerCase(),
                    dataset,
                    PlotOrientation.VERTICAL,
                    true, true, false);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new java.awt.Dimension(700, 500));
            
            CategoryPlot plot = chart.getCategoryPlot();
            CategoryAxis domainAxis = plot.getDomainAxis();
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

            return chartPanel;

        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

}
