package ru.aquamarina.instruction;

import ru.aquamarina.model.error.Error;
import ru.aquamarina.util.Result;

import java.io.File;

public interface PdfFactory {

    Result<File, Error> getPdf();
}
