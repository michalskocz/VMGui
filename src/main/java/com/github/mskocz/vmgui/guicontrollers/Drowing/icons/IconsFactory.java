package com.github.mskocz.vmgui.guicontrollers.Drowing.icons;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class IconsFactory {

    private static final String RESOURCE_ROOT = "/com/github/mskocz/vmgui/gui/icons/";
    private static final double DEFAULT_SIZE = 32.0;

    private static final Map<IconType, SVGDocument> SVG_CACHE = new ConcurrentHashMap<>();
    private static final Map<IconType, Map<Integer, WritableImage>> IMAGE_CACHE = new ConcurrentHashMap<>();

    private IconsFactory() {
    }

    public static WritableImage getIcon(IconType type, double scale) {
        if (type == null) throw new IllegalArgumentException("IconType cannot be null");
        if (!Double.isFinite(scale) || scale <= 0.0) throw new IllegalArgumentException("Scale must be a finite value > 0");
        
        int size = Math.max(1, (int) Math.round(DEFAULT_SIZE * scale));

        var bySize = IMAGE_CACHE.computeIfAbsent(type, t -> new ConcurrentHashMap<>());
        var cached = bySize.computeIfAbsent(size, s -> 
                renderSvgToImage(SVG_CACHE.computeIfAbsent(type, IconsFactory::loadSvg), s, s));
        
        return new WritableImage(cached.getPixelReader(), size, size);
    }

    private static SVGDocument loadSvg(IconType type) {
        Objects.requireNonNull(type, "IconType is null");

        String resourcePath = RESOURCE_ROOT + type.getFileName();

        try (InputStream is = IconsFactory.class.getResourceAsStream(resourcePath)) {
            if (is == null) throw new IllegalArgumentException("SVG not found: " + resourcePath);
            
            var parser = XMLResourceDescriptor.getXMLParserClassName();
            var factory = new SAXSVGDocumentFactory(parser);
            
            return factory.createSVGDocument(null, is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load SVG: " + resourcePath, e);
        }
    }

    private static WritableImage renderSvgToImage(SVGDocument document, int targetWidth, int targetHeight) {
        try {
            var ctx = new BridgeContext(new UserAgentAdapter());
            ctx.setDynamicState(BridgeContext.STATIC);

            var builder = new GVTBuilder();
            var root = builder.build(ctx, document);

            var bounds = root.getPrimitiveBounds();
            if (bounds == null || bounds.isEmpty()) bounds = root.getBounds();
            if (bounds == null || bounds.isEmpty()) throw new IllegalStateException("SVG has no drawable bounds");
            
            var bufferedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
            var g2d = bufferedImage.createGraphics();
            try {
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                var transform = getAffineTransform(targetWidth, targetHeight, bounds);

                g2d.setTransform(transform);
                root.paint(g2d);
            } finally {
                g2d.dispose();
            }

            return SwingFXUtils.toFXImage(bufferedImage, null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to render SVG to image", e);
        }
    }

    private static AffineTransform getAffineTransform(int targetWidth, int targetHeight, Rectangle2D bounds) {
        double scaleX = targetWidth / bounds.getWidth();
        double scaleY = targetHeight / bounds.getHeight();
        double scale = Math.min(scaleX, scaleY);

        double tx = (targetWidth - bounds.getWidth() * scale) / 2.0 - bounds.getX() * scale;
        double ty = (targetHeight - bounds.getHeight() * scale) / 2.0 - bounds.getY() * scale;

        AffineTransform transform = new AffineTransform();
        transform.translate(tx, ty);
        transform.scale(scale, scale);
        return transform;
    }
}