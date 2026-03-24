package ru.aquamarina.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.guide.IPoolGuideCalculator;
import ru.aquamarina.guide.GuideType;
import ru.aquamarina.guide.dto.PoolGuideDto;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.ExceptionWrapperError;
import ru.aquamarina.util.Result;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class PdfService {

    private static final Logger log = LoggerFactory.getLogger(PdfService.class);

    private final PebbleEngine pebbleEngine;
    private final PoolInfoService poolInfoService;

    public PdfService(
            PebbleEngine pebbleEngine,
            PoolInfoService poolInfoService) {
        this.pebbleEngine = pebbleEngine;
        this.poolInfoService = poolInfoService;
    }

    public Result<File, Error> getPdf(User user, GuideType guideType) {
        return poolInfoService.getPoolInfoByUserId(user.getId())
                .mapValue(guideType::getCalculator)
                .mapValue(IPoolGuideCalculator::evaluate)
                .map(dto -> generateHtml(dto, guideType))
                .map(this::generatePdf);
    }

    private Result<String, Error> generateHtml(PoolGuideDto guideDto, GuideType guideType) {
        Map<String, Object> context = new HashMap<>();
        context.put("poolGuideDto", guideDto);

        try (Writer writer = new StringWriter()) {
            PebbleTemplate template = pebbleEngine.getTemplate(guideType.getTemplate());
            template.evaluate(writer, context);
            String html = writer.toString();

            return Result.ok(html);
        } catch (Exception e) {
            log.error("error during html generation", e);
            return Result.error(new ExceptionWrapperError(e, "can't generate html from tempalte"));
        }
    }

    private Result<File, Error> generatePdf(String html) {
        try {
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
