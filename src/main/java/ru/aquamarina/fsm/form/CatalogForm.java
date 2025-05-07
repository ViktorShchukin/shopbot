package ru.aquamarina.fsm.form;

import ru.aquamarina.api.bot.View;
import ru.aquamarina.model.command.FolderCmd;
import ru.aquamarina.model.command.IndexCmd;
import ru.aquamarina.model.command.ProductAboutCmd;
import ru.aquamarina.model.entity.Folder;
import ru.aquamarina.model.entity.Product;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public record CatalogForm(List<Product> products, Collection<Folder> folders) implements Form {

    @Override
    public List<String> getCommands() {
        List<String> productAbout = products.stream()
                .map(Product::getName)
                .map(name -> ProductAboutCmd.NAME + "?" + name)
                .toList();
        List<String> foldersCommand = folders.stream()
                .map(folder -> FolderCmd.NAME + "?" + folder.path())
                .toList();
        return Stream.of(productAbout, foldersCommand, Collections.singleton(IndexCmd.NAME))
                .flatMap(Collection::stream)
                .toList();
    }

    @Override
    public void draw(View view) {
        view.drawCatalogForm(this);
    }
}
