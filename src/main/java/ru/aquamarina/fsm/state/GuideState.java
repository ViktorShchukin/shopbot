package ru.aquamarina.fsm.state;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.fsm.form.ErrorForm;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.form.GuideForm;
import ru.aquamarina.model.command.Command;
import ru.aquamarina.model.command.IndexCmd;
import ru.aquamarina.model.command.StartCmd;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

import java.io.*;

public class GuideState implements FsmState {

    public static final String NAME = "Guide";

    private final Logger log = LoggerFactory.getLogger(GuideState.class);

    private final User user;

    public GuideState(User user) {
        this.user = user;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case IndexCmd ndx -> Result.ok(new IndexState(user));
            case StartCmd start-> Result.ok(new IndexState(user, true));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        PebbleEngine engine = context.getPebbleEngine();
        try(Writer writer = new StringWriter()){
            PebbleTemplate template = engine.getTemplate("static/templates/instruction.html");
            template.evaluate(writer);
            String html = writer.toString();
            File tmpFile = File.createTempFile("pool-instruction", ".pdf");
            OutputStream os = new FileOutputStream(tmpFile);

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.useFont(new File("/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf"), "DejaVu Sans");
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();

            os.close();
            return new GuideForm(user, tmpFile);
        } catch (Exception e){
            log.error("error during pdf generation", e);
            return new ErrorForm(user);
        }
    }

    @Override
    public String toString() {
        return NAME;
    }
}
