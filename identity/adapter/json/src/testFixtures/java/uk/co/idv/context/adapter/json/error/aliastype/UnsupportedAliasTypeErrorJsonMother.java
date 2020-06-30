package uk.co.idv.context.adapter.json.error.aliastype;

import static uk.co.mruoc.file.content.ContentLoader.loadContentFromClasspath;

public interface UnsupportedAliasTypeErrorJsonMother {

    static String unsupportedAliasTypeErrorJson() {
        return loadContentFromClasspath("error/unsupported-alias-type-error.json");
    }

}
