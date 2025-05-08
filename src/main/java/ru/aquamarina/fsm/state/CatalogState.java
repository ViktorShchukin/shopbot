package ru.aquamarina.fsm.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aquamarina.fsm.form.CatalogForm;
import ru.aquamarina.fsm.form.Form;
import ru.aquamarina.fsm.FsmContextHolder;
import ru.aquamarina.model.command.*;
import ru.aquamarina.model.entity.Folder;
import ru.aquamarina.model.entity.Product;
import ru.aquamarina.model.entity.User;
import ru.aquamarina.model.error.Error;
import ru.aquamarina.model.error.NotSupportedCommand;
import ru.aquamarina.util.Result;

import java.util.Collection;
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
                    .map(product -> Result.ok(new ProductAboutState(user, product, 0)));
            case FolderCmd fld -> Result.ok(new CatalogState(user, fld.path()));
            case CatalogCmd ctg -> Result.ok(new CatalogState(user, "/"));
            case StartCmd start -> Result.ok(new IndexState(user));
            default -> Result.error(new NotSupportedCommand());
        };
    }

    @Override
    public Form getForm(FsmContextHolder context) {
        List<Product> products = context.getProductService().getByPathLike(path);
        List<Command> productInFolder = products.stream()
                .filter(product -> product.getPath().equals(path))
                .map(product -> new ProductAboutCmd(user, product.getName()))
                .collect(Collectors.toList());
        Set<Command> folderInFolder = products.stream()
                .map(Product::getPath)
                .filter(pth -> !pth.equals(path))
                // todo should trim folder to by current path
//                .map(this::mapToFolder)
                .map(pth -> new FolderCmd(user, pth))
                .collect(Collectors.toSet());
        List<Command> commands = List.of(
                new IndexCmd(user),
                new CatalogCmd(user)
        );
        List<Command> finalCommands = Stream.of(productInFolder, folderInFolder, commands).flatMap(Collection::stream).toList();
        return new CatalogForm(finalCommands);
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
