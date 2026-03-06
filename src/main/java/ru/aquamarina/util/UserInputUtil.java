package ru.aquamarina.util;

import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.validation.NotAllowedValue;

public class UserInputUtil {

    public static Result<Long, Error> validateAboveZero(
            Long value
    ) {
        return value > 0
                ? Result.ok(value)
                : Result.error(new NotAllowedValue());
    }

    public static Result<Double, Error> centimeterToMeter(
            Long value
    ) {
        double delimeter = 100d;
        return Result.ok(value / delimeter);
    }
}
