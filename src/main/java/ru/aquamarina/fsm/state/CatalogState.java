package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.form.CatalogForm;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.BasketRow;
import ru.aquamarina.model.entity.Folder;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.PathUtil;
import ru.aquamarina.util.Result;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CatalogState implements FsmState {

    public static final String NAME = "Catalog";

    private final Logger log = LoggerFactory.getLogger(CatalogState.class);

    private final User user;
    private final String path;

    public CatalogState(User user, String path) {
        this.user = user;
        this.path = path;
    }

    @Override
    public Result<FsmState, Error> doWork(FsmContextHolder context, Command command) {
        return switch (command) {
            case IndexCmd ndx -> Result.ok(new IndexState(user));
            case ProductAboutCmd pbt -> context.getProductService()
                    .getByName(pbt.productName())
                    .map(product -> {
                        Long quantity = context.getBasketService()
                                .getBasketRow(user).stream()
                                .filter(bsk -> bsk.getProductId().equals(product.getId()))
                                .findFirst()
                                .map(BasketRow::getQuantity)
                                .orElseGet(() -> 0L);
                        return Result.ok(new ProductAboutState(user, product, quantity));
                    });

            case FolderCmd fld -> Result.ok(new CatalogState(user, fld.path()));
            case CatalogCmd ctg -> Result.ok(new CatalogState(user, "/"));
            case BasketCmd bsk -> Result.ok(new BasketState(user));
            case StartCmd start -> Result.ok(new IndexState(user));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        List<Product> products = context.getProductService().getByPathLike(path);
        List<Product> productInFolder = products.stream()
                .filter(product -> product.getPath().equals(path))
                .toList();
        Set<Folder> folderInFolder = products.stream()
                .map(Product::getPath)
                .filter(folderPth -> !folderPth.equals(path))
                .map(folderPth -> PathUtil.getSubfolder(path, folderPth))
                .map(this::mapToFolder)
                .collect(Collectors.toSet());
        return new CatalogForm(productInFolder, List.copyOf(folderInFolder), path);
    }

    @Override
    public String toString() {
        return NAME;
    }

    private Folder mapToFolder(String folderPath) {
        // todo maybe crate special util class???
        var name = folderPath.substring(path.length()).split("/")[0];
        return new Folder(name, folderPath);
    }
}
