package org.digijava.module.aim.util;

import org.digijava.kernel.ampapi.endpoints.indicator.IndicatorEPConstants;
import org.digijava.module.aim.dbentity.AmpIndicatorColor;

import java.util.*;

public class ColorRampUtil {

    public static final int MIN_CLASSES_INDEX = 2;

    private static final String[][][] colorRamps = {
            {
                    { "#e9ced2", "#8d1874" },
                    { "#e9ced2", "#e061a2", "#8d1874" },
                    { "#e9ced2", "#e08cab", "#cb4196", "#8d1874" },
                    { "#e9ced2", "#e19eb3", "#e061a2", "#bb368e", "#8d1874" },
                    { "#e9ced2", "#e1a8b8", "#e07ca7", "#d74a9c", "#b23089", "#8d1874" },
                    { "#e9ced2", "#e2afbc", "#e08cab", "#e061a2", "#cb4196", "#ac2c86", "#8d1874" },
                    { "#e9ced2", "#e3b3bf", "#e096b0", "#e075a5", "#dd4d9f", "#c23b92", "#a72984", "#8d1874" },
                    { "#e9ced2", "#e3b7c2", "#e19eb3", "#e082a8", "#e061a2", "#d3469a", "#bb368e", "#a42782", "#8d1874" },
                    { "#e9ced2", "#e4b9c3", "#e1a4b6", "#e08cab", "#e071a4", "#e050a0", "#cb4196", "#b6338c",
                            "#a12580", "#8d1874" } },
            {
                    { "#e9cfc1", "#981538" },
                    { "#e9cfc1", "#dc6f50", "#981538" },
                    { "#e9cfc1", "#e39070", "#cf4c3a", "#981538" },
                    { "#e9cfc1", "#e3a186", "#dc6f50", "#c93530", "#981538" },
                    { "#e9cfc1", "#e3ab92", "#e4815e", "#d45b43", "#c22a31", "#981538" },
                    { "#e9cfc1", "#e4b19b", "#e39070", "#dc6f50", "#cf4c3a", "#bb2734", "#981538" },
                    { "#e9cfc1", "#e4b6a0", "#e39a7d", "#e17c5a", "#d66146", "#cc3f34", "#b62435", "#981538" },
                    { "#e9cfc1", "#e5b9a4", "#e3a186", "#e38765", "#dc6f50", "#d2553f", "#c93530", "#b32236", "#981538" },
                    { "#e9cfc1", "#e5bca8", "#e3a78d", "#e39070", "#e07958", "#d76448", "#cf4c3a", "#c72d2e",
                            "#b02137", "#981538" }, },
            {
                    { "#ddd69d", "#6c4513" },
                    { "#ddd69d", "#a68a4f", "#6c4513" },
                    { "#ddd69d", "#b9a367", "#947239", "#6c4513" },
                    { "#ddd69d", "#c2b074", "#a68a4f", "#8a672f", "#6c4513" },
                    { "#ddd69d", "#c7b77c", "#b1995d", "#9b7c42", "#846029", "#6c4513" },
                    { "#ddd69d", "#cbbc81", "#b9a367", "#a68a4f", "#947239", "#805b25", "#6c4513" },
                    { "#ddd69d", "#cec085", "#beaa6e", "#ae9559", "#9e8046", "#8e6c34", "#7d5823", "#6c4513" },
                    { "#ddd69d", "#cfc388", "#c2b074", "#b49d61", "#a68a4f", "#98783f", "#8a672f", "#7b5621", "#6c4513" },
                    { "#ddd69d", "#d1c58a", "#c5b478", "#b9a367", "#ac9357", "#a08248", "#947239", "#87632c",
                            "#7a541f", "#6c4513" } },
            {
                    { "#aee29c", "#525013" },
                    { "#aee29c", "#81964f", "#525013" },
                    { "#aee29c", "#90af67", "#727e39", "#525013", },
                    { "#aee29c", "#97bc73", "#81964f", "#6a722f", "#525013" },
                    { "#aee29c", "#9cc37b", "#8aa55d", "#788742", "#656b29", "#525013" },
                    { "#aee29c", "#9ec881", "#90af67", "#81964f", "#727e39", "#626625", "#525013" },
                    { "#aee29c", "#a1cc84", "#94b66e", "#87a159", "#7b8b45", "#6d7733", "#606322", "#525013" },
                    { "#aee29c", "#a2cf87", "#97bc73", "#8ca961", "#81964f", "#76843e", "#6a722f", "#5e6121", "#525013" },
                    { "#aee29c", "#a3d189", "#9ac078", "#90af67", "#869e57", "#7c8e47", "#727e39", "#676e2c",
                            "#5d5f1f", "#525013" } },
            {
                    { "#a1e2ca", "#225913" },
                    { "#a1e2ca", "#519d70", "#225913" },
                    { "#a1e2ca", "#6ab48f", "#3a8751", "#225913" },
                    { "#a1e2ca", "#77bf9e", "#519d70", "#2f7b40", "#225913" },
                    { "#a1e2ca", "#7fc6a7", "#5fab83", "#43905e", "#297536", "#225913" },
                    { "#a1e2ca", "#84caad", "#6ab48f", "#519d70", "#3a8751", "#25702e", "#225913" },
                    { "#a1e2ca", "#88ceb1", "#71ba97", "#5ba77d", "#479363", "#348047", "#226d29", "#225913" },
                    { "#a1e2ca", "#8bd0b4", "#77bf9e", "#63ae87", "#519d70", "#3f8c59", "#2f7b40", "#206b24", "#225913" },
                    { "#a1e2ca", "#8ed2b7", "#7bc3a3", "#6ab48f", "#59a47a", "#499566", "#3a8751", "#2c783a",
                            "#1f6920", "#225913" } },
            {
                    { "#a4dfe4", "#155848" },
                    { "#a4dfe4", "#549a93", "#155848" },
                    { "#a4dfe4", "#6db0ae", "#3d8379", "#155848" },
                    { "#a4dfe4", "#7abcbb", "#549a93", "#32786c", "#155848" },
                    { "#a4dfe4", "#82c3c3", "#62a8a3", "#468c83", "#2c7265", "#155848" },
                    { "#a4dfe4", "#87c7c9", "#6db0ae", "#549a93", "#3d8379", "#286d60", "#155848" },
                    { "#a4dfe4", "#8bcbcd", "#74b7b5", "#5ea39e", "#4a9088", "#377d72", "#256a5c", "#155848" },
                    { "#a4dfe4", "#8ecdd0", "#7abcbb", "#66aba7", "#549a93", "#42897f", "#32786c", "#23685a", "#155848" },
                    { "#a4dfe4", "#91cfd2", "#7ec0c0", "#6db0ae", "#5ca19c", "#4c928a", "#3d8379", "#2f7568",
                            "#216658", "#155848" } },
            {
                    { "#c6d6e8", "#16565c" },
                    { "#c6d6e8", "#5796ad", "#16565c" },
                    { "#c6d6e8", "#70adcb", "#3f8191", "#16565c" },
                    { "#c6d6e8", "#7eb8da", "#5796ad", "#347683", "#16565c" },
                    { "#c6d6e8", "#8bbedf", "#66a4bf", "#48899c", "#2e6f7b", "#16565c" },
                    { "#c6d6e8", "#97c2e0", "#70adcb", "#5796ad", "#3f8191", "#2a6b76", "#16565c" },
                    { "#c6d6e8", "#9fc4e1", "#78b3d3", "#61a0ba", "#4c8da1", "#397a89", "#276872", "#16565c" },
                    { "#c6d6e8", "#a5c6e2", "#7eb8da", "#6aa7c3", "#5796ad", "#458698", "#347683", "#24656f", "#16565c" },
                    { "#c6d6e8", "#a9c8e2", "#82bcdf", "#70adcb", "#5f9eb7", "#4f8fa4", "#3f8191", "#31727f",
                            "#23646d", "#16565c" } },
            {
                    { "#d9d1e8", "#18517b" },
                    { "#d9d1e8", "#7c88db", "#18517b" },
                    { "#d9d1e8", "#a29fdc", "#4775d0", "#18517b" },
                    { "#d9d1e8", "#b2abde", "#7c88db", "#3a6db7", "#18517b" },
                    { "#d9d1e8", "#bab2df", "#9595db", "#5b7cdb", "#3368aa", "#18517b" },
                    { "#d9d1e8", "#c0b7e0", "#a29fdc", "#7c88db", "#4775d0", "#2e64a1", "#18517b" },
                    { "#d9d1e8", "#c4bbe1", "#aca5dd", "#8e91db", "#6680db", "#4071c1", "#2b619b", "#18517b" },
                    { "#d9d1e8", "#c6bde2", "#b2abde", "#9a99db", "#7c88db", "#5079db", "#3a6db7", "#295f97", "#18517b" },
                    { "#d9d1e8", "#c8c0e2", "#b7afde", "#a29fdc", "#8a8fdb", "#6b81db", "#4775d0", "#366aaf",
                            "#275e94", "#18517b" } },
            {
                    { "#e8cde1", "#741eaa" },
                    { "#e8cde1", "#cd63da", "#741eaa" },
                    { "#e8cde1", "#dc87db", "#b348ce", "#741eaa" },
                    { "#e8cde1", "#de9ad9", "#cd63da", "#a43dc6", "#741eaa" },
                    { "#e8cde1", "#dfa5d9", "#d679db", "#c051d5", "#9b37c0", "#741eaa" },
                    { "#e8cde1", "#e0acd9", "#dc87db", "#cd63da", "#b348ce", "#9432bd", "#741eaa" },
                    { "#e8cde1", "#e1b1da", "#dd92da", "#d473da", "#c554d8", "#ab42c9", "#902fba", "#741eaa" },
                    { "#e8cde1", "#e2b4db", "#de9ad9", "#d87edb", "#cd63da", "#bb4dd3", "#a43dc6", "#8c2db8", "#741eaa" },
                    { "#e8cde1", "#e3b7db", "#dfa0d9", "#dc87db", "#d36fda", "#c857da", "#b348ce", "#9f39c3",
                            "#8a2bb7", "#741eaa" } },
            {
                    { "#FC0915", "#FF4528"},
                    { "#FC0915", "#FF4528", "#FD7C32" },
                    { "#FC0915", "#FF4528", "#FD7C32", "#FBB03D"},
                    { "#FC0915", "#FF4528", "#FD7C32", "#FBB03D", "#FCDF3F"},
                    { "#FC0915", "#FF4528", "#FD7C32", "#FBB03D", "#FCDF3F", "#DFF650"},
                    { "#FC0915", "#FF4528", "#FD7C32", "#FBB03D", "#FCDF3F", "#DFF650", "#B9F360"},
                    { "#FC0915", "#FF4528", "#FD7C32", "#FBB03D", "#FCDF3F", "#DFF650", "#B9F360", "#81F354" },
                    { "#FC0915", "#FF4528", "#FD7C32", "#FBB03D", "#FCDF3F", "#DFF650", "#B9F360", "#81F354", "#3DF65D" },
                    { "#FC0915", "#FF4528", "#FD7C32", "#FBB03D", "#FCDF3F", "#DFF650", "#B9F360", "#81F354", "#3DF65D", "#0adc2e"} },
            {
                    { "#80B8F5", "#989BF9", "#C69CF9","#F59BFD","#DD77A7"},
                    { "#80B8F5", "#989BF9", "#C69CF9","#F59BFD","#DD77A7"},
                    { "#80B8F5", "#989BF9", "#C69CF9","#F59BFD","#DD77A7"},
                    { "#80B8F5", "#989BF9", "#C69CF9","#F59BFD","#DD77A7"},
                    { "#80B8F5", "#989BF9", "#C69CF9","#F59BFD","#DD77A7"},
                    { "#80B8F5", "#989BF9", "#C69CF9","#F59BFD","#DD77A7"},
                    { "#80B8F5", "#989BF9", "#C69CF9","#F59BFD","#DD77A7" },
                    { "#80B8F5", "#989BF9", "#C69CF9","#F59BFD","#DD77A7" },
                    { "#80B8F5", "#989BF9", "#C69CF9","#F59BFD","#DD77A7"} }  

    };

    private static final String[] DEFAULT_RAMP = colorRamps[0][0];

    public static String[][] getColorRampCollectionByIndex(Integer index) {
        if (index >= colorRamps.length) {
            return null;
        }
        return colorRamps[index];
    }

    public static String[] getColorRamp(int index, Long classes) {
        if (index >= colorRamps.length || (classes - 1) > colorRamps[index].length) {
            return DEFAULT_RAMP;
        }
        classes = classes < MIN_CLASSES_INDEX ? MIN_CLASSES_INDEX  : classes;
        return colorRamps[index][(classes.intValue() - MIN_CLASSES_INDEX)];
    }

    public static String[][] getColors() {
        String[][] colors = new String [colorRamps.length][IndicatorEPConstants.COLOR_RAMP_INDEX];
        for (int i = 0; i < colorRamps.length; i++) {
            colors[i] = colorRamps[i][IndicatorEPConstants.COLOR_RAMP_INDEX];
        }

        return colors;
    }

    public static int getColorId(String color) {
        int colorId = 0;
        for (int i = 0; i < colorRamps.length; i++) {
            if (color.equals(colorRamps[i][IndicatorEPConstants.COLOR_RAMP_INDEX][0])) {
                colorId = i;
                break;
            }
        }
        return colorId;
    }

    public static int getIndexByColors(Set<AmpIndicatorColor> colorRamp) {
        String[] selectedColors = getColorArray(colorRamp);
        for (int i = 0; i < colorRamps.length; i++) {
            String[] colors = colorRamps[i][selectedColors.length - 2];
            boolean isEqual = true;
            for (int j = 0; j < colors.length; j++) {
                if (!colors[j].equals(selectedColors[j])) {
                    isEqual = false;
                    break;
                }
            }
            if (isEqual) {
                return i;
            }
        }
        return -1;
    }

    private static String[] getColorArray(Set<AmpIndicatorColor> colorRamp) {
        String colors[] = new String [colorRamp.size()];
        List<AmpIndicatorColor> colorList = new ArrayList<AmpIndicatorColor>(colorRamp);
        Collections.sort(colorList, new Comparator<AmpIndicatorColor>() {
            @Override
            public int compare(AmpIndicatorColor o1, AmpIndicatorColor o2) {
                return o1.getPayload().compareTo(o2.getPayload());
            }
        });
        for (int i = 0; i < colorList.size(); i++) {
            AmpIndicatorColor color = colorList.get(i);
            colors[i] = color.getColor();
        }
        return colors;

    }

}
