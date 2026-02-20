package ru.aquamarina.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import io.micronaut.core.io.scan.ClassPathResourceLoader;
import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.form.ErrorForm;
import ru.aquamarina.fsm.form.GuideForm;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.ExceptionWrapperError;
import ru.aquamarina.util.Result;

import java.io.*;

@Singleton
public class PdfService {

    private static final Logger log = LoggerFactory.getLogger(PdfService.class);

    private final ClassPathResourceLoader resourceLoader;
    private final PebbleEngine pebbleEngine;

    public PdfService(
            ClassPathResourceLoader resourceLoader,
            PebbleEngine pebbleEngine
    ) {
        this.resourceLoader = resourceLoader;
        this.pebbleEngine = pebbleEngine;
    }

    public Result<File, Error> getPdf() {
        try (Writer writer = new StringWriter()) {
            PebbleTemplate template = pebbleEngine.getTemplate("static/templates/instruction.html");
            template.evaluate(writer);
            String html = writer.toString();
            File tmpFile = File.createTempFile("pool-instruction", ".pdf");

            try (OutputStream os = new FileOutputStream(tmpFile)) {


                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.useFastMode();
                builder.useFont(
                        () -> getClass().getResourceAsStream("/static/font/DejaVuSans.ttf"),
                        "DejaVu Sans"
                );
                builder.withHtmlContent(html, null);
                builder.toStream(os);
                builder.run();
            }

            return Result.ok(tmpFile);
        } catch (Exception e) {
            log.error("error during pdf generation", e);
            return Result.error(new ExceptionWrapperError(e, "cant not produce pdf"));
        }
    }
}
