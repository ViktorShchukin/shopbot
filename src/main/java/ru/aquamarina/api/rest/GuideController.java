package ru.aquamarina.api.rest;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.server.types.files.SystemFile;
import ru.aquamarina.guide.GuideType;
import ru.aquamarina.guide.dto.PoolInfoDto;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.service.PdfService;
import ru.aquamarina.util.Result;
import ru.aquamarina.util.ResultError;
import ru.aquamarina.util.ResultOk;

import java.io.File;

@Controller("/guide")
public class GuideController {

    private final PdfService pdfService;

    public GuideController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @Post("/pdf")
    public HttpResponse<SystemFile> getPdf(
            @Body PoolInfoDto poolInfo
    ) {
        Result<File, Error> res = pdfService.getPdf(poolInfo, GuideType.STEP_BY_STEP);

        return switch (res) {
            case ResultOk<File, Error> ok -> HttpResponse.ok(new SystemFile(ok.result()));
            case ResultError<File, Error> err -> HttpResponse.serverError();
        };
    }
}
