package org.digijava.module.orgProfile.helper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.AttributedString;
import java.util.Iterator;
import java.util.List;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.data.general.PieDataset;
import org.jfree.text.G2TextMeasurer;
import org.jfree.text.TextBlock;
import org.jfree.text.TextFragment;
import org.jfree.text.TextLine;
import org.jfree.text.TextUtilities;
import org.jfree.ui.Size2D;

/**
 *
 * the ugliest workaround (don't look with disdain :P), because jfreechart doesn't support fix sized legend ...
 */
public class PieChartLegendGenerator implements PieSectionLabelGenerator {

    private float maxWidth = 200;

    public PieChartLegendGenerator() {
    }

    public PieChartLegendGenerator(float maxWidth) {
        this.maxWidth = maxWidth;
    }

    public String generateSectionLabel(PieDataset dataset, Comparable key) {
        String result = "";
        String newKey = key.toString();
        BufferedImage image = new BufferedImage(800, 800, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = image.createGraphics();
        Font font = new Font("Arial", Font.PLAIN, 10);
        TextBlock text = TextUtilities.createTextBlock(newKey,font, Color.BLACK, maxWidth, new G2TextMeasurer(g2));
        List<TextLine> lines = text.getLines();
        Iterator<TextLine> lineIter = lines.iterator();
        while (lineIter.hasNext()) {
            TextLine line = lineIter.next();
            Size2D size = line.calculateDimensions(g2);
            Double width = size.getWidth();
            String lastText = line.getLastTextFragment().getText();
            while (width < maxWidth) {
                line.removeFragment(line.getFirstTextFragment());
                line.addFragment(new TextFragment(lastText + " ",font));
                lastText = line.getLastTextFragment().getText();
                width = line.calculateDimensions(g2).getWidth();
            }

            result += lastText + '\n';
        }
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        
        return result;
    }

    public AttributedString generateAttributedSectionLabel(PieDataset dataset, Comparable key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
