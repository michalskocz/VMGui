package build.utils;

import java.io.File;
import java.util.Objects;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SvgToPngConventer {
    public static void main(String[] args) throws Exception {
        try {
            if (args.length != 2) {
                System.err.println("Usage: SvgToPngConventer <svgDir> <pngDir>");
                System.exit(1);
            }
            File svgDir = new File(args[0]);
            File pngDir = new File(args[1]);
            convertFolder(svgDir, pngDir);
        } catch (Exception ignore) {}
    }

    public static void convertFolder(File svgDir, File pngDir) throws Exception {
        try {
            if (!pngDir.exists()) pngDir.mkdirs();
            File[] svgFiles = svgDir.listFiles((d, n) -> n.toLowerCase().endsWith(".svg"));
            if (svgFiles == null) return;

            for (File svgFile : svgFiles) {
                File pngFile = new File(pngDir, svgFile.getName().replace(".svg", ".png"));
                PNGTranscoder transcoder = new PNGTranscoder();
                TranscoderInput input = new TranscoderInput(new FileInputStream(svgFile));
                TranscoderOutput output = new TranscoderOutput(new FileOutputStream(pngFile));
                transcoder.transcode(input, output);
                System.out.println("Converted: " + pngFile.getAbsolutePath());
            }
        } catch (Exception ignore) {}
    }
}